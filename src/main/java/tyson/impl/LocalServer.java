package tyson.impl;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class LocalServer implements ConnectionProducer {

    private ServerSocket serverSocket;
    private final int port;
    private final List<ConnectionConsumer> consumers = new ArrayList<ConnectionConsumer>();

    public LocalServer(int port) {
        this.port = port;
    }

    @Override
    public void start() {
        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(port));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String threadName = getClass().getSimpleName() + " acceptor";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    connectionAccepted(serverSocket.accept());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, threadName).start();
    }

    @Override
    public void stop() {
        if(serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            serverSocket = null;
        }
    }

    @Override
    public void addConsumer(ConnectionConsumer consumer) {
        consumers.add(consumer);
    }

    private void connectionAccepted(Socket socket) {
        Connection connection = new DefaultConnection(socket);
        for(ConnectionConsumer consumer : consumers)
            consumer.consumeConnection(connection, this);
    }

}
