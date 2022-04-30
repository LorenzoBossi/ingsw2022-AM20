package it.polimi.ingsw.network.messages.clientMessage;

import it.polimi.ingsw.model.TowerColor;

public class ChosenTowerColor implements ClientMessage {
    private TowerColor towerColor;

    public ChosenTowerColor(TowerColor towerColor) {
        this.towerColor = towerColor;
    }

    public TowerColor getTowerColor() {
        return towerColor;
    }
}
