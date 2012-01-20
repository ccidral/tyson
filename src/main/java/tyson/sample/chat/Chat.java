package tyson.sample.chat;

import tyson.Tyson;
import tyson.TysonListener;
import tyson.sample.chat.ui.ChatWindow;
import tyson.sample.chat.ui.ChatWindowListener;
import tyson.sample.chat.ui.ConnectionForm;
import tyson.sample.chat.ui.ConnectionFormListener;

import java.io.IOException;
import java.net.Socket;

public class Chat implements ConnectionFormListener {

    public static void main(String[] args) throws Exception {
        new Chat().start();
    }

    private void start() {
        new ConnectionForm().listener(this).show();
    }

    @Override
    public void connect(final String myNickname, String peerAddress, final ConnectionForm connectionForm) {
        TysonListener listener = new TysonListener() {
            @Override
            public void handleConnection(Socket socket) {
                connectionForm.close();
                connectedTo(socket, myNickname);
            }
        };
        new Tyson().listener(listener).punchHolesFor(peerAddress);
    }

    public void connectedTo(Socket socket, String myNickname) {
        ChatWindow chatWindow = new ChatWindow(myNickname).listener(new MessageSender(socket)).show();
        new MessageReader(socket).keepReading(new MessageReceiver(chatWindow));
    }

}
