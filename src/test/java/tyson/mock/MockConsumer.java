package tyson.mock;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;
import tyson.util.Silently;

public class MockConsumer implements ConnectionConsumer {

    public Connection connection;
    public ConnectionProducer producer;
    private boolean hasConsumedSomething;

    @Override
    public synchronized void consumeConnection(Connection connection, ConnectionProducer producer) {
        this.connection = connection;
        this.producer = producer;

        hasConsumedSomething = true;

        notify();
    }

    public synchronized Connection waitForConnection() {
        if(connection == null)
            Silently.wait(this);
        return connection;
    }

    public boolean hasConsumedSomething() {
        return hasConsumedSomething;
    }

}
