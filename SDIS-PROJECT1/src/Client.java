import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {

    private static RMI initiatorPeer;

    public static void main(String[] args) throws IOException {

        if (args.length < 2) {
            System.out.println("ERROR - Invalid arguments");
            System.out.println("java Client <peear_app> <sub_protocol> <opnd_1> <opnd_2>");
            return;
        }

        //STATE
        String peer_ap = args[0];
        String sub_protocol = args[1];


        try {
            // create a remote object registry that accepts calls on a specific port.
            Registry registry = LocateRegistry.getRegistry();
            //Returns the remote reference bound to the specified name in this registry.
            initiatorPeer = (RMI) registry.lookup(peer_ap);
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        String file;

        try {
            switch (sub_protocol.toUpperCase()) {
                case "BACKUP":
                    file=args[2];
                    int replicationDeg=Integer.parseInt(args[3]);
                    System.out.println("BACKUP op");
                    initiatorPeer.backup(file,replicationDeg);
                    break;
                case "RESTORE":
                    file=args[2];
                    System.out.println("RESTORE op");
                    initiatorPeer.restore(file);
                    break;
                case "DELETE":
                    file=args[2];
                    System.out.println("DELETE op");
                    initiatorPeer.delete(file);
                    break;
                case "RECLAIM":
                    System.out.println("RECLAIM op");
                    initiatorPeer.reclaim(600000000);
                    break;
                case "STATE":
                    System.out.println("STATE op");
                    break;
            }
        } catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
