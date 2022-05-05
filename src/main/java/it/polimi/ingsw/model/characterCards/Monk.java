package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;


import java.util.List;

public class Monk extends WithStudents {
    /*
    private final int NUMBER_OF_STUDENT = 4;
    private final int MAX_SELECTION = 1;
    */

    /**
     * Constructor
     *
     * @param bag the bag of the game to put students on the card
     */
    public Monk(Bag bag) {
        super(CharacterName.MONK, 1,1);
        setStudents(bag.getStudents(4));
    }

    /**
     * Puts the selected student on the selected Island
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        IslandsManager islandsManager = game.getArchipelago();
        Player currPlayer = game.getCurrPlayer();
        int islandPosition;

        List<Color> student = currPlayer.getPlayerChoice().getSelectedStudents();
        if (getStudents().contains(student.get(0))) {
            remove(student);
        }

        Island island = currPlayer.getPlayerChoice().getSelectedIsland();
        islandPosition = islandsManager.getPositionByIsland(island);

        island.addStudent(student.get(0));
        notifyObserver(new MoveStudents(GameComponent.CARD, GameComponent.ISLAND, student, getName().name(), islandPosition));

        List<Color> studentToAdd = game.getBag().getStudents(1);
        add(studentToAdd);
        notifyObserver(new MoveStudents(GameComponent.BAG, GameComponent.CARD, studentToAdd, null, getName().name()));
        endActivation();
    }



}
