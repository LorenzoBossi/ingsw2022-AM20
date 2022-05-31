package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ActionMove;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.network.messages.clientMessage.SelectedCloud;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class CloudsController implements GUIController, Initializable {
    private GUI gui;

    private List<ImageView> circles;
    private List<RadioButton> buttons;
    private Map<Integer, LinkedList<ImageView>> clouds;

    @FXML
    private ImageView student11;
    @FXML
    private ImageView student12;
    @FXML
    private ImageView student13;
    @FXML
    private ImageView student14;
    @FXML
    private ImageView student21;
    @FXML
    private ImageView student22;
    @FXML
    private ImageView student23;
    @FXML
    private ImageView student24;
    @FXML
    private ImageView student31;
    @FXML
    private ImageView student32;
    @FXML
    private ImageView student33;
    @FXML
    private ImageView student34;
    @FXML
    private ImageView cloud1P;
    @FXML
    private ImageView cloud2P;
    @FXML
    private ImageView cloud3P;
    @FXML
    private ImageView circle1;
    @FXML
    private ImageView circle2;
    @FXML
    private ImageView circle3;
    @FXML
    private ToggleGroup cloudsButton;
    @FXML
    private RadioButton button1;
    @FXML
    private RadioButton button2;
    @FXML
    private RadioButton button3;
    @FXML
    private Button confirmButton;


    public void initClouds(int numberOfPlayer) {
        if (numberOfPlayer == 2) {
            cloud3P.setImage(null);
            clouds.remove(2);
            for (ImageView circle : circles)
                circle.setImage(null);
            for (Integer cloudId : clouds.keySet())
                clouds.get(cloudId).removeLast();
            button3.setDisable(true);
            button3.setOpacity(0);
            cloudsButton.getToggles().remove(button3);
            buttons.remove(button3);
        }
    }

    public void update() {
        Map<Integer, List<Color>> clouds = gui.getClientModel().getClouds();

        for (Integer cloudId : clouds.keySet())
            updateCloud(cloudId, clouds.get(cloudId));
    }

    private void updateCloud(Integer cloudId, List<Color> students) {
        List<ImageView> studentView = clouds.get(cloudId);
        int i = 0;

        for (Color student : students) {
            studentView.get(i).setImage(gui.getStudentImage(student));
            i++;
        }
    }

    public void initCloudsButton() {
        Map<Integer, List<Color>> cloudsMap = gui.getClientModel().getClouds();
        if(cloudsButton.getSelectedToggle() != null)
            cloudsButton.getSelectedToggle().setSelected(false);

        for (Integer cloudId : clouds.keySet()) {
            if (cloudsMap.get(cloudId).size() > 0) {
                buttons.get(cloudId).setOpacity(1);
                buttons.get(cloudId).setDisable(false);
            }
        }

        confirmButton.setDisable(true);
        confirmButton.setOpacity(1);
    }

    public void selectCloud() {
        int cloudId = (int) cloudsButton.getSelectedToggle().getUserData();
        gui.consumeAction(ActionMove.SELECT_CLOUD);
        cloudsButton.getSelectedToggle().setSelected(false);
        gui.sendMessage(new SelectedCloud(gui.getClientNickname(), cloudId));
        Stage stage = (Stage) confirmButton.getScene().getWindow();
        stage.close();
    }

    public void clear() {
        List<ImageView> images;

        for (Integer cloudId : clouds.keySet()) {
            images = clouds.get(cloudId);
            for (ImageView image : images)
                image.setImage(null);
        }

        for (RadioButton button : buttons) {
            button.setDisable(true);
            button.setOpacity(0);
        }

        confirmButton.setDisable(true);
        confirmButton.setOpacity(0);
    }


    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        clouds = new HashMap<>();

        circles = new ArrayList<>(Arrays.asList(circle1, circle2, circle3));

        LinkedList<ImageView> cloud1 = new LinkedList<>(Arrays.asList(student11, student12, student13, student14));
        LinkedList<ImageView> cloud2 = new LinkedList<>(Arrays.asList(student21, student22, student23, student24));
        LinkedList<ImageView> cloud3 = new LinkedList<>(Arrays.asList(student31, student32, student33, student34));

        clouds.put(0, cloud1);
        clouds.put(1, cloud2);
        clouds.put(2, cloud3);

        buttons = new ArrayList<>(Arrays.asList(button1, button2, button3));
        button1.setUserData(0);
        button2.setUserData(1);
        button3.setUserData(2);

        confirmButton.setDisable(true);
        confirmButton.setOpacity(0);

        cloudsButton.selectedToggleProperty().addListener((observableValue, toggle, t1) -> confirmButton.setDisable(false));
    }
}
