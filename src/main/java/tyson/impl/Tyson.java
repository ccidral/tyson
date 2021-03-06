package tyson.impl;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;
import tyson.StopListener;
import tyson.lang.NotImplementedYet;

public class Tyson implements ConnectionProducer {

    private final ConnectionProducer[] producers;
    private final ConnectionConsumers consumers = new ConnectionConsumers(this);

    public Tyson(ConnectionProducer... connectionProducers) {
        this.producers = connectionProducers;

        for(ConnectionProducer producer : connectionProducers)
            producer.addConsumer(new Funnel());
    }

    @Override
    public void addConsumer(ConnectionConsumer consumer) {
        consumers.add(consumer);
    }

    @Override
    public void start() {
        for(ConnectionProducer producer : producers)
            producer.start();
    }

    @Override
    public void stop() {
        for(ConnectionProducer producer : producers)
            producer.stop();
    }

    @Override
    public void addStopListener(StopListener listener) {
        throw new NotImplementedYet();
    }

    private void stopAllProducersExcept(ConnectionProducer excludedProducer) {
        for(ConnectionProducer otherProducer : producers)
            if(otherProducer != excludedProducer)
                otherProducer.stop();
    }

    private class Funnel implements ConnectionConsumer {

        @Override
        public void consumeConnection(Connection connection, ConnectionProducer thisProducer) {
            stopAllProducersExcept(thisProducer);
            consumers.consume(connection);
        }

    }

}
