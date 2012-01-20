package tyson;

import tyson.impl.ConnectionConsumer;
import tyson.impl.ConnectionProducer;

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
    public void addConsumer(ConnectionConsumer consumer) {
        consumers.add(consumer);
    }

    public void produceConnection() {
        for(ConnectionConsumer consumer : consumers)
            consumer.consumeConnection(this);
    }

}
