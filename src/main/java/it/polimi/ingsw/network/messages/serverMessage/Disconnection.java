package it.polimi.ingsw.network.messages.serverMessage;

public class Disconnection implements ServerMessage {
    private String playerDisconnected;

    public Disconnection(String playerDisconnected) {
        this.playerDisconnected = playerDisconnected;
    }


    public String getPlayerDisconnected() {
        return playerDisconnected;
    }
}
