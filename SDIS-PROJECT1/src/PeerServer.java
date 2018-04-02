import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

class PeerServer {
    public static void main(String[] args) throws IOException {
        System.setProperty("java.net.preferIPv4Stack", "true");

        if (args.length != 9) {
            System.out.println(args.length);
            throw new IllegalArgumentException("\nUsage: java PeerServer <protocolVersion> <serverId>  <accessPoint>" +
                    " <mcAddr> <mcPort> <mdbAddr> <mdbPort> <mdrAddr> <mdrPort>");
        }
        /*
        String protocolVersion = args[0];
        String serverId = args[1];
        String serviceAccessPoint = args[2];

        InetAddress mcAddr = InetAddress.getByName(args[3]);
        int mcPort = Integer.parseInt(args[4]);

        InetAddress mdbAddr = InetAddress.getByName(args[5]);
        int mdbPort = Integer.parseInt(args[6]);

        InetAddress mdrAddr = InetAddress.getByName(args[7]);
        int mdrPort = Integer.parseInt(args[8]);

        PeerInfo peerService = new PeerInfo(serverId, protocolVersion, serviceAccessPoint, mcAddr, mcPort,
                mdbAddr, mdbPort, mdrAddr, mdrPort);*/
        /*
        PeerLauncher 1.0 1 peer1 224.0.0.0 4445 224.0.0.1  4446 224.0.0.2 2000
         */

        PeerInfo peerinfo1= new PeerInfo("0", "1", "1",InetAddress.getByName("224.0.0.0"),4445 ,
                InetAddress.getByName("224.0.0.1"), 4446,InetAddress.getByName("224.0.0.2"),4447);
        PeerInfo peerinfo2= new PeerInfo("0", "1", "1",InetAddress.getByName("224.0.0.0"),4445 ,
                InetAddress.getByName("224.0.0.1"), 4446,InetAddress.getByName("224.0.0.2"),4447);
        PeerInfo peerinfo3= new PeerInfo("0", "1", "1",InetAddress.getByName("224.0.0.0"),4445 ,
                InetAddress.getByName("224.0.0.1"), 4446,InetAddress.getByName("224.0.0.2"),4447);
       // byte[] asd={'a','b','c'};
        /*
        Message message = new Message("PUTCHUNK", "RMI", "1", "src", "2", 3);
        byte[] finalMsg = message.getMsg();
        String a1= new String(finalMsg,"UTF-8");
        System.out.println(a1);
        */
       //Message message = new Message("PUTCHUNK", "RMI", "1", "src", "2", 3);
       // peerinfo1.backupCh.sendMessage(finalMsg);
        //peerinfo2.backupCh.recieveMessage();
        //DatagramPacket pckt =peerinfo1.backupCh.getRecievedPacket();
        //System.out.println("receiving packet with length : "+ pckt.getData().length);

        //message for delete
        Message message = new Message("DELETE", "RMI", "1", "src");
        byte[] finalMsg = message.getMsg();



        String m1 = new String(finalMsg,"UTF-8");
        File file = new File("./bin");
        file.mkdirs();
        byte[] currChunk= new byte[64000];
        //peerinfo1.requestChunkBackup("bin/Peer0/my_files/Arkanoid-Logo-New.bmp",0,3,currChunk);

    }
}