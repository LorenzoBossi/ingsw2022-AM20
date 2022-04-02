package it.polimi.ingsw.model;

import java.util.List;

public class NoColorInfluence implements InfluenceStrategy{
    Player currPlayer;


    /**
     * Constructor
     * @param currPlayer the player that activates the effect
     */
    public NoColorInfluence(Player currPlayer){
        super();
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
        for (Color c : professorsOwned) {
            if (c != currPlayer.getPlayerChoice().getSelectedColor())
                influence += island.getSelectedStudents(c);
        }
        if (selectedPlayer.equals(island.getOwner()))
            influence += island.getNumberOfTowers();
        return influence;
    }
}
