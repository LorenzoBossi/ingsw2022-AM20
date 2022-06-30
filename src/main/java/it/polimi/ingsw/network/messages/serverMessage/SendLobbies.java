package it.polimi.ingsw.network.messages.serverMessage;

import java.util.Map;

/**
 * Message sent by the server to notify the client with the attending lobbies
 */
public class SendLobbies implements ServerMessage {
    private final Map<Integer, Integer> attendingLobbiesNumberPlayerMap;
    private final Map<Integer, String> attendingLobbiesGameModeMap;


    /**
     * Constructor
     * @param attendingLobbiesNumberPlayerMap the attending lobbies number of player map
     * @param attendingLobbiesGameModeMap the attending lobbies game mode map
     */
    public SendLobbies(Map<Integer, Integer> attendingLobbiesNumberPlayerMap, Map<Integer, String> attendingLobbiesGameModeMap) {
        this.attendingLobbiesNumberPlayerMap = attendingLobbiesNumberPlayerMap;
        this.attendingLobbiesGameModeMap = attendingLobbiesGameModeMap;
    }

    /**
     * Gets the attending lobbies number of player map
     * @return the attending lobbies number of player map
     */
    public Map<Integer, Integer> getAttendingLobbiesNumberPlayerMap() {
        return attendingLobbiesNumberPlayerMap;
    }

    /**
     * Gets the attending lobbies game mode map
     * @return the attending lobbies game mode map
     */
    public Map<Integer, String> getAttendingLobbiesGameModeMap() {
        return attendingLobbiesGameModeMap;
    }
}
