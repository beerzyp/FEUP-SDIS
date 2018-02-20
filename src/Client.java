import java.net.*;
import java.io.*;
import java.io.IOException;
import java.lang.System.*;
import java.lang.Integer;

import Server;

//
public class Client extends Throwable {

    public static int port = 4445; // server port

    public static void main(String[] args) throws IOException {
        if(args.length != 4){
            System.err.println("Usage: java Client <host_name> <port_number> <oper> <opnd>");
            return;
        }

        /*
        int port;
        InetAddress address;
        DatagramSocket socket = null;
        DatagramPacket packet;
        byte[] sendBuf = new byte[256];
        */

        /*
        String hostname = args[0];
        System.out.println(hostname+"\n");

        int port = Integer.parseInt(args[1]);
        System.out.println(port+"\n");
        */

        DatagramSocket socket = new DatagramSocket();

        sendRequest(socket, "abs");
    }

    // send request
    public static void sendRequest(DatagramSocket socket, String ans) throws IOException{
        InetAddress address = InetAddress.getLocalHost();
        //correto
        //byte[] sbuf = ans.getBytes();
        //try
        byte[] rbuf = {'1','2'};
        DatagramPacket packet = new DatagramPacket(rbuf, rbuf.length,address, Server.port); //port
        socket.send(packet);
    }

    // display response
    public static void displayResponse(DatagramPacket packet, DatagramSocket socket){
        String received = new String(packet.getData());
        System.out.println("Echoed Message: " + received);
        socket.close();
    }
}