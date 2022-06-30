package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the server to notify one disconnection
 */
public class Disconnection implements ServerMessage {
    private final String playerDisconnected;

    /**
     * Constructor
     * @param playerDisconnected the disconnected player
     */
    public Disconnection(String playerDisconnected) {
        this.playerDisconnected = playerDisconnected;
    }


    /**
     * Gets the disconnected player
     * @return the disconnected player
     */
    public String getPlayerDisconnected() {
        return playerDisconnected;
    }
}
