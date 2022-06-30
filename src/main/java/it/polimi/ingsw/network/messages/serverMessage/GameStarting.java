package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.TowerColor;

import java.util.List;
import java.util.Map;

/**
 * Message sent by the server when the game starts
 */
public class GameStarting implements ServerMessage {
    private final Map<String, TowerColor> towerColorMap;
    private final List<String> players;
    private final String gameMode;

    /**
     * Constructor
     * @param towerColorMap the player map of the tower color
     * @param players the players
     * @param gameMode the game mode
     */
    public GameStarting(Map<String, TowerColor> towerColorMap, List<String> players, String gameMode) {
        this.towerColorMap = towerColorMap;
        this.players = players;
        this.gameMode = gameMode;
    }

    /**
     * Gets the player map of the tower color
     * @return the player map of the tower color
     */
    public Map<String, TowerColor> getTowerColorMap() {
        return towerColorMap;
    }

    /**
     * Gets the players
     * @return the players
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Gets the game mode
     * @return the game mode
     */
    public String getGameMode() {
        return gameMode;
    }
}
