import java.io.IOException;
import java.net.DatagramPacket;
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
    public PeerConnection controlCh;
    public PeerConnection backupCh;
    public PeerConnection restoreCh;
    public static int peerID = 0;

    public PeerInfo(String serverId,String protocolVersion, String serviceAccessPoint, InetAddress mcAddr, int mcPort,
                    InetAddress mdbAddr, int mdbPort, InetAddress mdrAddr, int mdrPort) throws IOException{
        controlCh = new PeerConnection(mcAddr,mcPort,this);
        backupCh= new PeerConnection(mcAddr,mcPort,this);
        restoreCh = new PeerConnection(mcAddr,mcPort,this);
        //each connection has 1 thread listener

        PeerRmi initiatorPeer = new PeerRmi(this);
    }

    public void incrementPeerId(){
        this.peerID++;
    }


    public void messageHandler(DatagramPacket packet) {

    }
}
