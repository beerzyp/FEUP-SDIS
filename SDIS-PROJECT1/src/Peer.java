import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class Peer {
    public PeerChannel initiatorPeer;
    public PeerChannel connectingPeer;
    public MulticastSocket mcSocket;
    public static int peerID = 0;

    public static void main(String[] args) throws IOException{
        if(args.length!=2) {System.out.println("Usage <");}
        InetAddress multicast_address = InetAddress.getByName(args[0]);
        int multicast_port = Integer.parseInt(args[1]);
        Peer peerClient = new Peer(multicast_address,multicast_port);
        //peerClient.initiatorPeer.sendMessage();
        //peerClient.connectingPeer.recieveMessage();
    }

    public Peer(InetAddress address,int mcastPort) throws IOException{
        initiatorPeer = new PeerChannel(address,mcastPort,this);
        PeerChannel connectingPeer = new PeerChannel(address,mcastPort,this);
        //connectingPeer.mcSocketJoin();
    }

    public void incrementPeerId(){
        this.peerID++;
    }


}
