package tyson.impl;

import tyson.Connection;

import java.net.Socket;

public class DefaultConnection implements Connection {

    private final Socket socket;

    public DefaultConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public Socket getSocket() {
        return socket;
    }

}
