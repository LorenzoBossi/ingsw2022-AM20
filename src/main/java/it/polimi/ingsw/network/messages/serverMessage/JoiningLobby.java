package it.polimi.ingsw.network.messages.serverMessage;

/**
 * Message sent by the server to notify when a client joins one lobby
 */
public class JoiningLobby implements ServerMessage{
    private final String joiningPlayer;
    private final int playerRemainingToStartTheGame;

    /**
     * Constructor
     * @param joiningPlayer the name of the joining player
     * @param playerRemainingToStartTheGame the players remaining to start the game
     */
    public JoiningLobby(String joiningPlayer, int playerRemainingToStartTheGame) {
        this.joiningPlayer = joiningPlayer;
        this.playerRemainingToStartTheGame = playerRemainingToStartTheGame;
    }

    /**
     * Gets the players remaining to start the game
     * @return the players remaining to start the game
     */
    public int getPlayerRemainingToStartTheGame() {
        return playerRemainingToStartTheGame;
    }

    /**
     * Gets the name of the joining player
     * @return the name of the joining player
     */
    public String getJoiningPlayer() {
        return joiningPlayer;
    }
}
