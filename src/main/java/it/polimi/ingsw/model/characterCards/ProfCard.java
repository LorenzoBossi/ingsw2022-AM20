package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;

public class ProfCard extends CharacterCard {

    /**
     * Constructor
     */
    public ProfCard() {
        super(CharacterName.PROF_CARD, 1, CharacterCardType.NORMAL);
    }

    /**
     * Changes the comparator of professor manager
     * @param game the game
     */
    @Override
    public void activateEffect(Game game){
        game.getProfessorManager().setComparator( (x,y) -> x>=y ? 1:0 );
        endActivation(game.getCurrPlayer().getNickname());
    }
}
