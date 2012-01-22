package tyson.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Silently {

    public static void wait(Object object) {
        try {
            object.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String readLine(Socket socket) {
        try {
            return new BufferedReader(new InputStreamReader(socket.getInputStream())).readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
