package tyson;

import org.junit.Before;
import org.junit.Test;
import tyson.impl.LocalServer;
import tyson.util.ConnectTo;

import java.net.ConnectException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LocalServerStartTest {

    private static final int PORT = 8586;

    private ConnectionProducer localServer;

    @Before
    public void createServer() {
        localServer = new LocalServer(PORT);
    }

    @Test
    public void mustNotBeListeningBeforeStart() throws Exception {
        try {
            ConnectTo.localhost(PORT).close();
            fail("Connection should fail: server should not be listening before calling start()");

        } catch(ConnectException e) {
            assertEquals("Connection refused: connect", e.getMessage());
        }
    }

    @Test
    public void mustBeListeningAfterStart() throws Exception {
        localServer.start();
        try {
            ConnectTo.localhost(PORT).close();
        } finally {
            localServer.stop();
        }
    }

}
