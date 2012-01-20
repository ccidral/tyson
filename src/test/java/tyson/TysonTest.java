package tyson;

import org.junit.Before;
import org.junit.Test;
import tyson.impl.Tyson;

import static org.junit.Assert.*;

public class TysonTest {

    private ConnectionProducer tyson;
    private MockConnectionProducer localServer;
    private MockConnectionProducer holePuncher;

    @Before
    public void setUp() {
        localServer = new MockConnectionProducer();
        holePuncher = new MockConnectionProducer();
        tyson = new Tyson(localServer, holePuncher);
    }

    @Test
    public void producersMustNotBeRunningBeforeStartTyson() {
        assertFalse(localServer.isRunning());
        assertFalse(holePuncher.isRunning());
    }

    @Test
    public void producersMustBeRunningAfterStartedTyson() {
        tyson.start();
        assertTrue(localServer.isRunning());
        assertTrue(holePuncher.isRunning());
    }

    @Test
    public void producersMustNotBeRunningAfterStoppingTyson() {
        tyson.start();
        tyson.stop();

        assertFalse(localServer.isRunning());
        assertFalse(holePuncher.isRunning());
    }

    @Test
    public void shutdownLocalServerWhenHolePuncherConnectedToPeer() {
        tyson.start();
        holePuncher.produceConnection(null);
        assertTrue(holePuncher.isRunning());
        assertFalse(localServer.isRunning());
    }

    @Test
    public void stopHolePuncherWhenPeerConnectedToLocalServer() {
        tyson.start();
        localServer.produceConnection(null);
        assertTrue(localServer.isRunning());
        assertFalse(holePuncher.isRunning());
    }

    @Test
    public void getTheConnectionFromLocalServer() {
        final Connection producedConnection = new MockConnection();
        final TysonClient client = new TysonClient();

        tyson.addConsumer(client);
        tyson.start();

        localServer.produceConnection(producedConnection);

        assertSame(producedConnection, client.connection);
        assertSame(tyson, client.producer);
    }

    @Test
    public void getTheConnectionFromHolePuncher() {
        final Connection producedConnection = new MockConnection();
        final TysonClient client = new TysonClient();

        tyson.addConsumer(client);
        tyson.start();

        holePuncher.produceConnection(producedConnection);

        assertSame(producedConnection, client.connection);
        assertSame(tyson, client.producer);
    }

    private class TysonClient implements ConnectionConsumer {

        public Connection connection;
        public ConnectionProducer producer;

        @Override
        public void consumeConnection(Connection connection, ConnectionProducer producer) {
            this.connection = connection;
            this.producer = producer;
        }
    }

}
