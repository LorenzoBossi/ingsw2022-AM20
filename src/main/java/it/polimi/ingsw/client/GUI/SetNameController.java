package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.network.messages.clientMessage.NicknameRequest;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class SetNameController is the controller of the Name Scene
 */
public class SetNameController implements GUIController {
    private GUI gui;

    @FXML
    private Label error;
    @FXML
    private TextField nickname;

    /**
     * Checks if the name insert by the client is valid and sends the chosen nickname to the server
     */
    public void checkName() {
        error.setOpacity(0);
        if(nickname.getText().equals("")) {
            error.setOpacity(1);
            return;
        }
        gui.setClientNickname(nickname.getText());
        gui.sendMessage(new NicknameRequest(nickname.getText()));
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        error.setOpacity(0);
    }
}
