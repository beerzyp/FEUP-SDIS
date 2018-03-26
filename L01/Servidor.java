import java.net.*;
import java.io.IOException;
import java.lang.String;
import java.util.Hashtable;

/*
Compilar: javac Servidor.java
Correr: java Filename
 */

public class Servidor extends Throwable{
    private static Hashtable<String, String> data_base;
    public static int port = 4445; // server port

    public static void main(String[] args) throws IOException{
        if (args.length != 2) {
            System.out.println("Usage: java Echo <hostname> <string to echo>");
            return;
        }
        data_base= new Hashtable<String,String>();

        DatagramSocket socket = new DatagramSocket(port);//4445
        try {
            getResponse(socket);
        }catch(IOException e){
            System.err.println("Socket error");
            System.exit(1);
        }

    }

    // send request
    public static void sendRequest(DatagramSocket socket, String ans) throws IOException{
        // System.out.println("xbxbxbxb 2");

    }

    // display response
    public static void displayResponse(DatagramPacket packet, DatagramSocket socket){
        String received = new String(packet.getData());
        System.out.println("Echoed Message: " + received);
        socket.close();
    }
    // get response
    public static void getResponse(DatagramSocket socket) throws IOException{
        while (true) {
            byte[] rbuf = new byte[256];
            DatagramPacket packet = new DatagramPacket(rbuf, rbuf.length);
            socket.receive(packet);

            //String ans = new String(packet.getData(),0,packet.getLength());
            int i=0;

            //handle Request
            InetAddress address = packet.getAddress();
            int recievePort = packet.getPort();
            String ans = new String(packet.getData(),0,packet.getData().length);
            String response=handleRequest(ans);
            System.out.println(response);
            byte[] sendBuffer= new byte[512];
            //send response
            sendBuffer=response.getBytes();

            DatagramPacket sendPacket= new DatagramPacket(sendBuffer,sendBuffer.length,address,recievePort);
            socket.send(packet);

        }
    }

    private static String handleRequest(String request) {
        String[] array = request.split(" ");

        String request_type = array[0];
        String plate = array[1];

        if(request_type.equals("REGISTER")) {
            if(!data_base.containsKey(plate)) {
                String name = "";

                for(int i =2; i < array.length; i++)
                    name += array[i] + " ";

                name = name.substring(0, name.length() - 1);

                data_base.put(plate, name);
                return Integer.toString(data_base.size());

            }else return "-1";

        }else if(request_type.equals("LOOKUP")) {

            System.out.println("database size: " + data_base.get(plate));
            if(data_base.contains(plate)) {
                return data_base.get(plate);
            }else return "NOT_FOUND";
        }

        return "-1";
    }
}