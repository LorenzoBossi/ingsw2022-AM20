package it.polimi.ingsw.utils;

import it.polimi.ingsw.network.messages.serverMessage.ServerMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class ObservableSubject {
    private List<EventObserver> views;

    public ObservableSubject() {
        this.views = new ArrayList<>();
    }

    public void registerObserver(EventObserver eventObserver) {
        views.add(eventObserver);
    }

    public void removeObserver(EventObserver eventObserver) {
        views.remove(eventObserver);
    }

    public void notifyObserver(ServerMessage message) {
        for (EventObserver observer : views) {
            observer.update(message);
        }
    }

}
