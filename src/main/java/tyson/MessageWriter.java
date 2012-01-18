package tyson;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class MessageWriter {

    private final Socket socket;
    private final Logger logger = new Logger(this);

    public MessageWriter(Socket socket) {
        this.socket = socket;
    }

    public void write(final String text) {
        String threadName = getClass().getSimpleName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    PrintStream writer = new PrintStream(socket.getOutputStream());
                    writer.println(text);
                    writer.flush();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, threadName).start();
    }
}
