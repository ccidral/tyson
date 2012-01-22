package tyson.mock;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;
import tyson.util.Silently;

public class MockConsumer implements ConnectionConsumer {

    public Connection connection;
    public ConnectionProducer producer;

    @Override
    public synchronized void consumeConnection(Connection connection, ConnectionProducer producer) {
        this.connection = connection;
        this.producer = producer;

        notify();
    }

    public synchronized Connection waitForConnection() {
        if(connection == null)
            Silently.wait(this);
        return connection;
    }

}