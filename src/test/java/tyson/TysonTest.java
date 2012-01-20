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
        localServer.produceConnection(expectedConnection);

        tyson.addConsumer(new ConnectionConsumer() {
            @Override
            public void consumeConnection(Connection connection, ConnectionProducer producer) {
                assertSame(expectedConnection, connection);
            }
        });
    }

    @Test
    public void makeSureConnectionProducedByHolePuncherIsCorrectlyPassedToTysonListeners() {
        tyson.start();

        final Connection expectedConnection = new MockConnection();
        holePuncher.produceConnection(expectedConnection);

        tyson.addConsumer(new ConnectionConsumer() {
            @Override
            public void consumeConnection(Connection connection, ConnectionProducer producer) {
                assertSame(expectedConnection, connection);
            }
        });
    }

}
