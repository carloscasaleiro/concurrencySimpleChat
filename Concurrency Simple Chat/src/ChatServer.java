import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Multithreaded tcp chat server
 */
public class ChatServer {

    /**
     * Default port
     */
    public static final int PORT = 8000;

    /**
     * Synchronized List of worker threads
     */
    private List<ChatServerWorker> clients = Collections.synchronizedList(new ArrayList<>());

    /**
     * Managing threads
     */
    ExecutorService cachedPool = Executors.newCachedThreadPool();

    /**
     * Bootstraps the chat server
     */
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
        chatServer.listen(PORT);
    }

    /**
     * Starts the chat server on a specified port
     *
     * @param port the tcp port to bind to
     */
    private void listen(int port) {

        System.out.println("Server is : " + this);

        int clientCount = 0;

        try {

            // Bind to local port
            System.out.println("Binding to port " + port + ", please wait  ...");
            ServerSocket bindSocket = new ServerSocket(port);
            System.out.println("Server started: " + bindSocket);

            //Wait for clients
            System.out.println("Trying to establishing the connection, please wait...");
            acceptClient(bindSocket);

        } catch (IOException e) {
            System.out.println("Unable to start server on port " + port);
        }
    }

    /**
     * Accepts client connections and instantiates client workers
     *
     * @param bindSocket the tcp port to bind to
     */
    public void acceptClient(ServerSocket bindSocket) {

        while (true) {

            try {

                // Block waiting for client connections
                Socket clientSocket = bindSocket.accept();
                System.out.println("Client accepted: " + clientSocket);

                ChatServerWorker serverWorker = new ChatServerWorker(clientSocket, this);
                System.out.println("Client accepted: " + clientSocket);
                cachedPool.submit(serverWorker);
                clients.add(serverWorker);

            } catch (IOException ex) {
                System.out.println("Error receiving client connection: " + ex.getMessage());
            }
        }
    }

    /**
     * Broadcast a message to all server connected clients
     *
     * @param message the message to broadcast
     * @param port port of the client that the message originated from
     */
    public void broadcast(String message, int port) {

        for (ChatServerWorker x : clients) {

            if(!(x.getPort() == port)) {
                x.receivedMessage(message);
            }
        }
    }

    /**
     * Broadcast a message to a specific client
     *
     * @param message the message to broadcast
     * @param name name of the destiny client
     */
    public void privateSend(String message, String name) {

        for (ChatServerWorker x : clients) {

            if(x.getName().equals(name)) {
                x.receivedMessage(message);
            }
        }
    }

    public List<ChatServerWorker> getClients() {
        return clients;
    }
}
