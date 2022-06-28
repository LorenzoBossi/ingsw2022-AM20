package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.serverMessage.Ping;

import java.util.concurrent.TimeUnit;

/**
 * Class PingHandler send each second a Ping message to the Client
 */
public class PingHandler implements Runnable{
    private ClientConnectionHandler clientConnectionHandler;

    /**
     * Constructor
     * @param clientConnectionHandler the connection to the client
     */
    public PingHandler(ClientConnectionHandler clientConnectionHandler) {
        this.clientConnectionHandler = clientConnectionHandler;
    }

    /**
     * Method run send Ping messages to the client each second as long as it is connected
     */
    @Override
    public void run() {
        while(clientConnectionHandler.isConnected()) {
            clientConnectionHandler.sendMessageToClient(new Ping());
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
