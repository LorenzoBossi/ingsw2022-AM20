package it.polimi.ingsw.model;

/**
 * Interface that represents object which can observe other objects to understand if the game is ended
 */
public interface EndObserver {
    /**
     * Method called when the game must end
     */
    void end();

    /**
     * Method called when the game will end at the end of the current Round
     */
    void lastRound();
}
