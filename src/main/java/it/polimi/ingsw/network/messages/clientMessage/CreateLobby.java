package it.polimi.ingsw.network.messages.clientMessage;

public class CreateLobby implements ClientMessage {
    private int numberOfPlayers;
    private String gameMode;

    public CreateLobby(int numberOfPlayers, String gameMode) {
        this.numberOfPlayers = numberOfPlayers;
        this.gameMode = gameMode;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public String getGameMode() {
        return gameMode;
    }
}
