package tyson.impl;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;
import tyson.lang.NotImplementedYet;

import java.util.ArrayList;
import java.util.List;

public class Tyson implements ConnectionProducer, ConnectionConsumer {

    private final ConnectionProducer localServer;
    private final ConnectionProducer holePuncher;
    private final List<ConnectionConsumer> consumers = new ArrayList<ConnectionConsumer>();

    public Tyson(ConnectionProducer localServer, ConnectionProducer holePuncher) {
        this.localServer = localServer;
        this.holePuncher = holePuncher;

        localServer.addConsumer(this);
        holePuncher.addConsumer(this);
    }

    @Override
    public void start() {
        localServer.start();
        holePuncher.start();
    }

    @Override
    public void stop() {
        throw new NotImplementedYet();
    }

    @Override
    public void addConsumer(ConnectionConsumer consumer) {
        consumers.add(consumer);
    }

    @Override
    public void consumeConnection(Connection connection, ConnectionProducer producer) {
        if(producer == localServer)
            holePuncher.stop();
        else
            localServer.stop();

        for(ConnectionConsumer consumer : consumers)
            consumer.consumeConnection(connection, this);
    }

}
