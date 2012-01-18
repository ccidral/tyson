package tyson;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Client {

    private ClientListener listener;
    private final Logger logger = new Logger(this);

    public void setListener(ClientListener listener) {
        this.listener = listener;
    }

    public void keepTryingToConnectEvery(int intervalInMilliseconds, String remoteHost, int port) {
        InetSocketAddress remoteAddress = new InetSocketAddress(remoteHost, port);
        Socket socket;
        boolean timeouted;
        do {
            logger.info("Trying to connect to " + remoteHost + ":" + port);
            socket = new Socket();
            try {
                socket.setReuseAddress(true);
                socket.bind(new InetSocketAddress(port));
                socket.connect(remoteAddress, intervalInMilliseconds);

                timeouted = false;

            } catch (SocketTimeoutException e) {
                timeouted = true;

            } catch (IOException e) {
                timeouted = false;
                e.printStackTrace();
            }

            if(timeouted)
                logger.info("Timeouted");

        } while(timeouted && listener.shouldTryToConnectAgain());

        logger.info("Connected to " + socket);

        listener.connectedToServer(socket);
    }
}
