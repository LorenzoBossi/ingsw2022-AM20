package it.polimi.ingsw.network.messages.serverMessage;

import java.util.List;

public class GameStarting implements ServerMessage {
    private List<String> players;
    private String gameMode;

    public GameStarting(List<String> players, String gameMode) {
        this.players = players;
        this.gameMode = gameMode;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getGameMode() {
        return gameMode;
    }
}
