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
    public void localServerMustBeStoppedBeforeStartPunchingHoles() {
        assertFalse(localServer.isRunning());
    }

    @Test
    public void localServerMustBeRunningAfterStartedPunchingHoles() {
        tyson.punchHoles();
        assertTrue(localServer.isRunning());
    }

    @Test
    public void holePuncherMustBeStoppedBeforeStartPunchingHoles() {
        assertFalse(holePuncher.isRunning());
    }

    @Test
    public void holePuncherMustBeRunningAfterStartedPunchingHoles() {
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
