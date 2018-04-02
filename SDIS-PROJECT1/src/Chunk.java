import java.security.MessageDigest;
public class Chunk {

    public static final int MAX_SIZE = 64000;

    private String chunkID;

    private int replicationDegree;

    private byte[] data;

    public Chunk(String fileID, int chunkNo, int replicationDegree, byte[] data) {

        this.replicationDegree = replicationDegree;

        this.data = data;

    }

    public String getID() {
        return chunkID;
    }

    public int getReplicationDegree() {
        return replicationDegree;
    }

    public byte[] getData() {
        return data;
    }

    public static String getSha256(String value) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(value.getBytes());
            return bytesToHex(md.digest());
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        return result.toString();
    }

}