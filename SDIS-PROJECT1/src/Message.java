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
        String join = String.join(msgType, versionId, senderID, fileID, chunkNo, Integer.toString(repDeg));
        byte[] body = join.getBytes();
        byte[] header = CRLF.getBytes();
        byte[] c = new byte[body.length + header.length];
        System.arraycopy(body, 0, c, 0, body.length);
        System.arraycopy(header, 0, c, body.length, header.length);
        byteArrayOutputStream.write(c, 0, c.length);


        this.finalMsg = byteArrayOutputStream.toByteArray();
    }

    /**
     *
     * @param msgType
     * @param versionId
     * @param senderID
     * @param fileID
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
