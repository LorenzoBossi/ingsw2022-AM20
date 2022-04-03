package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.InfluenceStrategy;

public class InfluenceCard extends CharacterCard {
    private InfluenceStrategy influenceStrategy;

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     * @param influenceStrategy the strategy to calculate influence
     */
    public InfluenceCard(int coinsRequired,InfluenceStrategy influenceStrategy){
        super(coinsRequired);
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
        endActivation();
    }
}
