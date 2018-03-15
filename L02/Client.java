import java.io.IOException;
import java.lang.String;
import java.net.*;

public class Client extends Throwable {
    //<mcast_addr> is the IP address of the multicast group used by the server to advertise its service;
    public static String multicast_address;

    //<mcast_port> is the port number of the multicast group used by the server to advertise its service;
    public static InetAddress multicast_port;

    //<oper> is ''register'' or ''lookup'', depending on the operation to invoke;
    public static String oper;

    //<opnd> * is the list of operands of the specified operation:
    //<plate number> <owner name>, for register;
    //<plate number>, for lookup.
    public static String opnd;

    public static void main(String[] args) throws IOException {
        if(args.length < 4){
            /*
            System.err.println("Usage: java Cliente <host_name> <port_number> <oper> <opnd>");
             */
            System.err.println("java Cliente <mcast_addr> <mcast_port> <oper> <opnd> *");
            return;
        }

        InetAddress address;
        DatagramPacket packet;
        byte[] sendBuf = new byte[256];

        DatagramSocket socket = new DatagramSocket();

        if(oper.equals("REGISTER")){
            /*
            sendRequest(socket,"REGISTER " + opnd + " " + opndName, port, hostname);
             */
            System.out.println("entrou");
        }else if(oper.equals("LOOKUP")){
            /*
            sendRequest(socket,"LOOKUP " + opnd, port, hostname);
             */
        }else{
            System.out.println("specify operation");
            System.exit(0);
        }

        byte[] r_buffer = new byte[512];
        DatagramPacket receivePacket = new DatagramPacket(r_buffer, r_buffer.length);
        socket.receive(receivePacket);
        String response = new String(receivePacket.getData());

        socket.close();
        System.out.println("socket closing");
    }

    /*
    send request

    Implementação Lógica,
    O servidor fica à espera das requests do Cliente, na socket criada por Servidor na porta estática definida pelo Servidor,
    quando o Servidor responder, não responde para a mesma porta, responde para a porta do cliente, com socket.getPort();
    */
    public static void sendRequest(DatagramSocket socket, String ans, int port,String hostname) throws IOException{
        InetAddress address = InetAddress.getByName(hostname);

        byte[] rbuf = ans.getBytes();

        try {
            DatagramPacket packet = new DatagramPacket(rbuf, rbuf.length,address, port); //port
            socket.send(packet);
        }catch (IOException e){
            System.err.println("Socket error");
            throw new IOException("Socket error");
        }

    }

    // display response
    public static void displayResponse(DatagramPacket packet, DatagramSocket socket){
        String received = new String(packet.getData());
        System.out.println("Echoed Message: " + received);
        socket.close();
    }
}