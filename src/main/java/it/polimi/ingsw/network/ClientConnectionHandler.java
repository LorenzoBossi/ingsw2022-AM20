package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.clientMessage.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessage.Pong;
import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientConnectionHandler implements Runnable {
    private Server server;
    private Socket socket;
    private String nickname;

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private boolean stop = false;

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

    public boolean isConnected() {
        return !stop;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public synchronized void receiveMessage() {
        ClientMessage message = null;
        try {
            message = (ClientMessage) inputStream.readObject();
        } catch (IOException e) {
            System.err.println(nickname + " disconnection");
            if (nickname != null)
                server.closeLobby(nickname);
            closeConnection();
            //e.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found");
            closeConnection();
            //e.printStackTrace();
        }
        if (message instanceof Pong)
            System.out.println("Pong");
        else {
            server.messageDispatcher(message, this);
        }
    }

    public void sendMessageToClient(ServerMessage serverMessage) {
        try {
            outputStream.reset();
            outputStream.writeObject(serverMessage);
            outputStream.flush();
        } catch (IOException e) {
            System.err.println("GameError during sending message to client");
            closeConnection();
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        while (!stop) {
            receiveMessage();
        }
        closeConnection();
    }

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
