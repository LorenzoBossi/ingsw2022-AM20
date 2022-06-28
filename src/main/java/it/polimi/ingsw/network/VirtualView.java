package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;
import it.polimi.ingsw.utils.EventObserver;

import java.util.Map;

/**
 * Class VirtualView Receives notifications when the model is changed and forwards them to clients
 */
public class VirtualView implements EventObserver {
    private GameHandler gameHandler;

    /**
     * Constructor
     * @param gameHandler
     */
    public VirtualView(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    /**
     * Send the update message to all the clients in the lobby
     * @param message
     */
    @Override
    public void update(ServerMessage message) {
        gameHandler.sendMessageToLobby(message);
    }
}
