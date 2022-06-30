package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.clientMessage.ClientMessage;
import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * Class ClientConnectionHandler handles the connection between the server and the client
 */
public class ClientConnectionHandler implements Runnable {
    private final Server server;
    private final Socket socket;
    private String nickname;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private boolean stop = false;

    /**
     * Constructor
     *
     * @param server the server
     * @param socket the socket to communicate with the client
     */
    public ClientConnectionHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        nickname = null;
        try {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("GameError during inputStream and outputStream initialization");
            e.printStackTrace();
        }
    }

    /**
     * Method isConnected communicate if the connection to the client is active
     * @return true if the server is connected, false otherwise
     */
    public boolean isConnected() {
        return !stop;
    }

    /**
     * Sets the nickname of the client to the socket
     * @param nickname the nickname of the client
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the client nickname
     * @return the client nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * Receives the message from the client and closes the lobby if the connection drops
     */
    public synchronized void receiveMessage() {
        ClientMessage message = null;
        try {
            message = (ClientMessage) inputStream.readObject();
        } catch (IOException e) {
            closeConnection();
            System.err.println(nickname + " disconnection");
            if (nickname != null)
                server.closeLobby(nickname);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found");
            closeConnection();
        }

        server.messageDispatcher(message, this);
    }

    /**
     * Sends the server message to the client
     * @param serverMessage the server message to be sent to the client
     */
    public void sendMessageToClient(ServerMessage serverMessage) {
        try {
            outputStream.reset();
            outputStream.writeObject(serverMessage);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("GameError during sending message to client");
            closeConnection();
        }
    }


    /**
     * Continues to receive messages from the client as long as the connection is open
     */
    @Override
    public void run() {
        while (!stop) {
            receiveMessage();
        }
    }

    /**
     * Close the connection to the client
     */
    public void closeConnection() {
        if (isConnected()) {
            stop = true;
            try {
                outputStream.close();
                inputStream.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("GameError during closing client socket");
                e.printStackTrace();
            }
        }
    }
}
