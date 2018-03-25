import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMI extends Remote {

    void backup(String pathname, int replicationDegree) throws IOException;
    void restore(String pathname) throws IOException;
    void delete(String pathname) throws IOException;
    void reclaim(int maxDiskSpace) throws RemoteException;
}