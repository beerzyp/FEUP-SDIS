import java.io.ByteArrayOutputStream;

/**
 * Message builder class.
 */
public class Message {
    public static final byte CR = 0xD;


    public static final byte LF = 0xA;

    private static final String CRLF = "" + (char) CR + (char) LF;
    private byte[] finalMsg;

    public Message(String msgType,String versionId,String senderID,String fileID,String chunkNo,int repDeg){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String join=String.join(msgType,versionId,senderID,fileID,chunkNo,Integer.toString(repDeg));
        byte[] body=join.getBytes();
        byteArrayOutputStream.write(body,0,body.length);
        byte[] header = CRLF.getBytes();
        byteArrayOutputStream.write(header,body.length, header.length);
        this.finalMsg=byteArrayOutputStream.toByteArray();
    }

    public byte[] getMsg(){
        return this.finalMsg;
    }
}
