import java.io.IOException;
import java.net.InetAddress;

public class PeerService {
    public static int peerID = 0;
    public static void main(String[] args) throws IOException {
        if(args.length!=2) {System.out.println("Usage <");}
        InetAddress multicast_address = InetAddress.getByName(args[0]);
        int multicast_port = Integer.parseInt(args[1]);

        PeerInfo peerClient = new PeerInfo(multicast_address,multicast_port);

    }

    private void incrementPeerCount(){
        this.peerID++;
    }
}
