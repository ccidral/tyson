package tyson.mock;

import tyson.impl.Connector;
import tyson.util.Silently;

import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

public class MockConnector implements Connector {

    public static final int ALWAYS_TIMEOUT = -1;

    private final Object cancellationSignal = new Object();

    private Socket socket;
    private int numberOfActualTries;
    private int numberOfTriesThatMustTimeout;
    private boolean mustWaitToBeCancelledWhileTryingToConnect;
    private boolean isCancelled;

    public void setSocketToReturn(Socket socket) {
        this.socket = socket;
    }

    public synchronized int getNumberOfTries() {
        return numberOfActualTries;
    }

    public void setNumberOfTriesThatMustTimeout(int numberOfTries) {
        this.numberOfTriesThatMustTimeout = numberOfTries;
    }

    public void setMustWaitToBeCancelledWhileTryingToConnect(boolean mustWait) {
        mustWaitToBeCancelledWhileTryingToConnect = mustWait;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void waitForFirstTry() {
        waitForNumberOfTries(1);
    }

    public synchronized void waitForNumberOfTries(int numberOfTriesToWaitFor) {
        while(!alreadyTried(numberOfTriesToWaitFor))
            Silently.wait(this);
    }

    @Override
    public Socket connect(long timeout) throws SocketTimeoutException, SocketException {
        newTry();

        if(mustWaitToBeCancelledWhileTryingToConnect)
            waitForCancellation();

        Silently.sleep(timeout);

        if(shouldTimeout())
            throw new SocketTimeoutException();

        return socket;
    }

    @Override
    public void cancelAnyPendingConnection() {
        isCancelled = true;

        synchronized (cancellationSignal) {
            cancellationSignal.notify();
        }
    }

    private synchronized void newTry() {
        numberOfActualTries++;
        notify();
    }

    private synchronized boolean alreadyTried(int numberOfTries) {
        return numberOfActualTries >= numberOfTries;
    }

    private synchronized boolean shouldTimeout() {
        return shouldAlwaysTimeout() || numberOfActualTries <= numberOfTriesThatMustTimeout;
    }

    private boolean shouldAlwaysTimeout() {
        return numberOfTriesThatMustTimeout == ALWAYS_TIMEOUT;
    }

    private void waitForCancellation() throws SocketException {
        synchronized (cancellationSignal) {
            Silently.wait(cancellationSignal);
        }
        throw new SocketException();
    }

}
