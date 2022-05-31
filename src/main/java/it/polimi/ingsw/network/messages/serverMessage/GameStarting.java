package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.TowerColor;

import java.util.List;
import java.util.Map;

public class GameStarting implements ServerMessage {
    private Map<String, TowerColor> towerColorMap;
    private List<String> players;
    private String gameMode;

    public GameStarting(Map<String, TowerColor> towerColorMap, List<String> players, String gameMode) {
        this.towerColorMap = towerColorMap;
        this.players = players;
        this.gameMode = gameMode;
    }

    public Map<String, TowerColor> getTowerColorMap() {
        return towerColorMap;
    }

    public List<String> getPlayers() {
        return players;
    }

    public String getGameMode() {
        return gameMode;
    }
}
