package it.polimi.ingsw.network.messages.serverMessage;

import java.util.List;
import java.util.Map;

public class SendLobbies implements ServerMessage {
    Map<Integer, Integer> attendingLobbiesNumberPlayerMap;
    Map<Integer, String> attendingLobbiesGameModeMap;


    public SendLobbies(Map<Integer, Integer> attendingLobbiesNumberPlayerMap, Map<Integer, String> attendingLobbiesGameModeMap) {
        this.attendingLobbiesNumberPlayerMap = attendingLobbiesNumberPlayerMap;
        this.attendingLobbiesGameModeMap = attendingLobbiesGameModeMap;
    }

    public Map<Integer, Integer> getAttendingLobbiesNumberPlayerMap() {
        return attendingLobbiesNumberPlayerMap;
    }

    public Map<Integer, String> getAttendingLobbiesGameModeMap() {
        return attendingLobbiesGameModeMap;
    }
}
