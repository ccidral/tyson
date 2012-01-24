package tyson.impl;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;
import tyson.StopListener;
import tyson.lang.NotImplementedYet;

import java.io.IOException;
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
    public void addConsumer(ConnectionConsumer consumer) {
        consumers.add(consumer);
    }

    @Override
    public void start() {
        serverSocket = createServerSocket(port);
        acceptOneConnection();
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
    public void addStopListener(StopListener listener) {
        throw new NotImplementedYet();
    }

    private static ServerSocket createServerSocket(int port) {
        try {
            return new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void acceptOneConnection() {
        String threadName = getClass().getSimpleName() + "@" + port + " acceptor";
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    deliverToConsumers(serverSocket.accept());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, threadName).start();
    }

    private void deliverToConsumers(Socket socket) {
        Connection connection = new DefaultConnection(socket);
        for(ConnectionConsumer consumer : consumers)
            consumer.consumeConnection(connection, this);
    }

}
