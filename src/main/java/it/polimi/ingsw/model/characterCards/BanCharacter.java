package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.IslandsManager;
import it.polimi.ingsw.model.Player;

public class BanCharacter extends CharacterCard {
    private int banCards;
    private final int INITIAL_BANCARDS = 4;

    /**
     * Constructor
     */
    public BanCharacter() {
        super(CharacterName.HERBALIST, 2);
        banCards = INITIAL_BANCARDS;
    }



    /**
     * Method activateEffect adds a banCard on the player's selected island
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        IslandsManager islandsManager = game.getArchipelago();
        Player currPlayer = game.getCurrPlayer();
        int removedBanCards = INITIAL_BANCARDS - islandsManager.getNumberOfBanCards() - banCards;

        banCards = removedBanCards + banCards;

        Island island = currPlayer.getPlayerChoice().getSelectedIsland();
        islandsManager.addBanCardOnIsland(island);
        banCards--;

        endActivation();
    }

    @Override
    public boolean checkRequirements(){
        if(banCards==0)
            return false;
        return true;
    }
}
