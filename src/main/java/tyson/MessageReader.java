package tyson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MessageReader {

    private final Socket socket;
    private final Logger logger = new Logger(this);

    public MessageReader(Socket socket) {
        this.socket = socket;
    }

    public void readAndPrint() {
        String threadName = getClass().getSimpleName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    logger.info(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, threadName).start();
    }
}
