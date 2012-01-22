package tyson.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ConnectTo {

    public static Socket localhost(int port) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("localhost", port));
        return socket;
    }

}
