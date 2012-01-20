package tyson;

import org.junit.Before;
import org.junit.Test;
import tyson.impl.DefaultTyson;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TysonTest {

    private Tyson tyson;
    private MockConnectionProducer localServer;
    private MockConnectionProducer holePuncher;

    @Before
    public void setUp() {
        localServer = new MockConnectionProducer();
        holePuncher = new MockConnectionProducer();
        tyson = new DefaultTyson(localServer, holePuncher);
    }

    @Test
    public void localServerIsStoppedBeforeStartPunchingHoles() {
        assertFalse(localServer.isRunning());
    }

    @Test
    public void localServerIsRunningAfterStartedPunchingHoles() {
        tyson.punchHoles();
        assertTrue(localServer.isRunning());
    }

    @Test
    public void holePuncherIsStoppedBeforeStartPunchingHoles() {
        assertFalse(holePuncher.isRunning());
    }

    @Test
    public void holePuncherIsRunningAfterStartedPunchingHoles() {
        tyson.punchHoles();
        assertTrue(holePuncher.isRunning());
    }

    @Test
    public void shutdownLocalServerWhenHolePuncherConnectedToPeer() {
        tyson.punchHoles();
        holePuncher.produceConnection();
        assertFalse(localServer.isRunning());
    }

    @Test
    public void stopHolePuncherWhenPeerConnectedToLocalServer() {
        tyson.punchHoles();
        localServer.produceConnection();
        assertFalse(holePuncher.isRunning());
    }

}
