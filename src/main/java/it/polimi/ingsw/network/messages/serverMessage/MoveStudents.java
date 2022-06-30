package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;

import java.util.List;

/**
 * Message sent by the server to notify the clients of the student movement
 */
public class MoveStudents implements UpdateMessage {
    private final GameComponent source;
    private final GameComponent destination;
    private final List<Color> students;
    private final Object indexSource;
    private final Object indexDestination;

    /**
     * Constructor
     * @param source the game component where the students come from
     * @param destination the game component where the students have to arrive
     * @param students the students
     * @param indexSource the index of the source
     * @param indexDestination the index of the destination
     */
    public MoveStudents(GameComponent source, GameComponent destination, List<Color> students, Object indexSource, Object indexDestination) {
        this.source = source;
        this.destination = destination;
        this.students = students;
        this.indexSource = indexSource;
        this.indexDestination = indexDestination;
    }

    /**
     * Gets the game component where the students come from
     * @return the game component where the students come from
     */
    public GameComponent getSource() {
        return source;
    }

    /**
     * Gets the game component where the students have to arrive
     * @return the game component where the students have to arrive
     */
    public GameComponent getDestination() {
        return destination;
    }

    /**
     * Gets the students
     * @return the students
     */
    public List<Color> getStudents() {
        return students;
    }

    /**
     * Gets the index of the source
     * @return the index of the source
     */
    public Object getIndexSource() {
        return indexSource;
    }

    /**
     * Gets the index of the destination
     * @return the index of the destination
     */
    public Object getIndexDestination() {
        return indexDestination;
    }
}
