package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;

import java.util.List;

public class MorePointsInfluence implements InfluenceStrategy {
    Player currPlayer;


    /**
     * Constructor
     */
    public MorePointsInfluence(){
        super();
        this.currPlayer = null;
    }

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
        if(currPlayer.equals(selectedPlayer))
            return influenceStandardCalculate(selectedPlayer, island, professorManager) + 2;
        return influenceStandardCalculate(selectedPlayer, island, professorManager);
    }

    /**
     * Used to calculate the influence of players different from the activator of the card in the standard way
     * @param selectedPlayer player to calculate influence of
     * @param island island to calculate influence of
     * @param professorManager containing informations about professors and their owners
     * @return the points of influence of the specified player on the specified island
     */
    private int influenceStandardCalculate(Player selectedPlayer, Island island, ProfessorManager professorManager) {
        List<Color> professorsOwned = professorManager.getProfessorsOwnedBy(selectedPlayer);
        int influence = 0;
        for(Color c: professorsOwned)
            influence += island.getSelectedStudents(c);
        if(selectedPlayer.equals(island.getOwner()))
            influence += island.getNumberOfTowers();
        return influence;
    }

}

