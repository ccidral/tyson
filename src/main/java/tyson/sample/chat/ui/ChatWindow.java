package tyson.sample.chat.ui;

import tyson.sample.chat.Chat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ChatWindow {

    private final JFrame frame = new JFrame("Chat");
    private final JTextArea messages = new JTextArea(15, 50);
    private ChatWindowListener listener;

    public ChatWindow(final String myNickname) {
        messages.setBackground(new Color(240, 240, 240));
        messages.setEditable(false);
        messages.setLineWrap(true);
        messages.setAutoscrolls(true);

        final JTextField outgoingMessage = new JTextField();
        outgoingMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 10) {
                    final String message = outgoingMessage.getText();
                    newMessage(message, myNickname);
                    outgoingMessage.setText(null);
                    
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            listener.sendMessage(message, myNickname);
                        }
                    }).start();
                }
            }
        });
        
        JPanel contentPane = new JPanel(new BorderLayout());
        contentPane.add(new JScrollPane(messages, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        contentPane.add(outgoingMessage, BorderLayout.SOUTH);

        frame.setContentPane(contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
    }

    public ChatWindow listener(ChatWindowListener listener) {
        this.listener = listener;
        return this;
    }

    public ChatWindow show() {
        frame.setVisible(true);
        return this;
    }

    public void newMessage(String message, String nickname) {
        messages.append(nickname + ": " + message + "\n");
    }
}
