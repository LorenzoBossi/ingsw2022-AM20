package it.polimi.ingsw.utils;

import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;

public interface EventObserver {

    /**
     * Notify the update
     * @param message
     */
    public void update(ServerMessage message);

}
