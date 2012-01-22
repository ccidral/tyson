package tyson;

import org.junit.Before;
import org.junit.Test;
import tyson.impl.Tyson;
import tyson.mock.MockConnection;
import tyson.mock.MockConnectionProducer;
import tyson.mock.MockConsumer;

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
    public void clientGotTheConnectionProducedByTheLocalServer() {
        final Connection producedConnection = new MockConnection();
        final MockConsumer client = new MockConsumer();

        tyson.addConsumer(client);
        tyson.start();

        localServer.produceConnection(producedConnection);

        assertSame(producedConnection, client.connection);
        assertSame(tyson, client.producer);
    }

    @Test
    public void clientGotTheConnectionProducedByTheHolePuncher() {
        final Connection producedConnection = new MockConnection();
        final MockConsumer client = new MockConsumer();

        tyson.addConsumer(client);
        tyson.start();

        holePuncher.produceConnection(producedConnection);

        assertSame(producedConnection, client.connection);
        assertSame(tyson, client.producer);
    }

}
