package Server;

import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Артем on 09.09.2017.
 */
public class App {
    public static void main(String[] args) throws IOException {
        ChatServer chatServer;
        chatServer = new ChatServer(9999);
        Thread thread = new Thread(chatServer);
        thread.start();
        Scanner scanner = new Scanner(System.in);
        String fromConsole;
        while (true) {
            System.out.println("Enter 'q' for exit");
            fromConsole = scanner.nextLine();
            if ("q".equals(fromConsole.trim().toLowerCase())){
                System.exit(0);
            }
        }
    }
}
