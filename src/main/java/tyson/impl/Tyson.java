package tyson.impl;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;
import tyson.lang.NotImplementedYet;

import java.util.ArrayList;
import java.util.List;

public class Tyson implements ConnectionProducer, ConnectionConsumer {

    private final ConnectionProducer[] connectionProducers;
    private final List<ConnectionConsumer> consumers = new ArrayList<ConnectionConsumer>();

    public Tyson(ConnectionProducer... connectionProducers) {
        this.connectionProducers = connectionProducers;

        for(ConnectionProducer producer : connectionProducers)
            producer.addConsumer(this);
    }

    @Override
    public void start() {
        for(ConnectionProducer producer : connectionProducers)
            producer.start();
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
    public void consumeConnection(Connection connection, ConnectionProducer theProducer) {
        for(ConnectionProducer otherProducer : connectionProducers)
            if(otherProducer != theProducer)
                otherProducer.stop();

        for(ConnectionConsumer consumer : consumers)
            consumer.consumeConnection(connection, this);
    }

}
