package it.polimi.ingsw.network.messages.clientMessage;

public class SelectedCloud extends ActionPhaseMessage {
    private int cloudId;

    public SelectedCloud(String nickname, int cloudId) {
        super(nickname);
        this.cloudId = cloudId;
    }

    public int getCloudId() {
        return cloudId;
    }
}
