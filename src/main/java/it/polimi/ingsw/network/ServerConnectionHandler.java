package it.polimi.ingsw.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class ServerConnectionHandler manages the connection of clients
 */
public class ServerConnectionHandler implements Runnable {
    private ServerSocket serverSocket;
    private int serverPort;
    private Server server;
    private boolean stop = false;
    private ExecutorService executorService = Executors.newCachedThreadPool();

    /**
     * Constructor
     * @param serverPort the port of the server
     * @param server the server
     */
    public ServerConnectionHandler(int serverPort, Server server) {
        try {
            serverSocket = new ServerSocket(serverPort);
            System.out.println("Server running on IP : " + serverSocket.getInetAddress());
        } catch (IOException e) {
            System.err.println("GameError during the ServerSocket creation");
            close();
            e.printStackTrace();
        }
        this.serverPort = serverPort;
        this.server = server;
    }

    /**
     * Accepts the client connection
     * @throws IOException launch by the serverSocket accept
     */
    public void acceptClientConnection() throws IOException {
        System.out.println("Server ready to accept connection...");
        Socket socket = serverSocket.accept();
        socket.setSoTimeout(5000);
        System.out.println("New Connection");
        ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler(server, socket);
        executorService.submit(clientConnectionHandler);
        executorService.submit(new PingHandler(clientConnectionHandler));
    }

    /**
     * Close the executorService and close the server Socket
     */
    private void close() {
        if(!stop) {
            stop = true;
            executorService.shutdown();
            try {
                serverSocket.close();
            } catch (IOException e) {
                System.err.println("GameError during closing server socket");
                System.exit(-1);
                e.printStackTrace();
            }
        }
    }

    /**
     * Continues to accept client connection
     */
    @Override
    public void run() {
        while (!stop) {
            try {
                acceptClientConnection();
            } catch (IOException e) {
                System.err.println("GameError during accepting client connection");
                close();
                System.exit(-1);
                e.printStackTrace();
            }
        }
    }
}
