import java.net.*;
import java.io.IOException;
import java.lang.String;
import java.util.Hashtable;

/*
Compilar: javac Server.java
Correr: java Filename
 */

public class Server extends Throwable{
    private static Hashtable<String, String> data_base;

    // server port
    /*
    public static int port = 4445;
     */

    public static void main(String[] args) throws IOException{
        if ((args.length >= 4) && (args.length<3)) {
            /*
            System.out.println("Usage: java Echo <hostname> <string to echo>");
             */
            System.out.println("java Server <srvc_port> <mcast_addr> <mcast_port>");
            return;
        }

        //<srvc_port> is the port number where the server provides the service
        String srvc_port = args[0];
        Integer server_port = Integer.parseInt(srvc_port);

        //<mcast_addr> is the IP address of the multicast group used by the server to advertise its service.
        String mcast_addr = args[1];
        Integer multicast_address = Integer.parseInt(mcast_addr);

        //<mcast_port> is the multicast group port number used by the server to advertise its service.
        String mcast_port = args[2];
        Integer multicast_port = Integer.parseInt(mcast_port);

        data_base= new Hashtable<String,String>();

        //server port 4445
        DatagramSocket socket = new DatagramSocket(server_port);
        MulticastSocket multicast_socket = new MulticastSocket(multicast_address);

        try {
            getResponse(socket);
        }catch(IOException e){
            System.err.println("Socket error");
            System.exit(1);
        }

    }

    // send request
    public static void sendRequest(DatagramSocket socket, String ans) throws IOException{

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

                for(int i =2; i < array.length; i++) {
                    name += array[i] + " ";
                }

                name = name.substring(0, name.length() - 1);

                data_base.put(plate, name);
                return Integer.toString(data_base.size());

            }else {
                return "-1";
            }
        }else if(request_type.equals("LOOKUP")) {
            System.out.println("database size: " + data_base.get(plate));

            if(data_base.contains(plate)) {
                return data_base.get(plate);
            }else return "NOT_FOUND";
        }

        return "-1";
    }
}