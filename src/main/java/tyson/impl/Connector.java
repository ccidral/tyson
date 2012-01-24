package tyson.impl;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public interface Connector {

    Socket connect(long timeout) throws SocketTimeoutException, SocketException;

    void cancelAnyPendingConnection();

}
