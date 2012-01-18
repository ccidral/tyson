package tyson;

import java.net.Socket;

public interface ClientListener {

    void connectedToServer(Socket socket);

    boolean shouldTryToConnectAgain();

}
