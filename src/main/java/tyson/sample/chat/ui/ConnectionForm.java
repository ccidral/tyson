package tyson.sample.chat.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ConnectionForm {

    public static final int SPACING = 8;

    private final Action connectAction = new AbstractAction("Connect") {
        @Override
        public void actionPerformed(ActionEvent e) {
            connecting();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    listener.connect(nickname.getText(), peerAddress.getText(), ConnectionForm.this);
                }
            }).start();
        }
    };

    private final JTextField peerAddress = new JTextField();
    private final JFrame frame = new JFrame("Connect");
    private final JTextField nickname = new JTextField();
    private final JButton connectButton = new JButton(connectAction);

    private ConnectionFormListener listener;

    public ConnectionForm() {
        JPanel fields = new JPanel(new GridLayout(4, 1, 0, SPACING));
        fields.add(new JLabel("Your nickname"));
        fields.add(nickname);
        fields.add(new JLabel("Peer address"));
        fields.add(peerAddress);

        JPanel buttons = new JPanel();
        buttons.add(connectButton);
        buttons.setPreferredSize(new Dimension(200, buttons.getPreferredSize().height));

        JPanel contentPane = new JPanel(new BorderLayout(SPACING, SPACING));
        contentPane.add(fields, BorderLayout.CENTER);
        contentPane.add(buttons, BorderLayout.SOUTH);
        contentPane.setBorder(BorderFactory.createEmptyBorder(SPACING, SPACING, SPACING, SPACING));

        frame.setContentPane(contentPane);
        frame.setLocationRelativeTo(null);
        frame.pack();
    }

    public ConnectionForm listener(ConnectionFormListener listener) {
        this.listener = listener;
        return this;
    }

    public void show() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                frame.setVisible(true);
            }
        });
    }

    private void connecting() {
        connectButton.setEnabled(false);
        connectButton.setText("Connecting...");
    }

    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }
}
