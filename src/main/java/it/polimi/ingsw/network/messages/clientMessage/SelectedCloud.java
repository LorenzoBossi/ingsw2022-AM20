package it.polimi.ingsw.network.messages.clientMessage;

/**
 * Message sent by the client to select one cloud
 */
public class SelectedCloud extends ActionPhaseMessage {
    private final int cloudId;

    /**
     * Constructor
     * @param nickname the nickname of the player
     * @param cloudId the cloud id
     */
    public SelectedCloud(String nickname, int cloudId) {
        super(nickname);
        this.cloudId = cloudId;
    }

    /**
     * Gets the cloud id
     * @return the cloud id
     */
    public int getCloudId() {
        return cloudId;
    }
}
