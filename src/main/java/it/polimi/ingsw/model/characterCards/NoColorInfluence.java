package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.util.List;

public class NoColorInfluence implements InfluenceStrategy {
    Player currPlayer;


    /**
     * Constructor
     */
    public NoColorInfluence(){
        super();
        this.currPlayer = null;
    }

    /**
     * Sets the current player of the card as it is relevant for its effect
     * @param currPlayer player to set as current
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
        for (Color c : professorsOwned) {
            if (c != currPlayer.getPlayerChoice().getSelectedColor())
                influence += island.getSelectedStudents(c);
        }
        if (selectedPlayer.equals(island.getOwner()))
            influence += island.getNumberOfTowers();
        return influence;
    }

}
