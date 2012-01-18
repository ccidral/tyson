package tyson;

import java.net.Socket;

public interface ServerListener {

    void clientConnected(Socket socket, Server server, long time);

}
