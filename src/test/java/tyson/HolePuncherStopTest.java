package tyson;

import org.junit.Before;
import org.junit.Test;
import tyson.impl.HolePuncher;
import tyson.mock.MockConnector;
import tyson.mock.MockConsumer;
import tyson.mock.MockStopListener;

import static org.junit.Assert.*;

public class HolePuncherStopTest {

    private static final long INTERVAL = 100;

    private MockConnector connector;
    private MockConsumer consumer;
    private ConnectionProducer holePuncher;
    private MockStopListener stopListener;

    @Before
    public void setUp() throws Exception {
        connector = new MockConnector();
        consumer = new MockConsumer();
        stopListener = new MockStopListener();
        holePuncher = new HolePuncher(connector, INTERVAL);

        holePuncher.addConsumer(consumer);
        holePuncher.addStopListener(stopListener);

        connector.setNumberOfTriesThatMustTimeout(MockConnector.ALWAYS_TIMEOUT);
    }

    @Test(timeout = 3000)
    public void stopBeforeStart() throws Exception {
        holePuncher.stop();
    }

    @Test(timeout = 3000)
    public void stopBeforeTryingToConnectTheThirdTime() throws Exception {
        holePuncher.start();
        connector.waitForNumberOfTries(2);

        holePuncher.stop();
        stopListener.waitForStop();

        assertEquals(2, connector.getNumberOfTries());
        assertTrue(connector.isCancelled());
        assertFalse(consumer.hasConsumedSomething());
    }

    @Test(timeout = 3000)
    public void stopWhileTryingToConnect() throws Exception {
        connector.setMustWaitToBeCancelledWhileTryingToConnect(true);

        holePuncher.start();
        connector.waitForFirstTry();
        holePuncher.stop();
        stopListener.waitForStop();

        assertTrue(connector.isCancelled());
        assertFalse(consumer.hasConsumedSomething());
    }

}
