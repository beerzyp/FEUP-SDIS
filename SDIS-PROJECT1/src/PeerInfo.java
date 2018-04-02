import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

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
    public ArrayList<Chunk> chunks;
    public static int peerID = 0;

    private String path_base = "./bin/";

    public PeerInfo(String serverId, String protocolVersion, String serviceAccessPoint, InetAddress mcAddr, int mcPort,
                    InetAddress mdbAddr, int mdbPort, InetAddress mdrAddr, int mdrPort) throws IOException{
        System.out.println("Peer " + this.peerID + " is connecting to network");
        controlCh = new PeerConnection(mcAddr, mcPort, this);
        backupCh= new PeerConnection(mdbAddr, mdbPort, this);
        restoreCh = new PeerConnection(mdrAddr, mdrPort, this);

        //each connection has 1 thread listener
        PeerRmi initPeer = new PeerRmi(this);
        //initPeer.backup("./bin/Peer0/my_files/Arkanoid-Logo-New.bmp",3);
        //PeerRmi initiatorPeer = new PeerRmi(this);
        String pathname="./bin/"+"Peer"+Integer.toString(this.peerID)+"/"+"my_files";
        File file = new File(pathname);
        file.mkdirs();
        String pathname1="./bin/"+"Peer"+Integer.toString(this.peerID)+"/"+"my_chunks";
        File file1 = new File(pathname1);
        file1.mkdirs();
        this.incrementPeerId();
    }

    public void incrementPeerId(){
        this.peerID++;
    }

    public String getPath_base(){
        return path_base;
    }

    public void requestChunkBackup(String fileId, int chunkNo, int repDeg, byte[] currChunk){
        Runnable runnable = () -> {
            String peerName = Integer.toString(this.peerID);
            String chunkNumb = Integer.toString(chunkNo);
            Message message = new Message("PUTCHUNK", "RMI", peerName, fileId, chunkNumb, repDeg);
            byte[] finalMsg = message.getMsg();
            while(true){
                try {
                    backupCh.sendMessage(finalMsg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public void messageHandler(DatagramPacket packet) {

    }

    public void requestFileDeletion(String fileId){
        
    }
}
