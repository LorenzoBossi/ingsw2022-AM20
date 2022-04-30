package it.polimi.ingsw.network.messages.clientMessage;

public class JoinLobby implements ClientMessage {
    private int lobbyId;

    public JoinLobby(int lobbyId) {
        this.lobbyId = lobbyId;
    }

    public int getLobbyId() {
        return lobbyId;
    }
}
