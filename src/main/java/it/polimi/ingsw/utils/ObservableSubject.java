package it.polimi.ingsw.utils;

import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class ObservableSubject {
    private final List<EventObserver> views;

    /**
     * Constructor
     */
    public ObservableSubject() {
        this.views = new ArrayList<>();
    }

    /**
     * Registers observers to the object
     * @param eventObserver
     */
    public void registerObserver(EventObserver eventObserver) {
        views.add(eventObserver);
    }

    /**
     * Notify all the observers with the update message
     * @param message
     */
    public void notifyObserver(ServerMessage message) {
        for (EventObserver observer : views) {
            observer.update(message);
        }
    }

}
