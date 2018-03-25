import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

class Client {

    private static RMI initiatorPeer;

    public static void main(String args[]) throws IOException, NotBoundException {

        if (args.length == 0) {
            System.out.println(args.length);
            throw new IllegalArgumentException("\nUsage: java ClientInterface <peer_ap>" +
                    " <sub_protocol> <opnd1> <opnd2> ");
        }

        String peerAp = args[0];
        String operation = args[1];

        String filepath;


        try {
            switch (operation.toUpperCase()) {
                case "BACKUP":
                    filepath = args[2];
                   int replicationDegree = Integer.parseInt(args[3]);
                    //initiatorPeer.backup(filepath, replicationDegree);
                    break;
                case "RESTORE":
                    filepath = args[2];
                    //initiatorPeer.restore(filepath);
                    break;
                case "DELETE":
                    filepath = args[2];
                    //initiatorPeer.delete(filepath);
                    break;
                case "RECLAIM":
                    int maxDiskSpace = Integer.parseInt(args[2]);
                    initiatorPeer.reclaim(maxDiskSpace);
                    break;
                case "STATE":
                    break;
                default:
                    break;
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

    }
}