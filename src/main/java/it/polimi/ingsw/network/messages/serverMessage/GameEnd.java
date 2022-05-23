package it.polimi.ingsw.network.messages.serverMessage;

public class GameEnd implements ServerMessage{
    private String winner;
    private boolean isADraw;

    public GameEnd(String winnerNickName) {
        this.winner=winnerNickName;
        this.isADraw=false;
    }
    public GameEnd() {
        this.winner="";
        this.isADraw=true;
    }

    public String getWinner() {
        return winner;
    }

    public boolean isADraw() {
        return isADraw;
    }
}
