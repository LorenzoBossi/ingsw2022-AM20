package it.polimi.ingsw.utils;

import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;

public interface EventObserver {

    public void update(ServerMessage message);

}
