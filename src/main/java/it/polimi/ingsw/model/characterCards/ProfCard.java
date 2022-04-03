package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;

public class ProfCard extends CharacterCard {

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public ProfCard(int coinsRequired) {
        super(coinsRequired);
    }

    /**
     * Changes the comparator of professor manager
     * @param game the game
     */
    @Override
    public void activateEffect(Game game){
        game.getProfessorManager().setComparator( (x,y) -> x>=y ? 1:0 );
    }
}
