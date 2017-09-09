package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Артем on 09.09.2017.
 */
public class ChatServer implements Runnable {
    private ServerSocket serverSocket;

    private List<ChatServerClient> clients =
            Collections.synchronizedList(new ArrayList<>());

    public ChatServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket accept = this.serverSocket.accept();
                System.out.println("Connect new user");
                ChatServerClient client =
                        new ChatServerClient(accept, this);
            } catch (IOException e) {
                System.out.println("Connect new user error");
                e.printStackTrace();
            }
        }
    }

    public List<ChatServerClient> getClients() {
        return clients;
    }
}
