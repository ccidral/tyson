package tyson.util;

import java.net.Socket;

public class AsyncSocketReader {

    private final Socket socket;
    private String text;

    private AsyncSocketReader(Socket socket) {
        this.socket = socket;
        new ReadingThread().start();
    }

    public static AsyncSocketReader startReading(Socket socket) {
        return new AsyncSocketReader(socket);
    }

    public synchronized String getResult() {
        if(text == null)
            Quietly.wait(this);
        return text;
    }

    private synchronized void readLineFromSocket() {
        text = Quietly.readLine(socket);
        notify();
    }

    private class ReadingThread extends Thread {
        @Override
        public void run() {
            readLineFromSocket();
        }
    }

}
