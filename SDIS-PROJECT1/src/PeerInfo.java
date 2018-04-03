import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Logger;

/*
1. testClient sends file
2. initiating peer id1 connects
3. peer id2 accepts connection and starts handler thread
4. peer id1 send msg c
5. peer id2 send msg ACK connec. closed
6. peer id2 connects
7. peer id1 accepts connection and starts handler thread
8. peer id2 sends msg
 */

public class PeerInfo {

    public PeerConnection controlCh;
    public PeerConnection backupCh;
    public PeerConnection restoreCh;
    public ArrayList<String> chunksFileID;
    public ArrayList<String> chunkFilePaths;
    public static int peerID = 0;
    private ArrayList<File> myHomeFiles;

    private ChunkDB chunkDB;

    private String path_base = "./bin/";

    



    public PeerInfo(String serverId, String protocolVersion, String serviceAccessPoint, InetAddress mcAddr, int mcPort,
                    InetAddress mdbAddr, int mdbPort, InetAddress mdrAddr, int mdrPort) throws IOException{
        System.out.println("Peer " + this.peerID + " is connecting to network");
        controlCh = new PeerConnection(mcAddr, mcPort, this); // = MC
        backupCh= new PeerConnection(mdbAddr, mdbPort, this);
        restoreCh = new PeerConnection(mdrAddr, mdrPort, this);
        this.chunkDB=new ChunkDB();
        PeerRmi initiatorPeer = new PeerRmi(this);
        try {
            Registry registry = LocateRegistry.getRegistry();
            registry.rebind(serviceAccessPoint, initiatorPeer);
        } catch (Exception e) {
            System.out.println("Peer error: " + e.getMessage());
            e.printStackTrace();
        }
        //initiatorPeer.backup("./bin/Peer0/my_files/img1.jpg",3);
        myHomeFiles = new ArrayList<File>(0);
        chunkFilePaths= new ArrayList<String>();
        chunksFileID=new ArrayList<String>();
        String pathname="./bin/"+"Peer"+Integer.toString(this.peerID)+"/"+"my_files";
        File file = new File(pathname);
        file.mkdirs();
        String pathname1="./bin/"+"Peer"+Integer.toString(this.peerID)+"/"+"my_chunks";
        File file1 = new File(pathname1);
        file1.mkdirs();
        readFilesFromPeer();
        System.out.println(this.myHomeFiles.size());
        controlCh.recieveMessage();
        backupCh.recieveMessage();
        restoreCh.recieveMessage();
        this.incrementPeerId();
    }
    public void readFilesFromPeer(){

        File folder = new File("bin/Peer0/my_files");
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length-1; i++) {
            if (listOfFiles[i].isFile()) {
                this.myHomeFiles.add(listOfFiles[i]);
            }
        }
    }

    public void incrementPeerId(){
        this.peerID++;
    }

    public String getPath_base(){
        return path_base;
    }

    public ChunkDB getChunkDB(){
        return chunkDB;
    }

    public void requestChunkBackup(String fileId, int chunkNo, int repDeg, byte[] currChunk){
        Runnable runnable = () -> {
            String peerName = Integer.toString(this.peerID);
            String chunkNumb = Integer.toString(chunkNo);
            Message message = new Message("PUTCHUNK", "RMI", peerName, fileId, chunkNumb, repDeg,currChunk);
            byte[] finalMsg = message.getMsg();
            while(true){
                int count=0;
                while(count<5) {
                    Random rand=new Random();
                    try {
                        if(backupCh.sendMessage(finalMsg))
                            System.out.println("Backup delivered to peer");
                        else{
                            System.out.println("FAILED TO SEND PUTCHUNK MSG");
                            if(count>5)
                                break;
                            else continue;
                        }
                        try {
                            Thread.sleep(rand.nextInt(400-0) );
                        } catch (InterruptedException ex) {
                           ex.printStackTrace();
                           continue;
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    count++;
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void messageHandler(byte[] packetData,InetAddress address) {
        ByteArrayInputStream msg= new ByteArrayInputStream(packetData);

        Scanner scanner = new Scanner(msg);
        scanner.useDelimiter("\\Z");//To read all scanner content in one String

        String data = "";

        if (scanner.hasNext()) {
            data = scanner.next();
        }

        if(packetData[packetData.length-2]!=(char) 0x0D){
            System.out.println("Error in CR Confirmation");
            return;
        }

        if(packetData[packetData.length-1]!=(char) 0x0A){
            System.out.println("Error in LF Confirmation");
            return;
        }

        String header[] = (data.trim()).split(" ");
        String protocol = header[0];
        String versionID = header[1];
        String senderID = header[2];
        String fileID;

        String chunkNo;

        switch(protocol){
            case "PUTCHUNK":{
                /*crlf e body */
                if (header.length < 6){
                    System.out.println("Invalid input");
                    System.out.println(" PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF> <Body>");
                    break;
                }

                fileID = header[3];
                chunkNo = header[4];
                String replicationDeg = header[5];
                String controllers = header[6];
                String body = header[7];

                int chunkNo_Num = Integer.parseInt(chunkNo);
                int replicationDeg_Num = Integer.parseInt(replicationDeg);
                byte[] body_byte = body.getBytes();

                Chunk chunk = new Chunk(fileID,chunkNo_Num,replicationDeg_Num,body_byte);

                Message message_put = new Message("PUTCHUNK",versionID,senderID,fileID,chunkNo,replicationDeg_Num,body_byte);
                System.out.println(message_put.toString());

                if(this.chunkDB.storeChunk(this,protocol,fileID,chunkNo,replicationDeg,message_put.getBody())){
                    Message stored = new Message("STORED", versionID, senderID, fileID, chunkNo);
                    ArrayList<Chunk> oldChunks=this.chunkDB.getPeerChunks(this.peerID);
                    oldChunks.add(chunk);
                    this.chunkDB.addPeerHasChunks(this.peerID,oldChunks);
                    this.chunkDB.addnumberOfChunksOfFile(fileID,chunkNo_Num);
                    try {
                        backupCh.sendMessage(stored.getMsg());
                    } catch (IOException e) {
                        System.out.println("failed sending ACK STORED response to peer");
                        e.printStackTrace();
                    }
                }
                break;
            }
            case "DELETE": {
                if (header.length < 4) {
                    System.out.println("Invalid input");
                    System.out.println(" DELETE <Version> <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>");
                    break;
                }
                fileID = header[3];

                ArrayList<String> deleted_files = getChunkDB().deleteFile(fileID);

                if (deleted_files != null) {
                    for (int i = 0; i < deleted_files.size(); i++) {
                        String cNo = deleted_files.get(i);
                        Message message_delete = new Message("DELETE", versionID, senderID, fileID, cNo);
                        System.out.println(message_delete.toString());
                        byte[] b_message_delete = message_delete.getMsg();
                        try {
                            controlCh.sendMessage(b_message_delete);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                break;
            }
            case "STORED":{

                fileID = header[3];
                chunkNo = header[4];
                System.out.println(this.peerID + " has stored chunk no " + chunkNo+ " of file "+ fileID +"\n");
                break;
            }

            case "GETCHUNK":{
                fileID = header[3];
                chunkNo = header[4];
                break;
            }
            case "CHUNK":{
                fileID = header[3];
                chunkNo = header[4];
                break;
            }
            default:
                System.out.println("Invalid option");
                break;
        }


    }

    public void requestFileDeletion(String fileId){

        Runnable runnable = () -> {
            String peerName = Integer.toString(this.peerID);
            //String chunkNumb = Integer.toString(chunkNo);
            Message message = new Message("DELETE", "RMI", peerName, fileId);
            byte[] finalMsg = message.getMsg();
            while(true){
                try {
                    controlCh.sendMessage(finalMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

}
