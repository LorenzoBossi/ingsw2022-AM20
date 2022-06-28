package it.polimi.ingsw.client.GUI;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.*;

/**
 * Class PlayerBoardController is the controller of the PlayerBoard Scene
 */
public class PlayerBoardController extends BoardUpdater implements GUIController {

    @FXML
    private ImageView student0;
    @FXML
    private ImageView student1;
    @FXML
    private ImageView student2;
    @FXML
    private ImageView student3;
    @FXML
    private ImageView student4;
    @FXML
    private ImageView student5;
    @FXML
    private ImageView student6;
    @FXML
    private ImageView student7;
    @FXML
    private ImageView student8;
    @FXML
    private TilePane greenDining;
    @FXML
    private TilePane redDining;
    @FXML
    private TilePane pinkDining;
    @FXML
    private TilePane yellowDining;
    @FXML
    private TilePane blueDining;
    @FXML
    private ImageView greenProf;
    @FXML
    private ImageView redProf;
    @FXML
    private ImageView yellowProf;
    @FXML
    private ImageView pinkProf;
    @FXML
    private ImageView blueProf;
    @FXML
    private TilePane towers;
    @FXML
    private ImageView coin;
    @FXML
    private Label coinNumber;

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.students = new ArrayList<>(Arrays.asList(student0, student1, student2, student3, student4, student5, student6, student7, student8));
        this.profs = new ArrayList<>(Arrays.asList(greenProf, redProf, yellowProf, pinkProf, blueProf));
        this.diningRooms = new ArrayList<>(Arrays.asList(greenDining, redDining, yellowDining, pinkDining, blueDining));
        this.towersPane = towers;
        this.towerX = 48;
        this.towerY = 52;
        this.studentX = 39;
        this.studentY = 39;
        this.coinsLabel = coinNumber;
        this.coinView = coin;
    }
}
