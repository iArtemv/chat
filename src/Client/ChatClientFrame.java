package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Артем on 09.09.2017.
 */
public class ChatClientFrame extends JFrame implements ChatClient.ReceiveMessage{
    private JTextArea messageTextArea;
    private JTextField sendTextField;
    private JButton sendButton;

    private String login;

    private ChatClientLoginFrame loginFrame;

    private ChatClient chatClient;

    public ChatClientFrame(String login, ChatClientLoginFrame loginFrame, ChatClient chatClient) throws HeadlessException {
        this.login = login;
        this.loginFrame = loginFrame;
        this.chatClient = chatClient;

        this.chatClient.setReceiveMessage(this);

        this.setTitle(String.format("Login:%s", this.login));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 20);
        Color color = new Color(193, 193, 200);

        this.messageTextArea = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(this.messageTextArea);
        JPanel sendPanel = new JPanel(new BorderLayout());
        this.sendTextField = new JTextField();
        this.sendButton = new JButton("SEND");

        this.messageTextArea.setFont(font);
        this.sendTextField.setFont(font);
        this.sendButton.setFont(font);

        this.messageTextArea.setBackground(color);
        this.messageTextArea.setDisabledTextColor(Color.BLACK);
        this.sendTextField.setBackground(color);

        this.messageTextArea.setEditable(false);
        this.messageTextArea.setLineWrap(true);
        this.messageTextArea.setWrapStyleWord(true);

        sendPanel.add(this.sendTextField, BorderLayout.CENTER);
        sendPanel.add(this.sendButton, BorderLayout.EAST);

        this.sendTextField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendText();
            }
        });
        this.sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendText();
            }
        });

        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(sendPanel, BorderLayout.PAGE_END);
    }

    private void sendText() {
        String text = this.sendTextField.getText();
        if (text.length() == 0) {
            return;
        }
        try {
            this.chatClient.sendMessage(text);
        } catch (IOException e) {
            error("Connection to server error. Try later.");
            this.loginFrame.setVisible(true);
            this.setVisible(false);
        }
        this.sendTextField.setText("");
    }

    @Override
    public synchronized void receiveMessage(String message) {
        this.messageTextArea.append(message);
        this.messageTextArea.append("\n");
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
