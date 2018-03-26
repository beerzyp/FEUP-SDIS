import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class Client {
    private static RMI initiatorPeer;

    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            System.out.println("ERROR - Invalid arguments");
            System.out.println("java Client <peear_app> <sub_protocol> <opnd_1> <opnd_2>");
            return;
        }
        //STATE
        String peer_ap=args[0];
        String sub_protocol= args[1];
        try {
            switch (sub_protocol) {
                case "BACKUP":
                    String file=args[2];
                    int replicationDeg=Integer.parseInt(args[3]);
                    System.out.println("backup op");
                    initiatorPeer.backup(file,replicationDeg);
                    break;
                case "RESTORE":
                    System.out.println("backup op");
                    break;
                case "DELETE":
                    System.out.println("backup op");
                    break;
                case "RECLAIM":
                    System.out.println("backup op");
                    break;
                case "STATE":
                    System.out.println("backup op");
                    break;
            }
        }
        catch(IllegalArgumentException e){e.printStackTrace();}


    }
}
