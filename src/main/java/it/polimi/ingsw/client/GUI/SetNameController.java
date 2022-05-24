package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.network.messages.clientMessage.NicknameRequest;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class SetNameController implements GUIController, Initializable {
    private GUI gui;

    @FXML
    private Label error;
    @FXML
    private TextField nickname;

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
