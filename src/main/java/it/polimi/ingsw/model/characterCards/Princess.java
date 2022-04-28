package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.Bag;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;

import java.util.List;

public class Princess extends WithStudents {
    private final int NUMBER_OF_STUDENT = 4;
    private final int MAX_SELECTION = 1;

    /**
     * Constructor
     *
     * @param bag the bag of the game to put students on the card
     */
    public Princess(Bag bag) {
        super(CharacterName.PRINCESS, 2);
        setStudents(bag.getStudents(NUMBER_OF_STUDENT));
    }

    /**
     * Puts the selected student of the current player into the current player dining room
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        Player currPlayer = game.getCurrPlayer();
        List<Color> student = currPlayer.getPlayerChoice().getSelectedStudents();

        if (getStudents().contains(student.get(0))) {
            List<Color> studentToAdd;
            remove(student);
            studentToAdd = game.getBag().getStudents(1);
            add(studentToAdd);

            currPlayer.getPlayerBoard().getDiningRoom().addStudent(student.get(0));

            notifyObserver(new MoveStudents("BAG", "CARD", studentToAdd, null, getName().name()));
            notifyObserver(new MoveStudents("CARD", "DINING_ROOM", student, getName().name(), currPlayer.getNickname()));
            endActivation();
        }
    }


    @Override
    public boolean checkRequirements(){
        if(getStudents().size()<MAX_SELECTION)
            return false;
        return true;
    }
}