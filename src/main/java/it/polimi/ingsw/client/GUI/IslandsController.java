package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.IslandView;
import it.polimi.ingsw.model.Color;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.*;

/**
 * The controller of the islands scene
 */
public class IslandsController implements GUIController {

    private GUI gui;

    private Image banCardImage;
    private Image islandImage;
    private Image motherNature;
    private List<AnchorPane> islands;

    @FXML
    private AnchorPane island0;
    @FXML
    private AnchorPane island1;
    @FXML
    private AnchorPane island2;
    @FXML
    private AnchorPane island3;
    @FXML
    private AnchorPane island4;
    @FXML
    private AnchorPane island5;
    @FXML
    private AnchorPane island6;
    @FXML
    private AnchorPane island7;
    @FXML
    private AnchorPane island8;
    @FXML
    private AnchorPane island9;
    @FXML
    private AnchorPane island10;
    @FXML
    private AnchorPane island11;
    @FXML
    private AnchorPane island12;
    @FXML
    private AnchorPane island13;

    /**
     * Updates the islands scene
     */
    public void update() {
        Map<Integer, IslandView> islandViewMap = gui.getClientModel().getIslandsViewMap();
        setupIslands(islandViewMap.size());
        IslandView island;

        for (Integer islandId : islandViewMap.keySet()) {
            island = islandViewMap.get(islandId);
            buildIsland(islandId, island.getStudents(), island.getOwner(), island.getNumberOfTower(), island.getBanCards());
        }
    }

    /**
     * Make one island
     * @param islandId the id of the island to make
     * @param students the students on the islands
     * @param owner the owner of the island
     * @param numberOfTowers the number of towers on the island
     * @param bancards the number of ban cards on the island
     */
    private void buildIsland(int islandId, List<Color> students, String owner, int numberOfTowers, int bancards) {
        AnchorPane island = islands.get(islandId);
        ImageView image = new ImageView(islandImage);

        setImageView(image, 90, 90, 65, 24);
        island.getChildren().add(image);

        addStudents(island, students);
        addTowers(island, owner, numberOfTowers);
        addBanCards(island, bancards);

        if (islandId == gui.getClientModel().getMotherNaturePosition()) {
            ImageView motherNature = new ImageView(this.motherNature);
            setImageView(motherNature, 39, 47, 91, 35);
            island.getChildren().add(motherNature);
        }
    }

    /**
     * Adds students to the island
     * @param island the pane where island is
     * @param students the students to add on the island
     */
    private void addStudents(AnchorPane island, List<Color> students) {
        ImageView student;
        TilePane space = new TilePane();

        space.setAlignment(Pos.TOP_CENTER);
        space.setPrefSize(90, 72);
        space.setLayoutX(65);
        space.setLayoutY(114);
        island.getChildren().add(space);

        for (Color color : students) {
            student = new ImageView(gui.getStudentImage(color));
            setImageView(student, 18, 18);
            space.getChildren().add(student);
        }

    }

    /**
     * Adds towers to the islands
     * @param island the pane where island is
     * @param owner the owner of the island
     * @param numberOfTowers the number of towers on the islands
     */
    private void addTowers(AnchorPane island, String owner, int numberOfTowers) {
        if(owner == null)
            return;

        TilePane towers = new TilePane();
        Image towerColor = gui.getTowerImage(owner);
        ImageView tower;

        towers.setAlignment(Pos.CENTER);
        towers.setPrefSize(49, 90);
        towers.setLayoutX(149);
        towers.setLayoutY(10);
        island.getChildren().add(towers);

        for (int i = 0; i < numberOfTowers; i++) {
            tower = new ImageView(towerColor);
            setImageView(tower, 24, 30);
            towers.getChildren().add(tower);
        }
    }

    /**
     * Adds ban cards to the island
     * @param island the pane where island is
     * @param banCards the ban cards to add on the island
     */
    private void addBanCards(AnchorPane island, int banCards) {
        TilePane space = new TilePane();
        ImageView banCard;

        space.setAlignment(Pos.TOP_CENTER);
        space.setPrefSize(50, 50);
        space.setLayoutX(20);
        space.setLayoutY(44);
        island.getChildren().add(space);

        for (int i = 0; i < banCards; i++) {
            banCard = new ImageView(banCardImage);
            setImageView(banCard, 23, 23);
            space.getChildren().add(banCard);
        }
    }

    /**
     * Make an image view
     * @param image the image
     * @param width the width
     * @param height the height
     * @param x the x-position
     * @param y the y-position
     */
    private void setImageView(ImageView image, double width, double height, double x, double y) {
        image.smoothProperty();
        image.setPreserveRatio(true);
        image.setLayoutX(x);
        image.setLayoutY(y);
        image.setFitWidth(width);
        image.setFitHeight(height);
    }

    /**
     * Make an image view
     * @param image the image
     * @param width the width
     * @param height the height
     */
    private void setImageView(ImageView image, double width, double height) {
        image.smoothProperty();
        image.setPreserveRatio(true);
        image.setFitWidth(width);
        image.setFitHeight(height);
    }


    /**
     * Adds the IslandPane to the list of the pane where to put the islands
     * @param numberOfIslands the number of islands
     */
    private void setupIslands(int numberOfIslands) {
        switch (numberOfIslands) {
            case 12 -> islands = new ArrayList<>(Arrays.asList(island0, island1, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11));
            case 11 -> islands = new ArrayList<>(Arrays.asList(island0, island2, island3, island4, island5, island6, island7, island8, island9, island10, island11));
            case 10 -> islands = new ArrayList<>(Arrays.asList(island0, island2, island3, island4, island6, island7, island8, island9, island10, island11));
            case 9 -> islands = new ArrayList<>(Arrays.asList(island0, island2, island3, island4, island6, island8, island9, island10, island11));
            case 8 -> islands = new ArrayList<>(Arrays.asList(island12, island2, island3, island4, island13, island8, island9, island10));
            case 7 -> islands = new ArrayList<>(Arrays.asList(island12, island3, island4, island13, island8, island9, island10));
            case 6 -> islands = new ArrayList<>(Arrays.asList(island12, island3, island13, island8, island9, island10));
            case 5 -> islands = new ArrayList<>(Arrays.asList(island12, island3, island13, island9, island10));
            case 4 -> islands = new ArrayList<>(Arrays.asList(island12, island3, island13, island9));
        }
    }

    /**
     * Clear the islands
     */
    public void clear() {
        for (AnchorPane pane : islands) {
            pane.getChildren().clear();
        }
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        islands = new ArrayList<>();
        islandImage = new Image(String.valueOf(getClass().getResource("/Images/island1.png")));
        motherNature = new Image(String.valueOf(getClass().getResource("/Images/mother_nature.png")));
        banCardImage = new Image(String.valueOf(getClass().getResource("/Images/ban_card.png")));
    }
}
