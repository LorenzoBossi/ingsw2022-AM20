package it.polimi.ingsw.model;

public class BanCharacter extends CharacterCard{
    private int banCards;
    private final int INITIAL_BANCARDS = 4;

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public BanCharacter(int coinsRequired) {
        super(coinsRequired);
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
        int islandIndex = game.getCurrPlayer().getPlayerChoice().getIslandSelection();
        game.getArchipelago().getIsland(islandIndex).addBanCard();
        banCards--;
        endActivation();
    }
}
