import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;

public class Responder implements Runnable {
    private DatagramSocket socket;
    private DatagramPacket packet;
    private byte[] buf;

    public Responder(DatagramSocket socket, DatagramPacket packet, byte[] buf) {
        this.socket = socket;
        this.packet = packet;
        this.buf = buf;
    }

    @Override
    public void run() {
        System.out.println();
        System.out.println("S: new connection to client");
        InetAddress address = packet.getAddress();
        int port = packet.getPort();
        packet = new DatagramPacket(buf, buf.length, address, port);
        String received = new String(packet.getData(), 0, packet.getLength());

        System.out.println("S: " + received.trim());

        // Build a DatagramPacket object to send a request packet to the server (the server is running locally)
        if(received.contains("CLIENT C1 SYSTEM TIME IS")) {
            String response = "TIME ACK";
            buf = response.getBytes();
            DatagramPacket responsePacket = new DatagramPacket(buf, buf.length, packet.getAddress(), packet.getPort());

            // Send the packet to the client
            try {
                socket.send(responsePacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
