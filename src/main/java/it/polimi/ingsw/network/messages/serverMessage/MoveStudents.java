package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.GameComponent;

import java.util.List;

public class MoveStudents implements UpdateMessage {
    private GameComponent source;
    private GameComponent destination;
    private List<Color> students;
    private Object indexSource;
    private Object indexDestination;

    public MoveStudents(GameComponent source, GameComponent destination, List<Color> students, Object indexSource, Object indexDestination) {
        this.source = source;
        this.destination = destination;
        this.students = students;
        this.indexSource = indexSource;
        this.indexDestination = indexDestination;
    }

    public GameComponent getSource() {
        return source;
    }

    public GameComponent getDestination() {
        return destination;
    }

    public List<Color> getStudents() {
        return students;
    }

    public Object getIndexSource() {
        return indexSource;
    }

    public Object getIndexDestination() {
        return indexDestination;
    }
}
