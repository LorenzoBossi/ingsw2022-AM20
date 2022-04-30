package it.polimi.ingsw.network;

import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;
import it.polimi.ingsw.utils.EventObserver;

import java.util.Map;

public class VirtualView implements EventObserver {
    GameHandler gameHandler;

    public VirtualView(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
    }

    @Override
    public void update(ServerMessage message) {
        gameHandler.sendMessageToLobby(message);
    }
}
