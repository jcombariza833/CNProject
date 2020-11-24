import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread {
    private DatagramSocket datagramSocket;
    private int portNumber = 12331;

    public UDPServer() throws SocketException {
        System.out.println("START SERVER S");
        // create a DatagramSocket instance and bind it to the specified port number
        datagramSocket = new DatagramSocket(portNumber);
    }

    @Override
    public void run(){
        try {
            while (! isInterrupted()) {

                // Build a DatagramPacket object to receive a packet

                byte[] buffer = new byte[2048];
                DatagramPacket requestPacket = new DatagramPacket(buffer, buffer.length);
                datagramSocket.receive(requestPacket);

                new Thread(new Responder(datagramSocket,requestPacket,buffer)).start();

    //                // Receive a packet from a client. The method "receive" blocks until a packet arrives
    //                datagramSocket.receive(requestPacket);
    //
    //                // Get the string contained in the received packet
    //                String request = new String(requestPacket.getData(), 0, requestPacket.getLength());

    //                if(request.equals("GET")){
    //                    // Build a DatagramPacket object to send a response packet to the client
    //                    String response = new Date().toString();
    //                    buffer = response.getBytes();
    //                    DatagramPacket responsePacket = new DatagramPacket(buffer, buffer.length, requestPacket.getAddress(), requestPacket.getPort());
    //
    //                    // Send the packet to the client
    //                    datagramSocket.send(responsePacket);
    //                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            datagramSocket.close();
        }
    }
}


