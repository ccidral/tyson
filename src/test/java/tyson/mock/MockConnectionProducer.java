package tyson.mock;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;
import tyson.StopListener;
import tyson.lang.NotImplementedYet;

import java.util.ArrayList;
import java.util.List;

public class MockConnectionProducer implements ConnectionProducer {

    private boolean isRunning;
    private final List<ConnectionConsumer> consumers = new ArrayList<ConnectionConsumer>();

    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void start() {
        isRunning = true;
    }

    @Override
    public void stop() {
        isRunning = false;
    }

    @Override
    public void addStopListener(StopListener listener) {
        throw new NotImplementedYet();
    }

    @Override
    public void addConsumer(ConnectionConsumer consumer) {
        consumers.add(consumer);
    }

    public void produceConnection(Connection connection) {
        for(ConnectionConsumer consumer : consumers)
            consumer.consumeConnection(connection, this);
    }

}
