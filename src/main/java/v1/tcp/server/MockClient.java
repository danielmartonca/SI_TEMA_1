package v1.tcp.server;

import java.io.IOException;
import java.net.Socket;

public class MockClient extends ClientThread {
    public MockClient(Socket socket) {
        super(socket);
    }

    @Override
    public void run() {
        try {
            while (in.readLine().equalsIgnoreCase("close")) {
                out.println("A B and MC already set. Cannot accept any more nodes.");
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}