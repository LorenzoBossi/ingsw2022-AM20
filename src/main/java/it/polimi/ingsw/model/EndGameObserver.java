package it.polimi.ingsw.model;

public class EndGameObserver {
    private Game game;

    public EndGameObserver(Game game) {
        this.game = game;
    }
    public void notifyEndGame(){
        game.end();
    }
    public void notifyLastRound(){
        game.lastRound();
    }
}
