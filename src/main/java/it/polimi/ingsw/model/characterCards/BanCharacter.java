package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;

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
        int removedBanCards = INITIAL_BANCARDS - game.getArchipelago().getNumberOfBanCards() - banCards;
        banCards = removedBanCards + banCards;
        Island island= game.getCurrPlayer().getPlayerChoice().getSelectedIsland();
        island.addBanCard();
        banCards--;
        endActivation();
    }
}
