package tyson.impl;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;

import java.util.ArrayList;
import java.util.List;

public class Tyson implements ConnectionProducer {

    private final ConnectionProducer[] connectionProducers;
    private final List<ConnectionConsumer> consumers = new ArrayList<ConnectionConsumer>();
    private final ConnectionConsumer funnel = new Funnel();

    public Tyson(ConnectionProducer... connectionProducers) {
        this.connectionProducers = connectionProducers;

        for(ConnectionProducer producer : connectionProducers)
            producer.addConsumer(funnel);
    }

    @Override
    public void start() {
        for(ConnectionProducer producer : connectionProducers)
            producer.start();
    }

    @Override
    public void stop() {
        for(ConnectionProducer producer : connectionProducers)
            producer.stop();
    }

    @Override
    public void addConsumer(ConnectionConsumer consumer) {
        consumers.add(consumer);
    }

    private class Funnel implements ConnectionConsumer {

        @Override
        public void consumeConnection(Connection connection, ConnectionProducer theProducer) {
            for(ConnectionProducer otherProducer : connectionProducers)
                if(otherProducer != theProducer)
                    otherProducer.stop();

            for(ConnectionConsumer consumer : consumers)
                consumer.consumeConnection(connection, Tyson.this);
        }

    }

}
