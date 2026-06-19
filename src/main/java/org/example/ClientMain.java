import server.SocketClient;

public class ClientMain {

    public static void main(String[] args) throws Exception {

        SocketClient client = new SocketClient();

        client.connect();
    }
}
