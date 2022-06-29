package it.polimi.ingsw.model;

/**
 * Interface of classes which can determines how to calculate influence on islands ( strategy pattern ).
 */
public interface InfluenceStrategy {

    /**
     * Method calculateInfluence calculates the player's influence on the specified island
     *
     * @param player the specified player
     * @param island the specified island
     * @param professorManager the professorsManager to check the professor owned by the player
     * @return the player's influence on the specified island
     */
    int calculateInfluence(Player player, Island island, ProfessorManager professorManager);

    /**
     * Sets the current player, which is usefull to know for some cards.
     * @param currPlayer
     */
    void setCurrPlayer(Player currPlayer);

}
