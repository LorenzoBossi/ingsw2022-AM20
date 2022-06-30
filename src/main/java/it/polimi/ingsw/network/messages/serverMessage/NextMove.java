package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the server to notify that the current player can do the next move
 */
public class NextMove implements ServerMessage {
    private final String currPlayer;

    /**
     * Constructor
     * @param currPlayer the current player
     */
    public NextMove(String currPlayer) {
        this.currPlayer = currPlayer;
    }

    /**
     * Gets the current player
     * @return the current player
     */
    public String getCurrPlayer() {
        return currPlayer;
    }
}
