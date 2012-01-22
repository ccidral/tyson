package tyson.util;

import java.net.Socket;

public class AsyncSocketReader implements Runnable {

    private final Socket socket;
    private String text;

    private AsyncSocketReader(Socket socket) {
        this.socket = socket;
        new Thread(this, getClass().getSimpleName()).start();
    }

    public static AsyncSocketReader startReading(Socket socket) {
        return new AsyncSocketReader(socket);
    }

    public synchronized String getResult() {
        if(text == null)
            Silently.wait(this);
        return text;
    }

    @Override
    public synchronized void run() {
        text = Silently.readLine(socket);
        notify();
    }

}
