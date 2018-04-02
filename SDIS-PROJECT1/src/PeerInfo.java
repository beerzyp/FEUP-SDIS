import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Scanner;

/*
http://cs.berry.edu/~nhamid/p2p/

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
    public ArrayList<Integer> chunks;
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
        //each connection has 1 thread listener
        //PeerRmi initPeer = new PeerRmi(this);
        //initPeer.backup("./bin/Peer0/my_files/Arkanoid-Logo-New.jpg",3);

       // PeerRmi initiatorPeer = new PeerRmi(this);
        //initiatorPeer.backup("./bin/Peer0/my_files/img1.jpg",3);
        myHomeFiles = new ArrayList<File>(0);
        readFilesFromPeer();
        //for(int i=0;i<this.myFiles.size();i++){
        //}
        String pathname="./bin/"+"Peer"+Integer.toString(this.peerID)+"/"+"my_files";
        File file = new File(pathname);
        file.mkdirs();
        String pathname1="./bin/"+"Peer"+Integer.toString(this.peerID)+"/"+"my_chunks";
        File file1 = new File(pathname1);
        file1.mkdirs();
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
                    try {
                        backupCh.sendMessage(finalMsg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

        /*
        Options for messages:
            generic | <MessageType> | <Version> | <SenderId> | <FileId> | <ChunkNo> | <ReplicationDeg> |     <CRLF>   | <Body>
           ---------|---------------|-----------|------------|----------|-----------|------------------|--------------|--------
             backup |    PUTCHUNK   | <Version> | <SenderId> | <FileId> | <ChunkNo> | <ReplicationDeg> | <CRLF><CRLF> | <Body>
           ---------|---------------|-----------|------------|----------|-----------|------------------|--------------|--------
                    |     STORED    | <Version> | <SenderId> | <FileId> | <ChunkNo> |                  | <CRLF><CRLF> |
           ---------|---------------|-----------|------------|----------|-----------|------------------|--------------|--------
            restore |    GETCHUNK   | <Version> | <SenderId> | <FileId> | <ChunkNo> |                  | <CRLF><CRLF> |
           ---------|---------------|-----------|------------|----------|-----------|------------------|--------------|--------
                    |     CHUNK     | <Version> | <SenderId> | <FileId> | <ChunkNo> |                  | <CRLF><CRLF> | <Body>
           ---------|---------------|-----------|------------|----------|-----------|------------------|--------------|--------
             delete |     DELETE    | <Version> | <SenderId> | <FileId> |           |                  | <CRLF><CRLF> |
           ---------|---------------|-----------|------------|----------|-----------|------------------|--------------|--------
            reclaim |    REMOVED    | <Version> | <SenderId> | <FileId> | <ChunkNo> |                  | <CRLF><CRLF> |
 */
        String header[] = (data.trim()).split(" ");
        String protocol = header[0];
        String versionID = header[1];
        String senderID = header[2];
        String fileID;

        String chunkNo;

        switch(protocol){
            case "PUTCHUNK":
                /*crlf e body */
                if (header.length < 6){
                    System.out.println("Invalid input");
                    System.out.println(" PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF> <Body>");
                }

                fileID = header[3];
                chunkNo = header[4];
                String replicationDeg = header[5];
                break;
            case "GETCHUNK":
                if (header.length < 5){
                    System.out.println("Invalid input");
                    System.out.println(" PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF> <Body>");
                }

                fileID = header[3];
                chunkNo = header[4];
                break;
            case "DELETE":
                if (header.length < 4){
                    System.out.println("Invalid input");
                    System.out.println(" PUTCHUNK <Version> <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF> <Body>");
                }
                fileID = header[3];

                getChunkDB().deleteFile(fileID);
                break;
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
