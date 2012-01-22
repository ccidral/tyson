package tyson;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import tyson.impl.LocalServer;
import tyson.mock.MockConsumer;
import tyson.util.AsyncSocketReader;
import tyson.util.ConnectTo;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import static org.junit.Assert.*;

public class StartedLocalServerTest {

    private static final int PORT = 8586;

    private ConnectionProducer localServer;
    private MockConsumer serverListener;

    @Before
    public void createServer() {
        serverListener = new MockConsumer();
        localServer = new LocalServer(PORT);
        localServer.addConsumer(serverListener);
        localServer.start();
    }

    @After
    public void stopServer() {
        localServer.stop();
    }

    @Test
    public void mustBeListeningAfterStart() throws Exception {
        ConnectTo.localhost(PORT).close();
    }

    @Test
    public void feedConsumersWithIncomingConnection() throws Throwable {
        Socket clientSocket = ConnectTo.localhost(PORT);

        try {
            Connection connection = serverListener.waitForConnection();

            assertNotNull(connection);
            assertNotNull(connection.getSocket());
            assertSame(localServer, serverListener.producer);
        } finally {
            clientSocket.close();
        }
    }

    @Test
    public void ensureClientIsCorrectlyConnectedToServer() throws Throwable {
        Socket clientSocket = ConnectTo.localhost(PORT);

        try {
            Connection connection = serverListener.waitForConnection();
            AsyncSocketReader serverReader = AsyncSocketReader.startReading(connection.getSocket());

            assertEquals(write("Hello", clientSocket), serverReader.getResult());
        } finally {
            localServer.stop();
        }
    }

    private String write(String message, Socket socket) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(message);
        writer.flush();
        return message;
    }

}
