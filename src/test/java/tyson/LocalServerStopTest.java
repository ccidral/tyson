package tyson;

import org.junit.Before;
import org.junit.Test;
import tyson.impl.LocalServer;

public class LocalServerStopTest {

    private static final int PORT = 8586;

    private ConnectionProducer localServer;

    @Before
    public void createServer() {
        localServer = new LocalServer(PORT);
    }

    @Test
    public void tryToStopWithoutBeforeStart() {
        localServer.stop();
    }

}
