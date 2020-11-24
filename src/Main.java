import java.net.SocketException;

public class Main {
    public static void main(String[] args){
        UDPServer s = null;
        try {
            s = new UDPServer();
            s.start();
        } catch (SocketException e) {
            e.printStackTrace();
        }

        ClientC c = new ClientC();
        c.start();

        ClientC1 c1 = new ClientC1();
        c1.start();
    }
}
