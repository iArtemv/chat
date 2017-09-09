package Client;

import java.io.*;
import java.net.Socket;

/**
 * Created by Артем on 09.09.2017.
 */
public class ChatClient implements Runnable{
    public interface ReceiveMessage {
        /**
         * Получение сообщения от сервера
         *
         * @param message сообщение от сервера
         */
        void receiveMessage(String message);
    }

    private Socket socket;

    private BufferedReader reader;

    private BufferedWriter writer;

    private ReceiveMessage receiveMessage;

    /**
     * Создание соединение с сервером
     *
     * @param host адрес сервера
     * @param port порт сервера
     * @throws IOException если не получилось подключится
     */
    public ChatClient(String host, int port) throws IOException {
        this.socket = new Socket(host, port);
        this.reader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.writer = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
    }

    @Override
    public void run() {
        while (true) {
            try {
                String messge = this.reader.readLine();
                if (this.receiveMessage != null) {
                    this.receiveMessage.receiveMessage(messge);
                }
            } catch (IOException e) {
                e.printStackTrace();
                close();
                break;
            }
        }
    }

    /**
     * Регистрация пользователя на сервере <br/>
     * Если пользователь не будет зарегистрирован,
     * соединение будет закрыто
     *
     * @param login логин пользователя
     * @return зарегистрирован/не заренестрирован
     * @throws IOException разрыв соединения
     */
    public boolean login(String login) throws IOException {
        this.sendMessage(login);
        String message = this.reader.readLine();
        if (!"ok".equals(message)) {
            close();
            return false;
        }
        Thread thread = new Thread(this);
        thread.start();
        return true;
    }

    /**
     * Отправка сообщения на сервер <br/>
     * В случае ошибки, соединение будет разорвано
     *
     * @param message сообщение
     * @throws IOException разрыв соединения
     */
    public void sendMessage(String message) throws IOException {
        this.writer.write(message);
        this.writer.newLine();
        this.writer.flush();
    }

    /**
     * Закрытие соединения с сервером
     */
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

    public void setReceiveMessage(ReceiveMessage receiveMessage) {
        this.receiveMessage = receiveMessage;
    }
}
