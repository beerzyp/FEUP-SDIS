import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.Hashtable;

public class Node{
    private PeerInfo myInfo;
    private static int peerID;
    private int maxPeers;  // maximum size of peers list; 0 means unlimited
    private Hashtable<String,PeerInfo> peers;

    private Hashtable<String,ControllerInterface> handlers;

    private boolean shutdown;  // node is in shutdown mode?


    private class PeerHandler extends Thread{
        private MulticastSocket mcSocket;

        public PeerHandler(MulticastSocket socket) throws IOException {

        }

        public void run(){
            PeerChannel peerChannel = null;
            try {
                peerChannel = new PeerChannel(myInfo.getHost(),myInfo.getPort(),myInfo);

            } catch (IOException e) {
                e.printStackTrace();
            }
            peerChannel.close();
        }
    }


    public Node(int port) throws IOException {
        this(0, new PeerInfo(InetAddress.getByName("215.252.55"),port));
    }

    public Node(int maxPeers, PeerInfo info) throws UnknownHostException {

        if (info.getHost() == null)
            info.setHost(InetAddress.getByName("215.252.55"));
        if (info.getId() == -1)
            info.setId(peerID);

        this.myInfo = info;
        this.maxPeers = maxPeers;

        this.peers = new Hashtable<String,PeerInfo>();
        this.handlers = new Hashtable<String,ControllerInterface>();


        this.shutdown = false;
    }


    public void mainLoop() {
    }

}
