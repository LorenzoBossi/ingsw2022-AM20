package it.polimi.ingsw.model;

public class EndGameObserver {
    private EndObserver game;

    public EndGameObserver(EndObserver game) {
        this.game = game;
    }
    public void notifyEndGame(){
        game.end();
    }
    public void notifyLastRound(){
        game.lastRound();
    }
}
