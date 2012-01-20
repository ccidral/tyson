package tyson;

import tyson.sample.chat.Chat;
import tyson.util.Silently;

import java.io.*;
import java.net.Socket;
import java.util.Date;

public class Tyson {

    public static final int PORT = 8182;
    private static final int ONE_SECOND = 1000;

    private boolean connected;
    private final Logger logger = new Logger(this);
    private TysonListener listener;

    public void punchHolesFor(String remoteHost) {
        Server server = new Server(PORT);
        Client client = new Client();
        Listener listener = new Listener();

        server.setListener(listener);
        server.start();

        client.setListener(listener);
        client.keepTryingToConnectEvery(ONE_SECOND, remoteHost, PORT);
    }

    private boolean useThis(Socket socket) {
        boolean willUse;

        synchronized (this) {
            willUse = !connected && socket.isConnected();
            if(willUse)
                connected = true;
        }

        logger.info((willUse ? "Using " : "Will not use ") + socket);

        if(willUse)
            notifyListener(socket);

        return willUse;
    }

    private void notifyListener(final Socket socket) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                listener.handleConnection(socket);
            }
        }, "Listener notification").start();
    }

    public Tyson listener(TysonListener listener) {
        this.listener = listener;
        return this;
    }

    private class Listener implements ClientListener, ServerListener {

        @Override
        public void clientConnected(Socket socket, Server server, long time) {
            if(!useThis(socket))
                server.stop();
        }

        @Override
        public void connectedToServer(Socket socket) {
            if(!useThis(socket))
                Silently.close(socket);
        }

        @Override
        public boolean shouldTryToConnectAgain() {
            synchronized (Tyson.this) {
                return !connected;
            }
        }

    }
}

