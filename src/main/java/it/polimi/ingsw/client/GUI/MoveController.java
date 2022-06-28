package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.ActionMove;
import it.polimi.ingsw.client.IslandView;
import it.polimi.ingsw.model.Color;
import it.polimi.ingsw.model.characterCards.CharacterName;
import it.polimi.ingsw.network.messages.clientMessage.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class MoveController is the controller of the Move Scene where the client perform the action that involved
 * the move of students or the selection of the island
 */
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


    /**
     * Update the Move Scene
     */
    public void update() {
        String nickname = gui.getClientNickname();
        updatePlayerBoard(nickname);
        initEntranceButton(gui.getClientModel().getEntrances().get(nickname));
        updateIslands(gui.getClientModel().getIslandsViewMap());
    }

    /**
     * Init the student entrance button
     *
     * @param students the student in the entrance
     */
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

    /**
     * Updates the island
     * @param islandViewMap the islands map
     */
    private void updateIslands(Map<Integer, IslandView> islandViewMap) {
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

    /**
     * Adds students to the island
     * @param island the pane where island is
     * @param students the students to add on the island
     */
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

    /**
     * Adds towers to the islands
     * @param island the pane where island is
     * @param owner the owner of the island
     * @param numberOfTowers the number of towers on the islands
     */
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
        space.setLayoutX(38);
        space.setLayoutY(13);
        island.getChildren().add(space);

        for (int i = 0; i < banCards; i++) {
            banCard = new ImageView(banCardImage);
            setImageView(banCard, 23, 23);
            space.getChildren().add(banCard);
        }
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
     * Handle the MOVE_STUDENTS action
     */
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

    /**
     * Init the button to perform the MOVE_STUDENTS action
     * @param confirmButton the button to confirm the action
     */
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

    /**
     * Init the button to select one island
     * @param name the name of the character card
     */
    public void initIslandSelection(CharacterName name) {
        Label message = makeLabel("Select one island");
        Button confirmButton = makeConfirmButton();
        diningButton.setDisable(true);
        diningButton.setOpacity(0);
        messagePane.getChildren().add(cardPane);
        cardImage.setImage(gui.getCharacterImage(name));

        updatePlayerBoard(gui.getClientNickname());
        updateIslands(gui.getClientModel().getIslandsViewMap());

        for (RadioButton button : cardButtons) {
            button.setDisable(true);
            button.setOpacity(0);
        }

        confirmButton.setOnAction(actionEvent -> {
            int islandId = (int) studentContainer.getSelectedToggle().getUserData();
            gui.sendMessage(new SelectedIsland(gui.getClientNickname(), islandId));
            gui.consumeAction(ActionMove.ACTIVATE_CARD);
            gui.sendMessage(new ActiveEffect(gui.getClientNickname(), name));
        });


        studentContainer.selectedToggleProperty().addListener((observableValue, toggle, t1) -> confirmButton.setDisable(false));

        messagePane.getChildren().addAll(Arrays.asList(message, confirmButton));
    }

    /**
     * Handles the activation of the princess
     * @param students the students on the princess
     */
    public void initPrincessActivation(List<Color> students) {
        Button confirmButton = makeConfirmButton();
        Label message = makeLabel("Select one student to move to the Dining Room");
        ToggleGroup cardGroup = new ToggleGroup();

        messagePane.getChildren().add(cardPane);
        cardImage.setImage(gui.getCharacterImage(CharacterName.PRINCESS));
        addStudentsToCard(students, cardGroup);

        diningButton.setOpacity(0);
        studentContainer.getToggles().forEach(t -> {
            Node node = (Node) t;
            node.setDisable(true);
            node.setOpacity(0);

        });

        cardGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> confirmButton.setDisable(false));

        confirmButton.setOnAction(actionEvent -> {
            Color student = (Color) cardGroup.getSelectedToggle().getUserData();
            List<Color> students1 = new ArrayList<>();
            students1.add(student);
            gui.sendMessage(new SelectedStudentsFromCard(gui.getClientNickname(), students1, CharacterName.PRINCESS));
            gui.consumeAction(ActionMove.ACTIVATE_CARD);
            gui.sendMessage(new ActiveEffect(gui.getClientNickname(), CharacterName.PRINCESS));

        });

        messagePane.getChildren().addAll(confirmButton, message);
    }

    /**
     * Handles the activation of the jester
     * @param students the students on the jester
     */
    public void initJesterActivation(List<Color> students) {
        Label message = makeLabel("Select how much students you want to\nmove from the card");
        Button confirmButton = makeConfirmButton();
        ChoiceBox<String> choiceBox = makeChoiceBoxCards();
        ToggleGroup cardGroup = new ToggleGroup();

        diningButton.setDisable(true);
        diningButton.setOpacity(0);
        messagePane.getChildren().add(cardPane);
        cardImage.setImage(gui.getCharacterImage(CharacterName.JESTER));

        addStudentsToCard(students, cardGroup);

        messagePane.getChildren().addAll(confirmButton, choiceBox, message);
        initJesterButtons(cardGroup, choiceBox, confirmButton, message);
    }

    /**
     * Init the button to choose how much switch the client want to do
     * @param cardGroup  the toggle group of the students on the card
     * @param choiceBox the choiceBox to choose how many switch the client wants to do
     * @param confirmButton the button to confirm how many switch the client wants to do
     * @param message the message to show to the client
     */
    private void initJesterButtons(ToggleGroup cardGroup, ChoiceBox<String> choiceBox, Button confirmButton, Label message) {
        AtomicInteger numberOfSwaps = new AtomicInteger();

        cardGroup.getToggles().forEach(t -> {
            Node node = (Node) t;
            node.setDisable(true);
            node.setOpacity(0);
        });
        studentContainer.getToggles().forEach(t -> {
            Node node = (Node) t;
            node.setDisable(true);
            node.setOpacity(0);
        });

        choiceBox.getItems().addAll(Arrays.asList("1", "2", "3"));
        choiceBox.setOnAction(actionEvent -> confirmButton.setDisable(false));

        confirmButton.setOnAction(actionEvent -> {
            numberOfSwaps.set(Integer.parseInt(choiceBox.getValue()));
            confirmButton.setDisable(true);
            confirmButton.setOpacity(0.5);
            confirmButton.getProperties().clear();
            messagePane.getChildren().remove(choiceBox);
            message.setText("Select one students from the card and one student\n from the entrance");

            selectStudentsJester(numberOfSwaps.get(), confirmButton, cardGroup);

        });
    }

    /**
     * Handles the switch of students from the jester to the entrance
     * @param swaps the number of swaps the client need to perform
     * @param confirmButton the button to confirm the switch
     * @param cardGroup the toggle group of the students on the card
     */
    private void selectStudentsJester(int swaps, Button confirmButton, ToggleGroup cardGroup) {
        List<Color> studentsFromCards = new ArrayList<>();
        List<Color> studentsFromEntrance = new ArrayList<>();
        AtomicInteger numberOfSwaps = new AtomicInteger(swaps);

        cardGroup.getToggles().forEach(t -> {
            Node node = (Node) t;
            node.setDisable(false);
            node.setOpacity(1);
        });
        cardGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (entrance.getSelectedToggle() != null) {
                confirmButton.setDisable(false);
                confirmButton.setOpacity(1);
            }
        });

        initEntranceButton(gui.getClientModel().getEntrances().get(gui.getClientNickname()));
        entrance.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (cardGroup.getSelectedToggle() != null) {
                confirmButton.setDisable(false);
                confirmButton.setOpacity(1);
            }
        });

        confirmButton.setOnAction(actionEvent -> {
                Toggle cardStudent = cardGroup.getSelectedToggle();
                Toggle entranceStudent = entrance.getSelectedToggle();
                cardGroup.getSelectedToggle().setSelected(false);
                entrance.getSelectedToggle().setSelected(false);

                studentsFromCards.add((Color) cardStudent.getUserData());
                studentsFromEntrance.add((Color) entranceStudent.getUserData());

                ((Node) cardStudent).setDisable(true);
                ((Node) entranceStudent).setDisable(true);
                ((Node) cardStudent).setOpacity(0);
                ((Node) entranceStudent).setOpacity(0);

                confirmButton.setDisable(true);
                confirmButton.setOpacity(0.5);

                numberOfSwaps.set(numberOfSwaps.get() - 1);

                if (numberOfSwaps.get() == 0) {
                    gui.sendMessage(new SelectedStudentsFromEntrance(gui.getClientNickname(), studentsFromEntrance));
                    gui.sendMessage(new SelectedStudentsFromCard(gui.getClientNickname(), studentsFromCards, CharacterName.JESTER));
                    gui.consumeAction(ActionMove.ACTIVATE_CARD);
                    gui.sendMessage(new ActiveEffect(gui.getClientNickname(), CharacterName.JESTER));
                }
        });
    }

    /**
     * Init the Musician activation
     */
    public void initMusicianActivation() {
        String nickname = gui.getClientNickname();
        Label message = makeLabel("Select how much students you want to\nswap from the entrance");
        Button confirmButton = makeConfirmButton();
        ChoiceBox<String> choiceBox = makeChoiceBoxCards();
        List<Integer> dining = new ArrayList<>(gui.getClientModel().getDiningRooms().get(nickname));

        updatePlayerBoard(gui.getClientNickname());
        updateIslands(gui.getClientModel().getIslandsViewMap());


        diningButton.setDisable(true);
        diningButton.setOpacity(0);
        messagePane.getChildren().add(cardPane);
        cardImage.setImage(gui.getCharacterImage(CharacterName.MUSICIAN));

        messagePane.getChildren().addAll(confirmButton, choiceBox, message);

        initMusicianButtons(choiceBox, confirmButton, message, dining);
    }

    /**
     * Init the button to choose how many student you want to switch with the student in the dining room
     * @param choiceBox the choiceBox to make the client choose how many switch he wants to perform
     * @param confirmButton the button to confirm how many switch the client want to perform
     * @param message the message to show to the client
     * @param dining the dining
     */
    private void initMusicianButtons(ChoiceBox<String> choiceBox, Button confirmButton, Label message, List<Integer> dining) {
        AtomicInteger numberOfSwaps = new AtomicInteger();
        int totalNumberOfStudents = 0;

        for(int students : dining)
            totalNumberOfStudents+=students;

        choiceBox.getItems().addAll("1", "2");
        choiceBox.setOnAction(actionEvent -> confirmButton.setDisable(false));

        studentContainer.getToggles().forEach(t -> {
            Node node = (Node) t;
            node.setDisable(true);
            node.setOpacity(0);
        });

        int finalTotalNumberOfStudents = totalNumberOfStudents;
        confirmButton.setOnAction(actionEvent -> {
            numberOfSwaps.set(Integer.parseInt(choiceBox.getValue()));
            if(numberOfSwaps.get() > finalTotalNumberOfStudents) {
                gui.alertMessage("You don't have enough students in you dining room");
                confirmButton.setDisable(true);
            } else {
                choiceBox.getItems().clear();
                choiceBox.getProperties().clear();
                choiceBox.getItems().addAll(Color.GREEN.name(), Color.RED.name(), Color.YELLOW.name(), Color.PINK.name(), Color.BLUE.name());

                confirmButton.getProperties().clear();
                confirmButton.setDisable(true);
                confirmButton.setOpacity(0.5);

                message.setText("Choose the color of the student in the dining room and the student\n in the entrance you want to exchange with");
                selectStudentsMusician(choiceBox, dining, confirmButton, numberOfSwaps.get());
            }
        });
    }

    /**
     * Handles the switch of students from the entrance to the dining room
     *
     * @param choiceBox the choiceBox to make the client choose the color of the student in the dining room
     * @param dining the dining
     * @param confirmButton the button to confirm the switch
     * @param swaps the number of swaps
     */
    public void selectStudentsMusician(ChoiceBox<String> choiceBox, List<Integer> dining, Button confirmButton, int swaps) {
        AtomicInteger numberOfSwaps = new AtomicInteger(swaps);
        List<Color> entranceStudents = new ArrayList<>();
        List<Color> diningStudents = new ArrayList<>();

        initEntranceButton(gui.getClientModel().getEntrances().get(gui.getClientNickname()));
        entrance.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (choiceBox.getValue() != null) {
                confirmButton.setDisable(false);
                confirmButton.setOpacity(1);
            }
        });

        choiceBox.setOnAction(actionEvent -> {
            if(entrance.getSelectedToggle() != null) {
                confirmButton.setDisable(false);
                confirmButton.setOpacity(1);
            }
        });

        confirmButton.setOnAction(actionEvent -> {
            Toggle selectedEntrance = entrance.getSelectedToggle();
            Color colorDin = Color.valueOf(choiceBox.getValue());
            Color colorEnt = (Color) selectedEntrance.getUserData();

            if(dining.get(colorDin.ordinal()) == 0) {
                gui.alertMessage("You don't have student of these color in your dining room");
                confirmButton.setDisable(true);
                confirmButton.setOpacity(0.5);
            } else {
                diningStudents.add(colorDin);
                dining.set(colorDin.ordinal(), dining.get(colorDin.ordinal()) -  1);

                entranceStudents.add(colorEnt);
                entrance.getSelectedToggle().setSelected(false);
                ((Node)selectedEntrance).setDisable(true);
                ((Node) selectedEntrance).setOpacity(0);

                confirmButton.setDisable(true);
                confirmButton.setOpacity(0.5);

                clearDining();
                updateDining(dining);
                numberOfSwaps.set(numberOfSwaps.get() - 1);
                if(numberOfSwaps.get() == 0) {
                    gui.sendMessage(new SelectedStudentsFromEntrance(gui.getClientNickname(), entranceStudents));
                    gui.sendMessage(new SelectedStudentsFromCard(gui.getClientNickname(), diningStudents, CharacterName.MUSICIAN));
                    gui.consumeAction(ActionMove.ACTIVATE_CARD);
                    gui.sendMessage(new ActiveEffect(gui.getClientNickname(), CharacterName.MUSICIAN));
                }
            }
        });
    }

    /**
     * Init the monk activation
     * @param students the students on the card
     */
    public void initMonkActivation(List<Color> students) {
        Label message = makeLabel("Select one island and one student from the card");
        Button confirmButton = makeConfirmButton();
        ToggleGroup cardGroup = new ToggleGroup();

        diningButton.setDisable(true);
        diningButton.setOpacity(0);
        messagePane.getChildren().add(cardPane);
        cardImage.setImage(gui.getCharacterImage(CharacterName.MONK));

        addStudentsToCard(students, cardGroup);

        messagePane.getChildren().addAll(confirmButton, message);
        initMonkButtonProperty(cardGroup, confirmButton);
    }

    /**
     * Init the button to activate the Monk
     * @param cardGroup the toggle group of the student on the card
     * @param confirmButton the button to confirm the action
     */
    private void initMonkButtonProperty(ToggleGroup cardGroup, Button confirmButton) {
        cardGroup.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (studentContainer.getSelectedToggle() != null)
                confirmButton.setDisable(false);
        });

        studentContainer.selectedToggleProperty().addListener((observableValue, toggle, t1) -> {
            if (cardGroup.getSelectedToggle() != null)
                confirmButton.setDisable(false);
        });

        confirmButton.setOnAction(actionEvent -> {
            List<Color> student = new ArrayList<>();
            student.add((Color) cardGroup.getSelectedToggle().getUserData());
            int islandId = (int) studentContainer.getSelectedToggle().getUserData();
            cardGroup.getToggles().clear();
            gui.sendMessage(new SelectedIsland(gui.getClientNickname(), islandId));
            gui.sendMessage(new SelectedStudentsFromCard(gui.getClientNickname(), student, CharacterName.MONK));
            gui.consumeAction(ActionMove.ACTIVATE_CARD);
            gui.sendMessage(new ActiveEffect(gui.getClientNickname(), CharacterName.MONK));
        });
    }

    /**
     * Adds student to the card
     * @param students the students to add
     * @param cardGroup the toggle group of the student on the card
     */
    private void addStudentsToCard(List<Color> students, ToggleGroup cardGroup) {
        ImageView studImage;
        RadioButton button;
        int i = 0;

        updateIslands(gui.getClientModel().getIslandsViewMap());
        updatePlayerBoard(gui.getClientNickname());

        for (Color student : students) {
            studImage = cardStudents.get(i);
            studImage.setImage(gui.getStudentImage(student));
            button = cardButtons.get(i);
            button.setUserData(student);
            button.setOpacity(1);
            button.setDisable(false);
            cardGroup.getToggles().add(button);
            i++;
        }
    }

    /**
     * Make the choiceBox for the character card
     * @return the choiceBox
     */
    private ChoiceBox<String> makeChoiceBoxCards() {
        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.setPrefWidth(150);
        choiceBox.setLayoutX(376);
        choiceBox.setLayoutY(124);
        choiceBox.setDisable(false);
        return choiceBox;
    }

    /**
     * Make one confirm button
     * @return the button
     */
    private Button makeConfirmButton() {
        Button button = new Button();
        button.setFont(new Font("system", 20));
        button.setText("Confirm");
        button.setLayoutX(402);
        button.setLayoutY(160);
        button.setDisable(true);
        return button;
    }

    /**
     * Make the character label
     * @param text the text to show to the client
     * @return the label
     */
    private Label makeLabel(String text) {
        Label label = new Label(text);
        label.setFont(new Font("system", 15));
        label.setAlignment(Pos.CENTER);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setPrefSize(360, Region.USE_COMPUTED_SIZE);
        label.setLayoutX(271);
        label.setLayoutY(77);
        return label;
    }

    /**
     * Clears the Move Scene
     */
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

        for(ImageView student : cardStudents) {
            student.setImage(null);
        }

        for (RadioButton button : cardButtons) {
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

    /**
     * Permits the client to return to the previous scene
     */
    public void backScene() {
        clear();
        gui.updateMyPlayerBoard();
        gui.changeScene("/Scene/actionScene.fxml");
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        studentContainer = new ToggleGroup();
        islands = new ArrayList<>();
        islandImage = new Image(String.valueOf(getClass().getResource("/Images/island1.png")));
        motherNature = new Image(String.valueOf(getClass().getResource("/Images/mother_nature.png")));
        banCardImage = new Image(String.valueOf(getClass().getResource("/Images/ban_card.png")));

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
