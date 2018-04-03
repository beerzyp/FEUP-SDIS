import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class PeerRmi extends UnicastRemoteObject implements RMI {
    private final PeerInfo peer;

    private ChunkDB chunkDB;

    protected PeerRmi(PeerInfo peer) throws RemoteException {
        this.peer = peer;
        chunkDB = new ChunkDB();
    }

    @Override
    public void backup(String filepath, int replicationDegree) throws IOException {
        if (filepath == null) {
            throw new IllegalArgumentException("filepath does not exist");
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
            newChunk = new Chunk("null",0,replicationDegree,chunk);
            fileId = newChunk.getSha256(filepath);
            newChunk.setID(fileId);
            newChunk.setReplicationDegree(replicationDegree);
            newChunk.setChunkNumb(chunkNo);
            if (readableBytes > newChunk.MAX_SIZE)
                chunk = new byte[newChunk.MAX_SIZE];
            else
                chunk = new byte[readableBytes];

            file.read(chunk);
            System.out.println("reading chunk no: " + chunkNo);
            peer.requestChunkBackup(fileId, chunkNo, replicationDegree, chunk);
            chunkNo++;
        }

        newChunk = new Chunk("null",0,replicationDegree,chunk);
        newChunk.setID(fileId);
        newChunk.setReplicationDegree(replicationDegree);
        newChunk.setChunkNumb(chunkNo);
        if (readableBytes == newChunk.MAX_SIZE) {
            chunk = new byte[0];
            peer.requestChunkBackup(fileId, chunkNo, replicationDegree, chunk);
            chunkNo++;
        }

        this.peer.chunksFileID.add(fileId);
        this.peer.chunkFilePaths.add(filepath);
        ArrayList<Chunk> oldChunks=this.chunkDB.getPeerChunks(this.peer.peerID);
        oldChunks.add(newChunk);
        this.chunkDB.addPeerHasChunks(this.peer.peerID,oldChunks);
        this.chunkDB.addnumberOfChunksOfFile(fileId,chunkNo);


    }

    @Override
    public void restore(String pathname) throws IOException {

    }

    @Override
    public void delete(String pathname) throws IOException {
        if(pathname == null){
            throw new IllegalArgumentException("Invalid arguments for delete");
        }

        String filepath = peer.getPath_base() + pathname;

        int chunkNo = 0;
        int readableBytes = -1;
        byte[] chunk = new byte[64000];
        Chunk newChunk = new Chunk("null",0,1,chunk);
        String fileId = newChunk.getSha256(filepath);

        boolean fileExist = chunkDB.searchFileIDExists(fileId);

        if (fileExist == false){
            throw new IllegalArgumentException("File don't exist");
        }

        peer.requestFileDeletion(filepath);
    }

    @Override
    public void reclaim(int maxDiskSpace) throws RemoteException {

    }
}
