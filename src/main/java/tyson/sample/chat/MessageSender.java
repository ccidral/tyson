package tyson.sample.chat;

import tyson.sample.chat.ui.ChatWindowListener;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class MessageSender implements ChatWindowListener {

    private final PrintStream writer;

    public MessageSender(Socket socket) {
        try {
            writer = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendMessage(String message, String nickname) {
        writer.println(nickname + "|" + message);
        writer.flush();
    }

}
