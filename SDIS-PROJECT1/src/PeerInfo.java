import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

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
    public PeerConnection initiatorPeer;
    public PeerConnection connectingPeer;
    public MulticastSocket mcSocket;
    public static int peerID = 0;

    public static void main(String[] args) throws IOException{
        if(args.length!=2) {System.out.println("Usage <");return;}
        InetAddress multicast_address = InetAddress.getByName(args[0]);
        int multicast_port = Integer.parseInt(args[1]);

        PeerInfo peerInfoClient = new PeerInfo(multicast_address,multicast_port);
        byte[] msg={'O','L','A'};
        peerInfoClient.initiatorPeer.sendMessage(msg);
        peerInfoClient.connectingPeer.recieveMessage();
    }

    public PeerInfo(InetAddress address, int mcastPort) throws IOException{
        initiatorPeer = new PeerConnection(address,mcastPort,this);
        connectingPeer = new PeerConnection(address,mcastPort,this);
    }

    public void incrementPeerId(){
        this.peerID++;
    }


}
