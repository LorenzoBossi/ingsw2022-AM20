package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InfluenceStrategy;

/**
 * Unites three different characters that change the way of evaluate player influence on islands
 */
public class InfluenceCard extends CharacterCard {
    private InfluenceStrategy influenceStrategy;

    /**
     * Constructor
     *
     * @param coinsRequired     the coins required to activate the card
     * @param influenceStrategy the strategy to calculate influence
     */
    public InfluenceCard(CharacterName name, int coinsRequired, InfluenceStrategy influenceStrategy, CharacterCardType characterCardType) {
        super(name, coinsRequired, characterCardType);
        this.influenceStrategy = influenceStrategy;
    }

    /**
     * Method activateEffect sets current player in the influenceStrategy and sets the method to calculate
     * influence in the game
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        influenceStrategy.setCurrPlayer(game.getCurrPlayer());
        game.setInfluenceStrategy(this.influenceStrategy);
        endActivation(game.getCurrPlayer().getNickname());
    }
}
