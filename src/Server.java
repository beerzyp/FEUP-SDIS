import java.net.*;
import java.io.*;
import java.io.IOException;
import java.lang.System.*;

/*
Compilar: javac Server.java
Correr: java Filename
 */

public class Server extends Throwable{

    public static int port = 4445; // server port

    public static void main(String[] args) throws IOException{
        if (args.length != 2) {
            System.out.println("Usage: java Echo <hostname> <string to echo>");
            return;
        }

        DatagramSocket socket = new DatagramSocket(port);//4445

        getResponse(socket);
    }

    // send request
    public static void sendRequest(DatagramSocket socket, String ans) throws IOException{
       // System.out.println("xbxbxbxb 2");
        byte[] sbuf = ans.getBytes();
        InetAddress address = InetAddress.getLocalHost();
        DatagramPacket packet = new DatagramPacket(sbuf, sbuf.length,address, port); //4445
        socket.send(packet);
    }

    // display response
    public static void displayResponse(DatagramPacket packet, DatagramSocket socket){
        String received = new String(packet.getData());
        System.out.println("Echoed Message: " + received);
        socket.close();
    }
    // get response
    public static void getResponse(DatagramSocket socket) throws IOException{
        InetAddress address = InetAddress.getLocalHost();

        while (true) {
            byte[] rbuf = new byte[256];
            //byte[] rbuf = {'1','2'};
            DatagramPacket packet = new DatagramPacket(rbuf, rbuf.length);

            try {
                //System.out.println("xbxbxbxb");
                socket.receive(packet);

                //String ans = new String(packet.getData(),0,packet.getLength());
                System.out.println(packet.getData().length);
                System.out.println(packet.getData()[0]);
               // sendRequest(socket,ans);
            }catch (IOException e){
                System.err.println("Socket error");
                throw new IOException("Socket error");
            }

        }
    }
}