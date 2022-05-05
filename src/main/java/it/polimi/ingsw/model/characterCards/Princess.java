package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;

import java.util.List;

public class Princess extends WithStudents {
    /*
    private final int NUMBER_OF_STUDENT = 4;
    private final int MAX_SELECTION = 1;
    */
    /**
     * Constructor
     *
     * @param bag the bag of the game to put students on the card
     */
    public Princess(Bag bag) {
        super(CharacterName.PRINCESS, 2,1);
        setStudents(bag.getStudents(4));
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
        ProfessorManager profManager= game.getProfessorManager();
        if (getStudents().contains(student.get(0))) {
            List<Color> studentToAdd;
            remove(student);
            studentToAdd = game.getBag().getStudents(1);
            add(studentToAdd);

            currPlayer.getPlayerBoard().getDiningRoom().addStudent(student.get(0));
            if(profManager.canTakeProfessor(currPlayer,student.get(0))){
                profManager.takeProfessor(currPlayer,student.get(0));
            }

            notifyObserver(new MoveStudents(GameComponent.BAG, GameComponent.CARD, studentToAdd, null, getName().name()));
            notifyObserver(new MoveStudents(GameComponent.CARD, GameComponent.DINING_ROOM, student, getName().name(), currPlayer.getNickname()));

            endActivation();
        }
    }

}