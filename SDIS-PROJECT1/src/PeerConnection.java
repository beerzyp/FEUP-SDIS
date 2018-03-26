import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class PeerConnection {
    private final InetAddress mcAddr;
    private final int mcPort;
    private MulticastSocket mcSocket;
    private String peerName;
    private Controller controller;

    public PeerConnection(InetAddress mcAddr, int mcastPort, PeerInfo newPeerInfo) throws IOException{
    this.mcAddr=InetAddress.getByName("228.5.6.7"); //localhost is not in range in multicasting TODO: address comes from cmd
    this.mcPort=mcastPort;
    peerName="PeerInfo"+Integer.toString(newPeerInfo.peerID);
    newPeerInfo.incrementPeerId();

        try{
            mcSocket = new MulticastSocket(mcastPort);
            //Join the multi-cast socket
            mcSocket.joinGroup(this.mcAddr);
            System.out.println(peerName);

        }
        catch (SocketException e){System.out.println("socket creation error");e.printStackTrace();}


    }

    public void setController(Controller ctrl){
        this.controller=ctrl;
        //All subprotocols use a multicast channel, the control channel (MC), that is used for control messages.
        // All peers must subscribe the MC channel.
    }

    public boolean sendMessage(byte[] newMSG) throws IOException{
        DatagramPacket sendPacket = new DatagramPacket(newMSG, newMSG.length, this.mcAddr, this.mcPort);
        try{
            this.mcSocket.send(sendPacket);
        }
        catch(IOException e){e.printStackTrace();return false;}
        System.out.println("\nmessage sent");
        return true;
    }

    public void recieveMessage() throws IOException{
        byte[] r_buffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(r_buffer, r_buffer.length);
        Runnable runnable = () -> {
            while(true){
                try {
                    this.mcSocket.receive(receivePacket);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
