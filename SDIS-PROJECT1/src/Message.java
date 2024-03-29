import java.io.ByteArrayOutputStream;

/**
 * Message builder class.
 */
public class Message {
    public static final byte CR = 0xD;
    private static String msgType;
    public static final byte LF = 0xA;

    private static final String CRLF = "" + (char) CR + (char) LF;
    private byte[] finalMsg;
    private byte[] body;

    public Message(String msgType, String versionId, String senderID, String fileID, String chunkNo, int repDeg,byte[] body){
        this.msgType=msgType;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String join = String.join(" ",msgType, versionId, senderID, fileID, chunkNo, Integer.toString(repDeg),CRLF);
        byte[] msg=join.getBytes();
        this.body=body;
        System.arraycopy(body,0,msg,msg.length,body.length);

        this.finalMsg = msg;
    }

    /**
     * Message for DELETE operation
     * @param msgType type of message
     * @param versionId version
     * @param senderID server
     * @param fileID file identifier
     */
    public Message(String msgType, String versionId, String senderID, String fileID){
        this.msgType=msgType;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        String join = String.join(" ",msgType, versionId, senderID, fileID, CRLF);

        this.finalMsg = join.getBytes();
    }

    public Message(String msgType, String versionId, String senderID, String fileID, String chunkNo){
        this.msgType=msgType;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        String join = String.join(" ",msgType, versionId, senderID, fileID, chunkNo, CRLF);

        this.finalMsg = join.getBytes();
    }
    public byte[] getBody(){return this.body;}

    public byte[] getMsg(){
        return this.finalMsg;
    }
}
