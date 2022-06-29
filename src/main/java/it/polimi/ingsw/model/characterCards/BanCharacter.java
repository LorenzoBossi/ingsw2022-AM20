package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;
import it.polimi.ingsw.model.IslandsManager;
import it.polimi.ingsw.model.Player;

public class BanCharacter extends CharacterCard {
    private int banCards;
    private final int INITIAL_BANCARDS = 4;

    /**
     * Constructor similar to the one of CharacterCard with the addition of the initialization of the number of ban cards
     */
    public BanCharacter() {
        super(CharacterName.HERBALIST, 2, CharacterCardType.ISLAND_SELECTION);
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

        endActivation(currPlayer.getNickname());
    }


    /*
     * checks if the card contains at least one ban card
     *
     * @param player the current player of the game
     * @return true if the requirements are satisfied
     * false otherwise
     */
    /*
    @Override
    public boolean checkRequirements(Player player) {
        if (banCards == 0)
            return false;
        return true;
    }
     */
}
