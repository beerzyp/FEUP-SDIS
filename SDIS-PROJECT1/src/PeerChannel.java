import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class PeerChannel {
    private final InetAddress mcAddr;
    private final int mcPort;
    private MulticastSocket mcSocket;
    private String peerName;
    private static ControllerInterface controllerInterface;
    private static PeerInfo peerInfo;

    public PeerChannel(InetAddress mcAddr, int mcastPort,PeerInfo newPeer) throws IOException{
    this.mcAddr=mcAddr;
    this.mcPort=mcastPort;
    this.peerInfo=newPeer;

        try{
            //For any Peer create a multi-cast socket
            mcSocket = new MulticastSocket(mcastPort);
            //Join the multi-cast socket
            mcSocket.joinGroup(this.mcAddr);

        }
        catch (SocketException e){System.out.println("socket creation error");}


    }

    void setMcController(ControllerInterface controllerInterface){
        this.controllerInterface = controllerInterface;
    }
    boolean sendMessage(byte[] s_buffer) throws IOException{
        DatagramPacket sendPacket = new DatagramPacket(s_buffer, s_buffer.length, this.mcAddr, this.mcPort);
        try{
            this.mcSocket.send(sendPacket);
        }
        catch(IOException e ){e.printStackTrace();return false;}

        return true;
    }

    void recieveMessage() throws IOException{
        byte[] r_buffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(r_buffer, r_buffer.length);
        Runnable task = () -> {
            while(true) {
                try {
                    this.mcSocket.receive(receivePacket);
                    //calls message handler
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        task.run();

        Thread thread = new Thread(task);
        thread.start();
        System.out.println("recieved message!");
    }

   int close(){
    return 0;
    }
}
