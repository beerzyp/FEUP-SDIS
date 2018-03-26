import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PeerRmi extends UnicastRemoteObject implements RMI {
    private final PeerInfo peer;
    protected PeerRmi(PeerInfo peer) throws RemoteException {
        this.peer=peer;
    }

    @Override
    public void backup(String pathname, int replicationDegree) throws IOException {

    }

    @Override
    public void restore(String pathname) throws IOException {

    }

    @Override
    public void delete(String pathname) throws IOException {

    }

    @Override
    public void reclaim(int maxDiskSpace) throws RemoteException {

    }
}
