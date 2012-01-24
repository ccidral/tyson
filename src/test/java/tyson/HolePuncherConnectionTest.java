package tyson;

import org.junit.Before;
import org.junit.Test;
import tyson.impl.HolePuncher;
import tyson.mock.MockConnector;
import tyson.mock.MockConsumer;

import java.net.Socket;

import static org.junit.Assert.*;

public class HolePuncherConnectionTest {

    private static final long INTERVAL = 100;

    private MockConnector connector;
    private MockConsumer consumer;
    private ConnectionProducer holePuncher;
    private Socket socket;

    @Before
    public void setUp() throws Exception {
        connector = new MockConnector();
        consumer = new MockConsumer();
        socket = new Socket();
        holePuncher = new HolePuncher(connector, INTERVAL);

        holePuncher.addConsumer(consumer);
        connector.setSocketToReturn(socket);
    }

    @Test(timeout = 3000)
    public void successfulConnectionAtFirstTry() {
        connector.setNumberOfTriesThatMustTimeout(0);

        holePuncher.start();

        Connection connection = consumer.waitForConnection();

        assertNotNull(connection);
        assertSame(socket, connection.getSocket());
        assertSame(holePuncher, consumer.producer);
        assertEquals(1, connector.getNumberOfTries());
    }

    @Test(timeout = 3000)
    public void successfulConnectionAtThirdTry() {
        connector.setNumberOfTriesThatMustTimeout(2);

        holePuncher.start();

        Connection connection = consumer.waitForConnection();

        assertNotNull(connection);
        assertSame(socket, connection.getSocket());
        assertSame(holePuncher, consumer.producer);
        assertEquals(3, connector.getNumberOfTries());
    }

}
