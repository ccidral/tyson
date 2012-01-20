package tyson.util;

import java.io.IOException;
import java.net.Socket;

public class Silently {
    public static void close(Socket socket) {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
