package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Message sent by the client to create a lobby
 */
public class CreateLobby implements ClientMessage {
    private final int numberOfPlayers;
    private final String gameMode;

    /**
     * Constructor
     * @param numberOfPlayers the number of players in the lobby
     * @param gameMode the game mode choose by the client
     */
    public CreateLobby(int numberOfPlayers, String gameMode) {
        this.numberOfPlayers = numberOfPlayers;
        this.gameMode = gameMode;
    }

    /**
     * Gets the number of players in the lobby
     * @return the number of players in the lobby
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Gets the gameMode of the lobby
     * @return the gameMode
     */
    public String getGameMode() {
        return gameMode;
    }
}
