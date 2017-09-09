package Server;

import java.io.*;
import java.net.Socket;

/**
 * Created by Артем on 09.09.2017.
 */
public class ChatServerClient implements Runnable {
    private Socket socket;
    private ChatServer server;

    private BufferedReader reader;
    private BufferedWriter writer;

    private String login;

    public ChatServerClient(Socket socket, ChatServer chatServer) throws IOException {
        this.socket = socket;
        this.server = chatServer;
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        //login
        this.login = this.reader.readLine();
        System.out.println(String.format("Try add new user=[%s]", this.login));
        if (chatServer.getClients().contains(this)) {
            System.out.println(String.format("User already exist by login=[%s]", this.login));
            writeMessage("user by login already exist");
            close();
        } else {
            System.out.println(String.format("Add new user=[%s]", this.login));
            writeMessage("ok");
            writeMessageAll(String.format("Hello, add new user=[%s]", this.login));
            this.server.getClients().add(this);
            Thread thread = new Thread(this);
            thread.start();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = this.reader.readLine();
                System.out.println(String.format("user=[%s] write message=[%s]", this.login, message));
                writeMessageAll(String.format("%s: %s", this.login, message));
            } catch (IOException e) {
                System.out.println(String.format("user=[%s] disconnect", this.login));
                close();
                this.server.getClients().remove(this);
                break;
            }
        }
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
        }
        try {
            this.reader.close();
        } catch (IOException e) {
        }
        try {
            this.writer.close();
        } catch (IOException e) {
        }
    }

    private void writeMessageAll(String message) throws IOException {
        for (ChatServerClient client : this.server.getClients()) {
            client.writeMessage(message);
        }
    }

    public void writeMessage(String message) throws IOException {
        this.writer.write(message);
        this.writer.newLine();
        this.writer.flush();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatServerClient client = (ChatServerClient) o;
        return login != null ? login.equals(client.login) : client.login == null;
    }

    @Override
    public int hashCode() {
        return login != null ? login.hashCode() : 0;
    }
}
