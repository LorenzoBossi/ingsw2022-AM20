package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.serverMessage.Ping;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerConnectionHandler implements Runnable {
    private ServerSocket serverSocket;
    private int serverPort;
    private Server server;
    private boolean stop = false;
    private ExecutorService executorService = Executors.newCachedThreadPool();

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

    public void acceptClientConnection() throws IOException {
        System.out.println("Server ready to accept connection...");
        Socket socket = serverSocket.accept();
        System.out.println("New Connection");
        ClientConnectionHandler clientConnectionHandler = new ClientConnectionHandler(server, socket);
        executorService.submit(clientConnectionHandler);
        //executorService.submit(new PingHandler(clientConnectionHandler));
    }

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
