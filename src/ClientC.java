import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

public class ClientC extends Thread {
    private StringBuilder wResponse;
    private String localHost = "localhost";
    private DatagramSocket socket;
    private InetAddress address;

    private byte[] buf;

    public ClientC() {
        System.out.println("START CLIENT C");
        wResponse = new StringBuilder("");
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
        String hotsName = "www.example.com";
        Socket socket = new Socket(hotsName,80);
        PrintStream out = new PrintStream( socket.getOutputStream() );
        BufferedReader in = new BufferedReader(new InputStreamReader( socket.getInputStream()));

        out.println("GET / HTTP/1.1");
        out.println("Host: " + hotsName);
        out.println("User-Agent: Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:83.0) Gecko/20100101 Firefox/83.0");
        out.println("Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        out.println("Accept-Language: en-US,en;q=0.5");
//        out.println("Accept-Encoding: gzip, deflate");
        out.println("DNT: 1");
        out.println("Connection: keep-alive");
        out.println("Upgrade-Insecure-Requests: 1");
        out.println("");
        out.flush();

        String line;
        while ((line = in.readLine()) != null) {
            wResponse.append(line).append("\n");

            // hardcore to make the process faster
            if(wResponse.length() == 1599) break;  // long of the message

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

        buf = wResponse.toString().getBytes();
        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 12331);
        socket.send(packet);

        System.out.println();
        System.out.println("C: DATA SENT");
        socket.close();
        System.out.println("C: UDP socket closed");
    }
}
