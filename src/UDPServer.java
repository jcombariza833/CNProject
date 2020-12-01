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
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            datagramSocket.close();
        }
    }
}


