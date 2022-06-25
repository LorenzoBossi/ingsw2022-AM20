package it.polimi.ingsw.client;

import it.polimi.ingsw.network.messages.clientMessage.ClientMessage;
import it.polimi.ingsw.network.messages.clientMessage.Pong;
import it.polimi.ingsw.network.messages.serverMessage.Disconnection;
import it.polimi.ingsw.network.messages.serverMessage.Ping;
import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class ServerConnection handles the connection between Client and Server
 */
public class ServerConnection implements Runnable {
    private String serverIp;
    private int serverPort;

    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    private BlockingQueue<ServerMessage> queue = new ArrayBlockingQueue<>(1000);

    private ServerMessageHandler messageHandler;

    private boolean status = true;
    private Socket socket;

    /**
     * Constructor
     *
     * @param serverIp       the Server's Ip address
     * @param serverPort     the Server's port
     * @param messageHandler the messageHandler's Server message
     */
    public ServerConnection(String serverIp, int serverPort, ServerMessageHandler messageHandler) {
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.messageHandler = messageHandler;
    }

    /**
     * Method setupConnection setups the connection with Server
     */
    public boolean setupConnection() {
        try {
            socket = new Socket(serverIp, serverPort);
            socket.setSoTimeout(20000);
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            return true;
        } catch (IOException e) {
            System.err.println("Unable to reach the server at: ("+serverIp+ " , "+serverPort+")");
            e.printStackTrace();
            System.exit(-1);//da togliere per chiedere nuovi ip e port
            return false;//per capire che non è andato a buon fine
        }
    }

    /**
     * Method receiveFromServer receives the message comes from the Server
     */
    public void receiveFromServer() {
        try {
            ServerMessage message = (ServerMessage) inputStream.readObject();
            if (message instanceof Disconnection) {
                messageHandler.Disconnection();
                close();
            } else if (message instanceof Ping) {
                sendMessageToServer(new Pong());
            } else {
                queue.add(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Server Connection Lost...");
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void consumeMessage() {
        while (isConnected()) {
            try {
                messageHandler.handleMessage(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method sendMessageToServer sends the Client message to the Server
     *
     * @param message the message that the client sends to the Server
     */
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

    /**
     * This method closes the connection with the server and close the streams
     */
    public void close() {
        try {
            status = false;
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            System.err.println("GameError in closing connection to server");
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return status;
    }

    /**
     * Method run continues to receive message from the server
     */
    @Override
    public void run() {
        new Thread(this::consumeMessage).start();
        while (status) {

            receiveFromServer();
        }
        close();
    }
}
