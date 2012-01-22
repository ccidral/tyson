package tyson;

import tyson.util.Silently;

class MockConsumer implements ConnectionConsumer {

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
