package tyson.sample.chat;

import tyson.Logger;
import tyson.util.IO;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

class MessageReader {

    private final Socket socket;

    public MessageReader(Socket socket) {
        this.socket = socket;
    }

    public void keepReading(final MessageHandler handler) {
        String threadName = getClass().getSimpleName();
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = IO.readerFor(socket);
                String payload;
                do {
                    payload = IO.readLine(reader);
                    
                    if(payload != null) {
                        String[] parts = payload.split("\\|");
                        String nickname = parts[0];
                        String message = parts[1];
                        handler.handleMessage(message, nickname);
                    }

                } while(payload != null);
            }
        }, threadName).start();
    }

}
