import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import java.lang.String;

//
public class Client extends Throwable {


    public static void main(String[] args) throws IOException {
        if(args.length < 4){
            System.err.println("Usage: java Client <host_name> <port_number> <oper> <opnd>");
            return;
        }


        InetAddress address;
        DatagramPacket packet;
        byte[] sendBuf = new byte[256];

        String hostname = args[0];

        //System.out.println(hostname+"\n");

        int port = Integer.parseInt(args[1]);
        //System.out.println(port+"\n");

        String oper = args[2];
        System.out.println(oper+"\n");
        String opnd=args[3];
        System.out.println(opnd+"\n");
        String opndName="";
        if(args.length>4)
        {opndName=args[4];}//sSystem.out.println(opndName);}


        DatagramSocket socket = new DatagramSocket();
        if(oper.equals("REGISTER")){
            sendRequest(socket,"REGISTER "+opnd+ " "+ opndName,port,hostname);
            System.out.println("entrou");
        }
        else if(oper.equals("LOOKUP")){
            sendRequest(socket,"LOOKUP "+opnd,port,hostname);
            // System.out.println("entrou");
        }
        else{ System.out.println("specify operation");System.exit(0);}
        byte[] r_buffer = new byte[512];
        DatagramPacket receivePacket = new DatagramPacket(r_buffer, r_buffer.length);
        socket.receive(receivePacket);
        String response = new String(receivePacket.getData());
        //System.out.println(response);

        socket.close();
        System.out.println("socket closing");
    }

    // send request

    /*
    Implementação Lógica,
    O servidor fica à espera das requests do Cliente, na socket criada por Server na porta estática definida pelo Server,
    quando o Server responder, não responde para a mesma porta, responde para a porta do cliente, com socket.getPort();
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