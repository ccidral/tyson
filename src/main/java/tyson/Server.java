package tyson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket serverSocket;
    private ServerListener listener;
    private final Logger logger = new Logger(this);

    public Server(int port) {
        try {
            serverSocket = new ServerSocket();
            serverSocket.setReuseAddress(true);
            serverSocket.bind(new InetSocketAddress(port));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setListener(ServerListener listener) {
        this.listener = listener;
    }

    public void start() {
        logger.info("Starting...");
        String threadName = getClass().getSimpleName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    listenForNewConnection();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, threadName).start();
    }

    public void stop() {
        logger.info("Stopping...");
        try {
            serverSocket.close();
            logger.info("Stopped");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void listenForNewConnection() throws IOException {
        logger.info("Listening for new connection at port " + serverSocket.getLocalPort());
        final Socket socket = serverSocket.accept();
        final long now = System.currentTimeMillis();

        logger.info("Client connected: " + socket);
        listener.clientConnected(socket, this, now);
    }
}
