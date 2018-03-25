import java.io.IOException;
import java.net.InetAddress;

public class PeerInfo {
    public PeerChannel initiatorPeer;
    private int peerID;
    private InetAddress hostname;
    private int port;

    public PeerInfo(InetAddress address,int mcastPort) throws IOException{
        this.port=mcastPort;
        this.hostname=address;
        initiatorPeer = new PeerChannel(this.hostname,this.port,this);
    }


    public InetAddress getHost() {
        return this.hostname;
    }


    public int getId() {
        return peerID;
    }


    public int getPort() {
        return port;
    }


    public void setHost(InetAddress host) {
        this.hostname = host;
    }


    public void setId(int id) {
        this.peerID = id;
    }


    public void setPort(int port) {
        this.port = port;
    }




}
