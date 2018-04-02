import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkDB {
    private ConcurrentHashMap<Integer,Chunk> peerHasChunk;

    private ConcurrentHashMap<Integer,ArrayList<Chunk>> peerHasChunks;

    private ConcurrentHashMap<String,Integer> numberOfChunksOfFile;

    public ChunkDB(){
        this.peerHasChunk= new ConcurrentHashMap<>();
        this.peerHasChunks = new ConcurrentHashMap<>();
        this.numberOfChunksOfFile = new ConcurrentHashMap<>();
    }

    public void addPeerHasChunk(int peerID,Chunk chunk){
       peerHasChunk.put(peerID,chunk);
    }

    public ConcurrentHashMap<Integer,Chunk> getPeerHasChunk(){
        return peerHasChunk;
    }

    public ConcurrentHashMap<Integer,ArrayList<Chunk>> getPeerHasChunks(){
        return peerHasChunks;
    }

    public ConcurrentHashMap<String,Integer> getNumberOfChunksOfFile(){
        return numberOfChunksOfFile;
    }

    public boolean searchFileID(String fileID){
        boolean res = false;

        int size_hash = peerHasChunks.size();

        for (int i = 0; i < size_hash; i++){
            if(peerHasChunk.get(i).getID() == fileID){
                res = true;
                return res;
            }
        }

        return res;
    }
}
