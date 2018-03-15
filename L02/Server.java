import java.io.IOException;
import java.util.Hashtable;
import java.net.*;

/*
Compilar: javac Server.java
Correr: java Filename
 */

public class Server extends Throwable{

    // Hashtable<Plate, Owner>
    private static Hashtable<String, String> data_base;

    /*
    Dados
     */
    //<srvc_port> is the port number where the server provides the service
    public static int server_port;

    //<mcast_addr> is the IP address of the multicast group used by the server to advertise its service.
    public static InetAddress multicast_address;

    //<mcast_port> is the multicast group port number used by the server to advertise its service.
    public static int multicast_port;

    /*
    Multicast
     */
    static MulticastSocket multicast_socket;

    public static void main(String[] args) throws IOException{
        if ((args.length > 3) && (args.length < 3)) {
            System.out.println("java Server <srvc_port> <mcast_addr> <mcast_port>");
            return;
        }

        this.server_port = Integer.parseInt(args[0]);
        this.multicast_address = InetAdress.getByName(args[1]);
        this.multicast_port = Integer.parseInt(args[2]);

        data_base= new Hashtable<String,String>();

        DatagramSocket socket = new DatagramSocket(this.server_port);
        multicast_socket = new MulticastSocket(this.multicast_address);
        multicast_socket.setTimeToLeave(10);

        /*
        timer
         */

        try {
            getResponse(socket);
            /*
            getResponse(socket, multicast_socket, srvc_port, mcast_addr, mcast_port);
             */
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
    /*
    public static void getResponse(DatagramSocket socket, MulticastSocket multicast_socket, String srvc_port,
                String mcast_addr, String mcast_port) throws IOException{
    */
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