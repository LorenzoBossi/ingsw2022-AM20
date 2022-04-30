package it.polimi.ingsw.client;

import it.polimi.ingsw.network.messages.clientMessage.ClientMessage;
import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerConnection implements Runnable{
    private String serverIp;
    private int serverPort;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ServerMessageHandler messageHandler;
    private boolean status = true;
    private Socket socket;

    public ServerConnection(String serverIp, int serverPort, ServerMessageHandler messageHandler) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.messageHandler = messageHandler;
    }

    public void setupConnection() {
        try {
            socket = new Socket(serverIp, serverPort);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            System.err.println("GameError during the connection to server");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void receiveFromServer() {
        try {
            ServerMessage message = (ServerMessage) inputStream.readObject();
            messageHandler.handleMessage(message);
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Server Connection Lost...");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void sendMessageToServer(ClientMessage message) {
        try {
            outputStream.reset();
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("GameError in sending message to Server");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("GameError in closing connection to server");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(status) {
            receiveFromServer();
        }
        close();
    }
}
