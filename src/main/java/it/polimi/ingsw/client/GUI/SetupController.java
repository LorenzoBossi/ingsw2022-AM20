package it.polimi.ingsw.client.GUI;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class SetupController is the controller of the Connection Scene
 */
public class SetupController implements GUIController {
    @FXML
    public TextField serverIp;
    @FXML
    public TextField serverPort;
    @FXML
    public Label errorPort;
    @FXML
    public Label errorIP;

    private GUI gui;

    public void setGui(GUI gui) {
        this.gui = gui;
    }

    /**
     * Set up the connection scene
     * @param e the event
     */
    public void setupConnection(ActionEvent e) {
        if(serverIp.getText().equals("")) {
            errorIP.setOpacity(1);
            return;
        }
        errorIP.setOpacity(0);
        String IP = serverIp.getText();
        try {
            int port = Integer.parseInt(serverPort.getText());
            System.out.println("IP : " + IP + " Port :" + port);
            gui.setup(IP, port);
        } catch (NumberFormatException ex) {
            errorPort.setOpacity(1);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorIP.setOpacity(0);
        errorPort.setOpacity(0);
    }
}
