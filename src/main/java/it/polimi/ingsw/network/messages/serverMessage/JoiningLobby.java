package it.polimi.ingsw.network.messages.serverMessage;

public class JoiningLobby implements ServerMessage{
    private String joiningPlayer;
    private int playerRemainingToStartTheGame;

    public JoiningLobby(String joiningPlayer, int playerRemainingToStartTheGame) {
        this.joiningPlayer = joiningPlayer;
        this.playerRemainingToStartTheGame = playerRemainingToStartTheGame;
    }

    public int getPlayerRemainingToStartTheGame() {
        return playerRemainingToStartTheGame;
    }

    public String getJoiningPlayer() {
        return joiningPlayer;
    }
}
