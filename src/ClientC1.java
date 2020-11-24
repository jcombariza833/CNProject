import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClientC1 extends Thread {
    private String localHost = "localhost";
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public ClientC1() {
        System.out.println("START CLIENT C");
    }

    @Override
    public void run() {
        try {
            socket = new DatagramSocket();
            address = InetAddress.getByName(localHost);

            SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());

            String message = String.format("CLIENT C1 SYSTEM TIME IS (%s)",formatter.format(date));
            buf = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 12331);
            socket.send(packet);

            System.out.println("C1: DATA SENT");

            // Receive the response packet from the server
            buf = new byte[256];
            DatagramPacket responsePacket = new DatagramPacket(buf, buf.length);
            socket.receive(responsePacket);

            // Get the string contained in the received packet
            String received = new String(responsePacket.getData(), 0, responsePacket.getLength());

            // Display the received string
            System.out.println(received);

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("C1 to WEB SERVER");
            System.out.println("I/O error: " + ex.getMessage());
        } finally {
            // Close the DatagramSocket
            if(socket != null) {
                socket.close();
                System.out.println("C1: UDP socket closed");
            }
        }
    }
}
