package it.polimi.ingsw.model;

import java.util.List;

/**
 * Standard strategy to evaluate players influence on islands
 */
public class StandardInfluence implements InfluenceStrategy{
    Player currPlayer;

    /**
     * Constructor
     */
    public StandardInfluence(){
        super();
        currPlayer = null;
    }

    /**
     * Sets the current player of the game
     */
    @Override
    public void setCurrPlayer(Player currPlayer) {
        this.currPlayer = currPlayer;
    }


    /**
     * Method calculateInfluence calculates the player's influence on the specified island
     *
     * @param selectedPlayer the specified player
     * @param island the specified island
     * @param professorManager the professorsManager to check the professor owned by the player
     * @return the player's influence on the specified island
     */
    @Override
    public int calculateInfluence(Player selectedPlayer, Island island, ProfessorManager professorManager) {
        List<Color> professorsOwned = professorManager.getProfessorsOwnedBy(selectedPlayer);
        int influence = 0;
        for(Color c: professorsOwned)
                influence += island.getSelectedStudents(c);
        if(selectedPlayer.equals(island.getOwner()))
            influence += island.getNumberOfTowers();
        return influence;
    }

}
