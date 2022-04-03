package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Island;

public class Vassal extends CharacterCard {

    /**
     * Constructor
     *
     * @param coinsRequired the coins required to activate the card
     */
    public Vassal(int coinsRequired) {
        super(coinsRequired);
    }


    /**
     * Method activateEffect calls the Game's method updateInfluence on the player's selected island
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        Island island = game.getCurrPlayer().getPlayerChoice().getSelectedIsland();
        game.updateInfluence(island);
        endActivation();
        }
    }

