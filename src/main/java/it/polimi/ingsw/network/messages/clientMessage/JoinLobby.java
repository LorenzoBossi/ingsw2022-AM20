package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Message sent by the client to join one lobby
 */
public class JoinLobby implements ClientMessage {
    private final int lobbyId;

    /**
     * Constructor
     * @param lobbyId the id of the lobby
     */
    public JoinLobby(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    /**
     * Gets the lobbyId
     * @return the lobbyId
     */
    public int getLobbyId() {
        return lobbyId;
    }
}
