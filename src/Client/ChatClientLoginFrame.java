package Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * Created by Артем on 09.09.2017.
 */
public class ChatClientLoginFrame extends JFrame {
    private JTextField loginTextField;

    private JLabel loginLabel;

    private JTextField serverAddressTextField;

    private JLabel serverAddressLabel;

    private JButton loginButton;

    public ChatClientLoginFrame() throws HeadlessException {
        ChatClientLoginFrame loginFrame = this;

        this.setTitle("Login");
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        this.setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 20);

        this.loginTextField = new JTextField();
        this.loginLabel = new JLabel("Login:");
        this.serverAddressTextField = new JTextField();
        this.serverAddressLabel = new JLabel("Server address:");
        this.loginButton = new JButton("Login");

        this.loginButton.setFont(font);
        this.loginLabel.setFont(font);
        this.loginTextField.setFont(font);
        this.serverAddressTextField.setFont(font);
        this.serverAddressLabel.setFont(font);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(2, 2));

        jPanel.add(this.loginLabel);
        jPanel.add(this.loginTextField);
        jPanel.add(this.serverAddressLabel);
        jPanel.add(this.serverAddressTextField);

        this.add(jPanel, BorderLayout.CENTER);
        this.add(this.loginButton, BorderLayout.SOUTH);

        this.loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) throws RuntimeException {
                String login = null;
                String host = null;
                Integer port = null;

                login = loginTextField.getText().trim();
                if (login.length() == 0) {
                    error("Please enter login");
                    return;
                }
                String address = serverAddressTextField.getText().trim();
                if (address.length() == 0) {
                    error("Please enter server host and port");
                    return;
                }
                if (address.indexOf(':') < 1) {
                    error("Please enter server port after ':' example: localhost:9090");
                    return;
                }
                String[] split = address.split(":");
                host = split[0];
                try {
                    port = Integer.valueOf(split[1]);
                } catch (RuntimeException ex) {
                    error("Please enter server port after ':' example: localhost:9090");
                    return;
                }
                ChatClient chatClient;
                try {
                    chatClient = new ChatClient(host, port);
                } catch (IOException e1) {
                    error(String.format("Can't connect to server by adress=[%s:%s]", host, port));
                    return;
                }
                try {
                    if (!chatClient.login(login)) {
                        error(String.format("Can't connect to server by login=[%s]", login));
                        return;
                    }
                } catch (IOException e1) {
                    error("Connection error. Try later.");
                    return;
                }
                ChatClientFrame chatClientFrame = new ChatClientFrame(login, loginFrame, chatClient);
                chatClientFrame.setVisible(true);
                loginFrame.setVisible(false);
            }
        });
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
