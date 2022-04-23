package it.polimi.ingsw.network.messages.serverMessage;

import it.polimi.ingsw.model.Color;

import java.util.List;

public class MoveStudents implements UpdateMessage {
    private String source;
    private String destination;
    private List<Color> students;
    private Object indexSource;
    private Object indexDestination;

    public MoveStudents(String source, String destination, List<Color> students, Object indexSource, Object indexDestination) {
        this.source = source;
        this.destination = destination;
        this.students = students;
        this.indexSource = indexSource;
        this.indexDestination = indexDestination;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
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
