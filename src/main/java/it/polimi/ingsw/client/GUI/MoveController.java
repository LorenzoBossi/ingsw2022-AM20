package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ActionMove;
import it.polimi.ingsw.client.IslandView;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.clientMessage.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.*;

public class MoveController extends BoardUpdater implements GUIController, Initializable {

    private Image banCardImage;
    private Image islandImage;
    private Image motherNature;

    private List<AnchorPane> islands;
    private List<RadioButton> entranceButtons;
    private ToggleGroup studentContainer;
    private List<ImageView> cardStudents;
    private List<RadioButton> cardButtons;

    @FXML
    private ToggleGroup entrance;
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
    private RadioButton entrance0;
    @FXML
    private RadioButton entrance1;
    @FXML
    private RadioButton entrance2;
    @FXML
    private RadioButton entrance3;
    @FXML
    private RadioButton entrance4;
    @FXML
    private RadioButton entrance5;
    @FXML
    private RadioButton entrance6;
    @FXML
    private RadioButton entrance7;
    @FXML
    private RadioButton entrance8;
    @FXML
    private RadioButton diningButton;
    @FXML
    private TilePane greenDining;
    @FXML
    private TilePane redDining;
    @FXML
    private TilePane yellowDining;
    @FXML
    private TilePane pinkDining;
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
    private AnchorPane messagePane;
    @FXML
    private AnchorPane cardPane;
    @FXML
    private ImageView cardImage;
    @FXML
    private ImageView cardStud0;
    @FXML
    private ImageView cardStud1;
    @FXML
    private ImageView cardStud2;
    @FXML
    private ImageView cardStud3;
    @FXML
    private ImageView cardStud4;
    @FXML
    private ImageView cardStud5;
    @FXML
    private RadioButton cardButton0;
    @FXML
    private RadioButton cardButton1;
    @FXML
    private RadioButton cardButton2;
    @FXML
    private RadioButton cardButton3;
    @FXML
    private RadioButton cardButton4;
    @FXML
    private RadioButton cardButton5;


    public void update() {
        String nickname = gui.getClientNickname();
        updatePlayerBoard(nickname);
        initEntranceButton(gui.getClientModel().getEntrances().get(nickname));
        updateIslands(gui.getClientModel().getIslandsViewMap());
    }

    public void initEntranceButton(List<Color> students) {
        int i = 0;
        RadioButton button;

        for (Color student : students) {
            button = entranceButtons.get(i);
            button.setDisable(false);
            button.setOpacity(1);
            button.setUserData(student);
            i++;
        }

    }

    public void moveStudents() {
        diningButton.setUserData(-1);
        diningButton.setOpacity(1);
        diningButton.setDisable(false);
        studentContainer.getToggles().add(diningButton);
        Button confirmButton = new Button();
        Label message = new Label();
        message.setFont(new Font("system", 13.5));
        confirmButton.setFont(new Font("system", 24));


        message.setLayoutX(8);
        message.setLayoutY(79);
        message.setText("Select one student from the entrance and select the island where you want to move or the dining room");
        messagePane.getChildren().add(message);


        confirmButton.setDisable(true);
        confirmButton.setText("Confirm");
        confirmButton.setLayoutX(257);
        confirmButton.setLayoutY(118);

        messagePane.getChildren().add(confirmButton);
        confirmButton.setOnAction(actionEvent -> {
            int islandId = (int) studentContainer.getSelectedToggle().getUserData();
            Color student = (Color) entrance.getSelectedToggle().getUserData();
            gui.consumeAction(ActionMove.MOVE_STUDENTS);
            if (islandId == -1) {
                gui.sendMessage(new MoveStudentOnDiningRoom(gui.getClientNickname(), student));
            } else {
                gui.sendMessage(new MoveStudentToIsland(gui.getClientNickname(), islandId, student));
            }
        });
        initToggleGroupMoveStudents(confirmButton);
    }

    public void initIslandSelection(CharacterName name) {
        Label message = new Label("Select one island");
        Button confirmButton = new Button();
        diningButton.setDisable(true);
        diningButton.setOpacity(0);

        confirmButton.setFont(new Font("system", 20));
        confirmButton.setDisable(true);
        confirmButton.setPrefSize(102, 51);
        confirmButton.setLayoutX(265);
        confirmButton.setLayoutY(144);
        confirmButton.setText("Confirm");
        confirmButton.setOnAction(actionEvent -> {
            int islandId = (int) studentContainer.getSelectedToggle().getUserData();
            gui.sendMessage(new SelectedIsland(gui.getClientNickname(), islandId));
            gui.consumeAction(ActionMove.ACTIVATE_CARD);
            gui.sendMessage(new ActiveEffect(gui.getClientNickname(), name));
        });

        message.setFont(new Font("system", 24));
        message.setLayoutX(226);
        message.setLayoutY(63);

        studentContainer.selectedToggleProperty().addListener((observableValue, toggle, t1) -> confirmButton.setDisable(false));

        messagePane.getChildren().addAll(Arrays.asList(message, confirmButton));
        updatePlayerBoard(gui.getClientNickname());
        updateIslands(gui.getClientModel().getIslandsViewMap());

    }

    public void initPrincessActivation(List<Color> students) {
        int i = 0;
        Button confirmButton = makeConfirmButton();
        Label message = makeLabel("Select one student to move to the Dining Room");
        RadioButton button;
        ImageView studImage;
        updateIslands(gui.getClientModel().getIslandsViewMap());
        diningButton.setOpacity(0);
        studentContainer.getToggles().forEach(t -> {
            Node node = (Node) t;
            node.setDisable(true);
            node.setOpacity(0);

        });
        studentContainer.getToggles().clear();

        messagePane.getChildren().add(cardPane);

        cardImage.setImage(gui.getCharacterImage(CharacterName.PRINCESS));


        for(Color student : students) {
            studImage = cardStudents.get(i);
            studImage.setImage(gui.getStudentImage(student));
            button = cardButtons.get(i);
            button.setUserData(student);
            button.setOpacity(1);
            button.setDisable(false);
            studentContainer.getToggles().add(button);
            i++;
        }

        studentContainer.selectedToggleProperty().addListener((observableValue, toggle, t1) -> confirmButton.setDisable(false));

        confirmButton.setOnAction(actionEvent -> {
            Color student = (Color) studentContainer.getSelectedToggle().getUserData();
            List <Color> students1 = new ArrayList<>();
            students1.add(student);
            gui.sendMessage(new SelectedStudentsFromCard(gui.getClientNickname(), students1, CharacterName.PRINCESS));
            gui.sendMessage(new ActiveEffect(gui.getClientNickname(), CharacterName.PRINCESS));

        });

        updatePlayerBoard(gui.getClientNickname());

        messagePane.getChildren().addAll(confirmButton, message);
    }

    private Button makeConfirmButton() {
        Button button = new Button();
        button.setFont(new Font("system", 20));
        button.setText("Confirm");
        button.setLayoutX(414);
        button.setLayoutY(161);
        button.setDisable(true);
        return button;
    }

    private Label makeLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("system", 15));
        label.setLayoutX(302);
        label.setLayoutY(96);
        return label;
    }

    public void clear() {
        clearBoard();
        if (studentContainer.getSelectedToggle() != null)
            studentContainer.getSelectedToggle().setSelected(false);
        if (entrance.getSelectedToggle() != null)
            entrance.getSelectedToggle().setSelected(false);
        for (RadioButton button : entranceButtons) {
            button.setDisable(true);
            button.setOpacity(0);
        }

        for(RadioButton button : cardButtons) {
            button.setDisable(true);
            button.setOpacity(0);
        }

        for (AnchorPane island : islands)
            island.getChildren().clear();

        studentContainer.getToggles().clear();
        studentContainer.getProperties().clear();
        entrance.getProperties().clear();
        messagePane.getChildren().clear();
    }

    private void initToggleGroupMoveStudents(Button confirmButton) {
        entrance.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (studentContainer.getSelectedToggle() != null)
                confirmButton.setDisable(false);
        });
        studentContainer.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (entrance.getSelectedToggle() != null)
                confirmButton.setDisable(false);
        });
    }

    public void backScene() {
        clear();
        gui.updateMyPlayerBoard();
        gui.changeScene("/Scene/actionScene.fxml");
    }

    private void updateIslands(Map<Integer, IslandView> islandViewMap) {
        setupIslands(islandViewMap.size());
        IslandView island;

        for (Integer islandId : islandViewMap.keySet()) {
            island = islandViewMap.get(islandId);
            buildIsland(islandId, island.getStudents(), island.getOwner(), island.getNumberOfTower(), island.getBanCards());
        }
    }

    private void buildIsland(int islandId, List<Color> students, String owner, int numberOfTowers, int bancards) {
        RadioButton button = new RadioButton();
        AnchorPane island = islands.get(islandId);
        ImageView image = new ImageView(islandImage);

        setImageView(image, 65, 65, 99, 0);
        island.getChildren().add(image);

        studentContainer.getToggles().add(button);
        button.setLayoutX(124);
        button.setLayoutY(40);
        button.setUserData(islandId);
        island.getChildren().add(button);

        addStudents(island, students);
        addTowers(island, owner, numberOfTowers);
        addBanCards(island, bancards);

        if (islandId == gui.getClientModel().getMotherNaturePosition()) {
            ImageView motherNature = new ImageView(this.motherNature);
            setImageView(motherNature, 36, 34, 117, 8);
            island.getChildren().add(motherNature);
        }
    }

    private void addStudents(AnchorPane island, List<Color> students) {
        ImageView student;
        TilePane space = new TilePane();

        space.setAlignment(Pos.TOP_CENTER);
        space.setPrefSize(97, 52);
        space.setLayoutX(83);
        space.setLayoutY(68);
        island.getChildren().add(space);

        for (Color color : students) {
            student = new ImageView(gui.getStudentImage(color));
            setImageView(student, 15, 15);
            space.getChildren().add(student);
        }

    }

    private void addTowers(AnchorPane island, String owner, int numberOfTowers) {
        if (owner == null)
            return;

        TilePane towers = new TilePane();
        Image towerColor = gui.getTowerImage(owner);
        ImageView tower;

        towers.setAlignment(Pos.CENTER);
        towers.setPrefSize(66, 60);
        towers.setLayoutX(163);
        towers.setLayoutY(5);
        island.getChildren().add(towers);

        for (int i = 0; i < numberOfTowers; i++) {
            tower = new ImageView(towerColor);
            setImageView(tower, 20, 20);
            towers.getChildren().add(tower);
        }
    }

    private void addBanCards(AnchorPane island, int banCards) {
        TilePane space = new TilePane();
        ImageView banCard;

        space.setAlignment(Pos.TOP_CENTER);
        space.setPrefSize(50, 50);
        space.setLayoutX(38);
        space.setLayoutY(13);
        island.getChildren().add(space);

        for (int i = 0; i < banCards; i++) {
            banCard = new ImageView(banCardImage);
            setImageView(banCard, 23, 23);
            space.getChildren().add(banCard);
        }
    }

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

    private void setImageView(ImageView image, double width, double height, double x, double y) {
        image.smoothProperty();
        image.setPreserveRatio(true);
        image.setLayoutX(x);
        image.setLayoutY(y);
        image.setFitWidth(width);
        image.setFitHeight(height);
    }

    private void setImageView(ImageView image, double width, double height) {
        image.smoothProperty();
        image.setPreserveRatio(true);
        image.setFitWidth(width);
        image.setFitHeight(height);
    }


    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentContainer = new ToggleGroup();
        islands = new ArrayList<>();
        islandImage = new Image(String.valueOf(getClass().getResource("/Imagines/island1.png")));
        motherNature = new Image(String.valueOf(getClass().getResource("/Imagines/mother_nature.png")));
        banCardImage = new Image(String.valueOf(getClass().getResource("/Imagines/ban_card.png")));

        students = new ArrayList<>(Arrays.asList(student0, student1, student2, student3, student4, student5, student6, student7, student8));
        profs = new ArrayList<>(Arrays.asList(greenProf, redProf, yellowProf, pinkProf, blueProf));
        diningRooms = new ArrayList<>(Arrays.asList(greenDining, redDining, yellowDining, pinkDining, blueDining));

        towersPane = towers;
        towerX = 35;
        towerY = 44;
        studentX = 39;
        studentY = 30;

        entranceButtons = new ArrayList<>(Arrays.asList(entrance0, entrance1, entrance2, entrance3, entrance4, entrance5, entrance6, entrance7, entrance8));

        for (RadioButton entranceButton : entranceButtons) {
            entranceButton.setOpacity(0);
            entranceButton.setDisable(true);
        }

        cardStudents = new ArrayList<>(Arrays.asList(cardStud0, cardStud1, cardStud2, cardStud3, cardStud4, cardStud5));
        cardButtons = new ArrayList<>(Arrays.asList(cardButton0, cardButton1, cardButton2, cardButton3, cardButton4, cardButton5));
    }
}
