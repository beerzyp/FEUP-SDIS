import java.io.ByteArrayOutputStream;

/**
 * Message builder class.
 */
public class Message {
    public static final byte CR = 0xD;

    public static final byte LF = 0xA;

    private static final String CRLF = "" + (char) CR + (char) LF;
    private byte[] finalMsg;

    public Message(String msgType, String versionId, String senderID, String fileID, String chunkNo, int repDeg){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String join = String.join(msgType, versionId, senderID, fileID, chunkNo, Integer.toString(repDeg),CRLF);
        this.finalMsg = join.getBytes();
    }

    /**
     * Message for DELETE operation
     * @param msgType type of message
     * @param versionId version
     * @param senderID server
     * @param fileID file identifier
     */
    public Message(String msgType, String versionId, String senderID, String fileID){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        String join = String.join("",msgType, versionId, senderID, fileID, CRLF);

        this.finalMsg = join.getBytes();
    }

    public byte[] getMsg(){
        return this.finalMsg;
    }
}
