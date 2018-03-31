import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PeerConnection {

    private final InetAddress mcAddr;
    private final int mcPort;
    private MulticastSocket mcSocket;
    private DatagramPacket recievedPacket;

    //from peerInfo
    private final PeerInfo peerInfo;
    private String peerName;

    public PeerConnection(InetAddress mcAddr, int mcastPort, PeerInfo newPeerInfo) throws IOException{
    this.mcAddr=mcAddr; //localhost is not in range in multicasting TODO: address comes from cmd
    this.mcPort=mcastPort;
    this.peerInfo=newPeerInfo;

    peerName=Integer.toString(peerInfo.peerID);

        try{
            mcSocket = new MulticastSocket(mcastPort);
            //Join the multi-cast socket
            mcSocket.joinGroup(this.mcAddr);

        } catch (SocketException e){
            System.out.println("socket creation error");
            e.printStackTrace();
        }
    }


    public boolean sendMessage(byte[] newMSG) throws IOException{
        DatagramPacket sendPacket = new DatagramPacket(newMSG, newMSG.length, this.mcAddr, this.mcPort);

        try{
            this.mcSocket.send(sendPacket);
        } catch(IOException e){
            e.printStackTrace();
            return false;
        }

       // System.out.println("\nmessage sent");

        return true;
    }
    public DatagramPacket getRecievedPacket(){
        return this.recievedPacket;

    }
    public void recieveMessage() throws IOException{
        byte[] r_buffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(r_buffer, r_buffer.length);
        Runnable runnable = () -> {
            while(true) {
                try {
                    this.mcSocket.receive(receivePacket);
                    this.recievedPacket=receivePacket;
                    //System.out.println("\nmessage received " + ((String) Integer.toString(recievedPacket.getData()[recievedPacket.getData().length-1]))
                           // +Integer.toString(recievedPacket.getData()[recievedPacket.getData().length-2])+Integer.toString(recievedPacket.getData()[recievedPacket.getData().length-3]+Integer.toString(recievedPacket.getData()[recievedPacket.getData().length-4]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void peerMessageHandler(DatagramPacket packet){
        //peer id2 accepts connection and starts handler thread
        //An ExecutorService can be shut down, which will cause it to reject new tasks. . shutdown();
        Runnable runnable = () -> {
            this.peerInfo.messageHandler(packet);
        };
        ExecutorService service = Executors.newFixedThreadPool(10);

        service.execute(runnable);
    }
}
