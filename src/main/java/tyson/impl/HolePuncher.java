package tyson.impl;

import tyson.Connection;
import tyson.ConnectionConsumer;
import tyson.ConnectionProducer;
import tyson.StopListener;
import tyson.log.Logger;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class HolePuncher implements ConnectionProducer {

    private final ConnectionTrier connectionTrier;
    private final List<ConnectionConsumer> consumers = new ArrayList<ConnectionConsumer>();
    private final List<StopListener> stopListeners = new ArrayList<StopListener>();
    private final Logger logger = new Logger(this);

    public HolePuncher(Connector connector, long interval) {
        this.connectionTrier = new ConnectionTrier(connector, interval);
    }

    @Override
    public void start() {
        connectionTrier.start();
    }

    @Override
    public void stop() {
        connectionTrier.stop();
    }

    @Override
    public void addStopListener(StopListener listener) {
        stopListeners.add(listener);
    }

    @Override
    public void addConsumer(ConnectionConsumer consumer) {
        consumers.add(consumer);
    }

    private void deliverToConsumers(Socket socket) {
        Connection connection = new DefaultConnection(socket);
        for(ConnectionConsumer consumer : consumers)
            consumer.consumeConnection(connection, this);
    }

    private class ConnectionTrier implements Runnable {

        private final Connector connector;
        private final long interval;

        private boolean isCancelled;

        private ConnectionTrier(Connector connector, long interval) {
            this.connector = connector;
            this.interval = interval;
        }

        public void start() {
            new Thread(this).start();
        }

        public synchronized void stop() {
            isCancelled = true;
            connector.cancelAnyPendingConnection();
        }

        private synchronized boolean isCancelled() {
            return isCancelled;
        }

        @Override
        public void run() {
            Socket socket = keepTryingToConnect();

            if(socket != null)
                deliverToConsumers(socket);

            for(StopListener listener : stopListeners)
                listener.onStop();
        }

        private Socket keepTryingToConnect() {
            while (!isCancelled()) {
                try {
                    return connector.connect(interval);
                }

                catch (SocketTimeoutException timeoutException) {
                    //it's okay, try again
                }

                catch (SocketException socketException) {
                    if(!isCancelled())
                        logger.error(socketException);
                }
            }

            return null;
        }

    }

}
