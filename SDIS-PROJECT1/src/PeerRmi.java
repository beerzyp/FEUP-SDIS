import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;

import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PeerRmi extends UnicastRemoteObject implements RMI {
    private final PeerInfo peer;

    protected PeerRmi(PeerInfo peer) throws RemoteException {
        this.peer = peer;
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
        byte[] chunk = new byte[64000];
        Chunk newChunk = new Chunk("null",0,replicationDegree,chunk);
        String fileId = newChunk.getSha256(filepath);

        while (file.available() > 0) {
            readableBytes = file.available();

            if (readableBytes > 64000)
                chunk = new byte[64000];
            else
                chunk = new byte[readableBytes];

            file.read(chunk);
            System.out.println("reading chunk no: " + chunkNo);
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
        if(pathname == null){
            throw new IllegalArgumentException("Invalid arguments for delete");
        }

        //String filepath = peer.ge
        //String fileId = this.util.getSha256(filepath);
    }

    @Override
    public void reclaim(int maxDiskSpace) throws RemoteException {

    }
}
