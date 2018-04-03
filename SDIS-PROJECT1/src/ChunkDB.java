import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ChunkDB {
    private ConcurrentHashMap<Integer,Chunk> peerHasChunk;

    private ConcurrentHashMap<Integer,ArrayList<Chunk>> peerHasChunks;

    private ConcurrentHashMap<String,Integer> numberOfChunksOfFile;

    private ConcurrentHashMap<Integer, Chunk> peerHasRemove;

    public int count = 0;

    public ChunkDB(){
        this.peerHasChunk= new ConcurrentHashMap<>();
        this.peerHasChunks = new ConcurrentHashMap<>();
        this.numberOfChunksOfFile = new ConcurrentHashMap<>();
    }
    public void addnumberOfChunksOfFile(String fileID,int num){
        numberOfChunksOfFile.put(fileID,num);
    }
    public void addPeerHasChunk(int peerID,Chunk chunk){
       peerHasChunk.put(peerID,chunk);
    }
    public void addPeerHasChunks(int peerID,ArrayList<Chunk> chunks){peerHasChunks.put(peerID,chunks);}
    public ArrayList<Chunk> getPeerChunks(int peerId){return peerHasChunks.get(peerId);}

    public ConcurrentHashMap<Integer,Chunk> getPeerHasChunk(){
        return peerHasChunk;
    }

    public ConcurrentHashMap<Integer,ArrayList<Chunk>> getPeerHasChunks(){
        return peerHasChunks;
    }

    public ConcurrentHashMap<String,Integer> getNumberOfChunksOfFile(){
        return numberOfChunksOfFile;
    }


    public boolean searchFileIDExists(String fileID){
        int size_hash = peerHasChunks.size();

        for (int i = 0; i < size_hash; i++){
            ArrayList<Chunk> aux = peerHasChunks.get(i);

            int size_aux = aux.size();

            for (int j = 0; j < size_aux; j++){
                if (aux.get(j).getID() == fileID){
                    return true;
                }
            }
        }

        return false;
    }

    public ArrayList<Integer> searchFileIDPeer(String fileID){
        ArrayList<Integer> pos = new ArrayList<>(2);

        int size_hash = peerHasChunks.size();

        for (int i = 0; i < size_hash; i++){
            ArrayList<Chunk> aux = peerHasChunks.get(i);

            int size_aux = aux.size();

            for (int j = 0; j < size_aux; j++){
                if (aux.get(j).getID() == fileID){
                    pos.add(i);
                    pos.add(j);
                }
            }
        }

        count++;

        return pos;
    }

    private ArrayList<Chunk> makeNewArrayChunk(int peer, int pos_chunk){
        ArrayList<Chunk> chunks = new ArrayList<>();

        int size_peer = peerHasChunks.get(peer).size();

        for (int i = 0; i < size_peer; i++){
            if(i != pos_chunk){
                Chunk c = peerHasChunks.get(peer).get(i);
                chunks.add(c);
            }
        }

        return chunks;
    }

    public void removeFile(String fileID){
        ArrayList<Integer> pos = searchFileIDPeer(fileID);

        int peer = pos.get(0);
        int pos_chunk = pos.get(1);
        Chunk chunk = peerHasChunks.get(peer).get(pos_chunk);

        peerHasRemove.put(peer,chunk);

        peerHasChunks.get(peer).remove(pos_chunk);
    }

    public ArrayList<Chunk> search_chunks_of_file(String fileID){
        ArrayList<Chunk> chunks_of_file = new ArrayList<>();
        int size_hash = peerHasChunks.size();

        for (int i = 0; i < size_hash; i++){
            ArrayList<Chunk> aux = peerHasChunks.get(i);

            int size_aux = aux.size();

            for (int j = 0; j < size_aux; j++){
                if (aux.get(j).getID() == fileID){
                    chunks_of_file.add(aux.get(j));
                }
            }
        }

        return chunks_of_file;
    }

    public ArrayList<String> deleteFile(String fileID) {
        ArrayList<Chunk> chunks_of_file = search_chunks_of_file(fileID);

        ArrayList<String> deleted = new ArrayList<>();

        if (chunks_of_file == null) {
            System.out.println("Don't exist any file with id" + fileID);
        }else {

        }

        return deleted;
    }
}
