package it.polimi.ingsw.model;

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
        int islandIndex = game.getCurrPlayer().getPlayerChoice().getIslandSelection();
        Island island = game.getArchipelago().getIsland(islandIndex);
        game.updateInfluence(island);
        endActivation();
        }
    }

