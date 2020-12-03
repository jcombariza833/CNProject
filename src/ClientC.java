import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.*;

public class ClientC extends Thread {
    private String localHost = "localhost";
    private DatagramSocket socket;
    private InetAddress address;
    private byte[] packet;
    private int packetLength;
    private ByteArrayOutputStream wResponse;
    private int responseLength;

    public ClientC() {
        System.out.println("START CLIENT C");
        wResponse = new ByteArrayOutputStream();
        responseLength = 0;
        packet = new byte[1500];
    }

    @Override
    public void run() {
        try {
            sendRequestToWebServer();
            sendRequestToLocalServer();
        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("C to WEB SERVER");
            System.out.println("I/O error: " + ex.getMessage());
        }
    }

    private void sendRequestToWebServer() throws IOException,UnknownHostException  {
        String hostName = "www.example.com";
        Socket socket = new Socket(hostName, 80);
        PrintStream out = new PrintStream( socket.getOutputStream() );
        InputStream in = socket.getInputStream();

        out.println("GET / HTTP/1.1");
        out.println("Host: " + hostName);
        out.println("User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:83.0) Gecko/20100101 Firefox/83.0");
        out.println("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        out.println("Accept-Language: en-US,en;q=0.5");
        out.println("Accept-Encoding: gzip, deflate");
        out.println("DNT: 1");
        out.println("Connection: keep-alive");
        out.println("Upgrade-Insecure-Requests: 1");
        out.println("");
        out.flush();

        while ((packetLength = in.read(packet)) != -1) { // -1 is end of stream
            responseLength += packetLength;
            wResponse.write(packet, 0, packetLength);
        }

        System.out.println();
        System.out.println("C: message received from W");

        in.close();
        out.close();
        socket.close();

        System.out.println("C: TCP socket closed");
    }

    public void sendRequestToLocalServer() throws IOException,UnknownHostException  {
        socket = new DatagramSocket();
        address = InetAddress.getByName(localHost);

        DatagramPacket packet = new DatagramPacket(wResponse.toByteArray(), responseLength, address, 12331);
        socket.send(packet);

        System.out.println();
        System.out.println("C: DATA SENT");
        socket.close();
        System.out.println("C: UDP socket closed");
    }
}
