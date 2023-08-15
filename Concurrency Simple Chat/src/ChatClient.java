import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Multithreaded tcp chat client
 */
public class ChatClient {

    // Client socket
    private Socket socket;

    /**
     * Simple Chat Client constructor
     * @param host ip address
     * @param port port number
     */
    public ChatClient(String host, int port) {

        System.out.println("Connecting, please wait...");

        try {

            // Connect to server
            this.socket = new Socket(host, port);
            System.out.println("Connected to: " + socket);
            start();

        } catch (IOException ex) {

            System.out.println(ex.getMessage());
            System.exit(1);

        }
    }

    // Messages
    private void start() {

        // Creates a new thread to handle incoming server messages
        ChatClientWorker chatClientWorker = new ChatClientWorker();
        chatClientWorker.setSocket(socket);
        Thread thread = new Thread(chatClientWorker);
        thread.start();

        try {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

            while (!socket.isClosed()) {

                String message = null;

                try {

                    // Waiting for user input
                    message = in.readLine();

                } catch (IOException ex) {
                    System.out.println("Error reading from console: " + ex.getMessage());
                    break;
                }

                if (message == null || message.equals("/quit")) {
                    break;
                }


                out.write(message);
                out.newLine();
                out.flush();

            }

            try {

                in.close();
                out.close();
                socket.close();

            } catch (IOException ex) {
                System.out.println("Error closing connection: " + ex.getMessage());
            }

        } catch (IOException ex) {

            System.out.println("Error sending message to server: " + ex.getMessage());

        }
    }

    /**
     * Start the Concurrency Simple Chat Client
     * @param args
     */
    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = Integer.parseInt("8000");

        System.out.println("Java Simple Chat Client:\nHost " + host + "\nPort " + port);

        new ChatClient(host, port);
    }
}
