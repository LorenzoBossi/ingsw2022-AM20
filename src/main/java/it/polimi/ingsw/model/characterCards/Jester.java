package it.polimi.ingsw.model.characterCards;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;

import java.util.List;

public class Jester extends WithStudents {
    private final int NUMBER_OF_STUDENT = 6;
    private final int MAX_SELECTION = 3;

    /**
     * Constructor
     *
     * @param bag the bag of the game to put students on the card
     */
    public Jester(Bag bag) {
        super(CharacterName.JESTER, 1);
        List<Color> students;
        setStudents(bag.getStudents(NUMBER_OF_STUDENT));
    }

    /**
     * Swaps selected students on the card with the selected students in the entrance
     *
     * @param game the game
     */
    @Override
    public void activateEffect(Game game) {
        Player currPlayer = game.getCurrPlayer();
        PlayerChoice playerChoice = currPlayer.getPlayerChoice();
        Entrance entrance = currPlayer.getPlayerBoard().getEntrance();
        int i;
        int numberOfStudents;

        List<Color> fromEntrance = playerChoice.getSelectedStudentFromEntrance();
        List<Color> students = playerChoice.getSelectedStudents();
        numberOfStudents = fromEntrance.size();

        for (i = 0; i < numberOfStudents; i++) {
            entrance.removeStudent(fromEntrance.get(i));
            entrance.addStudent(students.get(i));
        }

        remove(students);
        add(fromEntrance);

        notifyObserver(new MoveStudents(GameComponent.ENTRANCE, GameComponent.CARD, fromEntrance, currPlayer.getNickname(), getName().name()));
        notifyObserver(new MoveStudents(GameComponent.CARD, GameComponent.ENTRANCE, students, getName().name(), currPlayer.getNickname()));
        endActivation();
    }
}
