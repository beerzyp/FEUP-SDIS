import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PeerRmi extends UnicastRemoteObject implements RMI {
    Utilities util;
    private final PeerInfo peer;
    protected PeerRmi(PeerInfo peer) throws RemoteException {
        this.peer=peer;
        this.util = new Utilities();
    }

    @Override
    public void backup(String filepath, int replicationDegree) throws IOException {
        if (filepath == null || replicationDegree < 1) {
            throw new IllegalArgumentException("Invalid arguments for backup");
        }

        FileInputStream file;


        try {
            file = new FileInputStream(filepath);
        } catch (FileNotFoundException e) {
            System.out.format("path is wrong, or file not found", filepath);
            return;
        }

        System.out.println("New backup request for file " + filepath);
        int chunkNo = 0;
        int readableBytes = -1;
        byte[] chunk;

        String fileId =this.util.getSha256(filepath);

        while (file.available() > 0) {
            readableBytes = file.available();

            if (readableBytes > 64000)
                chunk = new byte[64000];
            else
                chunk = new byte[readableBytes];

            file.read(chunk);
            peer.requestChunkBackup(fileId, chunkNo, replicationDegree, chunk);
            chunkNo++;
        }


        if (readableBytes == 64000) {
            chunk = new byte[0];
            peer.requestChunkBackup(fileId, chunkNo, replicationDegree, chunk);
            chunkNo++;
        }

        /*
        peer.registerFile(fileId, replicationDegree, chunkNo, filepath);
        register file in database
         */

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
