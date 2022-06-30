package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the server to notify the end of the game
 */
public class GameEnd implements ServerMessage {
    private final String winner;
    private final boolean isADraw;

    /**
     * Constructor
     * @param winnerNickName the nickname of the winner
     */
    public GameEnd(String winnerNickName) {
        this.winner = winnerNickName;
        this.isADraw = false;
    }

    /**
     * Constructor when there is a draw
     */
    public GameEnd() {
        this.winner = "";
        this.isADraw = true;
    }

    /**
     * Gets the winner
     * @return the winner
     */
    public String getWinner() {
        return winner;
    }

    /**
     * Gets if there is a draw
     * @return true if there is a draw, false otherwise
     */
    public boolean isADraw() {
        return isADraw;
    }
}
