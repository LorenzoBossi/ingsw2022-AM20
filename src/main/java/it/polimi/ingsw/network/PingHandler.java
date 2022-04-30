package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.serverMessage.Ping;

import java.util.concurrent.TimeUnit;

public class PingHandler implements Runnable{
    private ClientConnectionHandler clientConnectionHandler;

    public PingHandler(ClientConnectionHandler clientConnectionHandler) {
        this.clientConnectionHandler = clientConnectionHandler;
    }

    @Override
    public void run() {
        while(true) {
            clientConnectionHandler.sendMessageToClient(new Ping());
            try {
                TimeUnit.MILLISECONDS.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
