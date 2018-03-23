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

    public PeerChannel(InetAddress mcAddr, int mcastPort,Peer newPeer) throws IOException{
    this.mcAddr=mcAddr;
    this.mcPort=mcastPort;
    peerName="Peer"+Integer.toString(newPeer.peerID);
    newPeer.incrementPeerId();
        try{
            //For any Peer create a multi-cast socket
            mcSocket = new MulticastSocket(mcastPort);
            //Join the multi-cast socket
            System.out.println(peerName);
            mcSocket.joinGroup(this.mcAddr);

        }
        catch (SocketException e){System.out.println("socket creation error");}


    }
    int mcSocketJoin() throws IOException{
        try {
            this.mcSocket.joinGroup(this.mcAddr);
            return 0;
        } catch (SocketException e) {
            System.out.println("socket error Joining Group");
        }
        return 1;
    }
    int sendMessage() throws IOException{
        byte[] s_buffer = new byte[1024];
        s_buffer[0]='O';
        s_buffer[1]='L';
        s_buffer[2]='A';
        DatagramPacket sendPacket = new DatagramPacket(s_buffer, s_buffer.length, this.mcAddr, this.mcPort);
        this.mcSocket.send(sendPacket);
        return 0;
    }

    int recieveMessage() throws IOException{
        byte[] r_buffer = new byte[1024];
        DatagramPacket receivePacket = new DatagramPacket(r_buffer, r_buffer.length);
        this.mcSocket.receive(receivePacket);
        System.out.println(r_buffer[0]);
        System.out.println(r_buffer[1]);
        System.out.println(r_buffer[2]);
        return 0;
    }
}
