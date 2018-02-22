import java.net.*;
import java.io.*;
import java.io.IOException;
import java.lang.System.*;
import java.lang.Integer;



//
public class Client extends Throwable {


    public static void main(String[] args) throws IOException {
        if(args.length != 4){
            System.err.println("Usage: java Client <host_name> <port_number> <oper> <opnd>");
            return;
        }


        InetAddress address;
        DatagramPacket packet;
        byte[] sendBuf = new byte[256];



        String hostname = args[0];
        System.out.println(hostname+"\n");

        int port = Integer.parseInt(args[1]);
        System.out.println(port+"\n");


        DatagramSocket socket = new DatagramSocket();

        sendRequest(socket, "abs",port);
    }

    // send request
    
    /*
    Implementação Lógica,
    O servidor fica à espera das requests do Cliente, na socket criada por Server na porta estática definida pelo Server, 
    quando o Server responder, não responde para a mesma porta, responde para a porta do cliente, com socket.getPort();
    */
    public static void sendRequest(DatagramSocket socket, String ans, int port) throws IOException{
        InetAddress address = InetAddress.getLocalHost();
        //correto
        //byte[] sbuf = ans.getBytes();
        //try
        byte[] rbuf = {'A','b'};
        DatagramPacket packet = new DatagramPacket(rbuf, rbuf.length,address, port); //port

        try {
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
