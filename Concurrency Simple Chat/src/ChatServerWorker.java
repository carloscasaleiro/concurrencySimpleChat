import java.io.*;
import java.net.Socket;

/**
 * Accepts Clients Connections
 */
public class ChatServerWorker implements Runnable{

    private Socket socket;
    private ChatServer chatServer;
    private BufferedWriter out;
    private BufferedReader in;
    private String name;

    /**
     * @param socket the client socket connection
     * @param chatServer chat server to handle messages and clients
     */
    public ChatServerWorker(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;
    }

    @Override
    public void run() {

        System.out.println("Client accepted: " + socket);

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Set Name
        try {
            setName();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Read Messages
        String line = "";

        while(!line.equals("/quit")) {

            // read the pretended message from the console
            try {
                line = in.readLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            if(!isPrivate(line)) {
                chatServer.broadcast(name + ": " + line, socket.getPort());
            } else {
                chatServer.privateSend(name + ": @Private" + line.substring(line.indexOf(" ")), line.substring(1, line.indexOf(" ")));
            }
        }
        close();
    }

    /**
     * @param message
     * @return private status of message
     */
    public boolean isPrivate(String message) {

        return message.charAt(0) == '@';
    }

    /**
     * Set client name and welcome to chat
     * @throws IOException
     */
    public void setName() throws IOException {

        try {
            out.write("Welcome!\nPlease enter your nickname: ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String line;
        line = in.readLine();
        this.name = line;

        try {
            out.write("Hello " + name + ".\n" +
                    "To leave chat enter '/quit'.\n" +
                    "To send a private message insert '@' + nickname.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void receivedMessage(String message) {

        // write the pretended message to the output buffer
        try {
            out.write(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void close() {

        System.out.println("Closing the socket");
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        chatServer.getClients().remove(this);
    }


    public int getPort() {

        return this.socket.getPort();
    }


    public String getName() {

        return this.name;
    }
}
