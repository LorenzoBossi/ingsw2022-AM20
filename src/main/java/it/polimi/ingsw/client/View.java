package it.polimi.ingsw.client;

import it.polimi.ingsw.model.TowerColor;

import java.util.List;
import java.util.Map;

public interface View {

    public void lobbySetup(Map<Integer, Integer> attendingLobbiesNumberOfPlayerMap, Map<Integer, String> attendingLobbiesGameModeMap);

    public void startGame(List<String> players, String gameMode, Map<String, TowerColor> towerColorMap);

    public void pianificationPhase(String targetPlayer);

    public void actionPhase(String targetPlayer);

    public void nicknameSetup();

    public void lobbyRefresh();

    public String getClientNickname();

    public void endGame(boolean aDraw, String winner);
}
