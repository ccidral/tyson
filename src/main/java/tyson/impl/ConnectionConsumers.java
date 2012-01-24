package tyson.impl;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConnectionConsumers {

    private final ConnectionProducer producer;
    private final List<ConnectionConsumer> consumers = new ArrayList<ConnectionConsumer>();

    public ConnectionConsumers(ConnectionProducer producer) {
        this.producer = producer;
    }

    public void add(ConnectionConsumer consumer) {
        consumers.add(consumer);
    }

    public void consume(Socket socket) {
        consume(new DefaultConnection(socket));
    }

    public void consume(Connection connection) {
        for(ConnectionConsumer consumer : consumers)
            consumer.consumeConnection(connection, producer);
    }
}
