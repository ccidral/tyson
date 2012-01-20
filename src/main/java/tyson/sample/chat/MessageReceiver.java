package tyson.sample.chat;

import tyson.sample.chat.ui.ChatWindow;

public class MessageReceiver implements MessageHandler {

    private final ChatWindow chatWindow;

    public MessageReceiver(ChatWindow chatWindow) {
        this.chatWindow = chatWindow;
    }

    @Override
    public void handleMessage(String message, String nickname) {
        chatWindow.newMessage(message, nickname);
    }

}
