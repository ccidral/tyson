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
    public void localServerMustBeStoppedBeforeStartTyson() {
        assertFalse(localServer.isRunning());
    }

    @Test
    public void localServerMustBeRunningAfterStartedTyson() {
        tyson.start();
        assertTrue(localServer.isRunning());
    }

    @Test
    public void holePuncherMustBeStoppedBeforeStartTyson() {
        assertFalse(holePuncher.isRunning());
    }

    @Test
    public void holePuncherMustBeRunningAfterStartedTyson() {
        tyson.start();
        assertTrue(holePuncher.isRunning());
    }

    @Test
    public void shutdownLocalServerWhenHolePuncherConnectedToPeer() {
        tyson.start();
        holePuncher.produceConnection(null);
        assertFalse(localServer.isRunning());
    }

    @Test
    public void stopHolePuncherWhenPeerConnectedToLocalServer() {
        tyson.start();
        localServer.produceConnection(null);
        assertFalse(holePuncher.isRunning());
    }

    @Test
    public void makeSureConnectionProducedByLocalServerIsCorrectlyPassedToTysonListeners() {
        tyson.start();

        final Connection expectedConnection = new MockConnection();
        final ConnectionCatcher catcher = new ConnectionCatcher();

        tyson.addConsumer(catcher);

        localServer.produceConnection(expectedConnection);

        assertSame(expectedConnection, catcher.connection);
        assertSame(tyson, catcher.producer);
    }

    @Test
    public void makeSureConnectionProducedByHolePuncherIsCorrectlyPassedToTysonListeners() {
        tyson.start();

        final Connection expectedConnection = new MockConnection();
        final ConnectionCatcher catcher = new ConnectionCatcher();

        tyson.addConsumer(catcher);

        holePuncher.produceConnection(expectedConnection);

        assertSame(expectedConnection, catcher.connection);
        assertSame(tyson, catcher.producer);
    }

    private class ConnectionCatcher implements ConnectionConsumer {

        public Connection connection;
        public ConnectionProducer producer;

        @Override
        public void consumeConnection(Connection connection, ConnectionProducer producer) {
            this.connection = connection;
            this.producer = producer;
        }
    }

}
