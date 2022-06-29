package it.polimi.ingsw.model;

/**
 * Class used to notify the EndObserver and handle the end game logic
 */
public class EndGameObserver {
    private EndObserver game;

    /**
     * Construction of the class
     * @param game The game to observe
     */
    public EndGameObserver(EndObserver game) {
        this.game = game;
    }

    /**
     * When called it ends the game observed immediately
     */
    public void notifyEndGame(){
        game.end();
    }
    /**
     * When called it ends the game observed only at the end of the current round
     */
    public void notifyLastRound(){
        game.lastRound();
    }
}
