package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;

public class PostMan extends CharacterCard {


    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public PostMan(int coinsRequired) {
        super(coinsRequired);
    }

    /**
     * adds 2 to the maximum mother nature moves of the current player
     * @param game the game
     */
    @Override
    public void activateEffect(Game game){
        Player p= game.getCurrPlayer();
        p.setMotherNatureMaxMove(p.getMotherNatureMaxMove()+2);
    }
}
