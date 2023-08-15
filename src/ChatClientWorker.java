import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Accepts Incoming Messages
 */
public class ChatClientWorker implements Runnable{

    private Socket socket;


    @Override
    public void run() {

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (!socket.isClosed()) {

                // Waiting for incoming messages from server
                String message = br.readLine();

                if (message != null) {

                    System.out.println(message);

                } else {

                    try {

                        System.out.println("Connection closed, exiting...");
                        br.close();
                        socket.close();

                    } catch (IOException ex) {
                        System.out.println("Error closing connection: " + ex.getMessage());
                    }
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading from server: " + ex.getMessage());
        }

        // Server closed
        System.exit(0);

    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
}
