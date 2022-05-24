package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.model.Color;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.*;

public class PlayerBoardController implements GUIController, Initializable {
    private GUI gui;

    private Scene previousScene;

    private List<ImageView> students;

    private List<ImageView> profs;

    private List<ImageView> towersList;

    private Map<Color, List<ImageView>> studentsDinMap;

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

    public void update(String nickname) {
        List<Color> studentsInEntrance = gui.getClientModel().getEntrances().get(nickname);
        List<Integer> studentInDin = gui.getClientModel().getDiningRooms().get(nickname);
        List<Color> prof = gui.getClientModel().getProfessors().get(nickname);
        int numberOfTowers = gui.getClientModel().getTowers().get(nickname);
        int i = 0;

        for (Color color : studentsInEntrance) {
            students.get(i).setImage(gui.getStudentPath(color));
            i++;
        }

        for (i = 0; i < numberOfTowers; i++)
            towers.getChildren().add(towersList.get(i));

        ImageView studDin;
        for (Color color : Color.values()) {
            for (i = 0; i < studentInDin.get(color.ordinal()); i++) {
                studDin = studentsDinMap.get(color).get(i);
                switch (color) {
                    case BLUE -> {
                        studDin.setImage(gui.getStudentPath(color));
                        blueDining.getChildren().add(studDin);
                    }
                    case YELLOW -> {
                        studDin.setImage(gui.getStudentPath(color));
                        yellowDining.getChildren().add(studDin);
                    }
                    case RED -> {
                        studDin.setImage(gui.getStudentPath(color));
                        redDining.getChildren().add(studDin);
                    }
                    case PINK -> {
                        studDin.setImage(gui.getStudentPath(color));
                        pinkDining.getChildren().add(studDin);
                    }
                    case GREEN -> {
                        studDin.setImage(gui.getStudentPath(color));
                        greenDining.getChildren().add(studDin);
                    }
                }
            }
        }

        for (Color color : prof) {
            switch (color) {
                case BLUE -> blueProf.setImage(gui.getStudentPath(color));
                case YELLOW -> yellowProf.setImage(gui.getStudentPath(color));
                case RED -> redProf.setImage(gui.getStudentPath(color));
                case PINK -> pinkProf.setImage(gui.getStudentPath(color));
                case GREEN -> greenProf.setImage(gui.getStudentPath(color));
            }
        }

    }

    public void clear() {
        for(ImageView student : students)
            student.setImage(null);
        for(ImageView prof : profs)
            prof.setImage(null);
        yellowDining.getChildren().clear();
        redDining.getChildren().clear();
        greenDining.getChildren().clear();
        blueDining.getChildren().clear();
        pinkDining.getChildren().clear();
        towers.getChildren().clear();
    }

    public void exit() {
        clear();
        gui.changeScene("/Scene/pianificationScene.fxml");
    }

    public void setPreviousScene(Scene previousScene) {
        this.previousScene = previousScene;
    }

    @Override
    public void setGui(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.studentsDinMap = new HashMap<>();
        for(Color color : Color.values())
            studentsDinMap.put(color, new ArrayList<>());
        this.towersList = new ArrayList<>();
        this.students = new ArrayList<>(Arrays.asList(student0, student1, student2, student3, student4, student5, student6, student7, student8));
        this.profs = new ArrayList<>(Arrays.asList(redProf, pinkProf, greenProf, yellowProf, blueProf));
        ImageView tower;
        ImageView studDin;

        for(int i = 0; i < 9 ; i++) {
            tower = new ImageView();
            tower.setPreserveRatio(true);
            tower.smoothProperty();
            tower.setFitHeight(52);
            tower.setFitWidth(48);
            tower.setImage(new Image(String.valueOf(getClass().getResource("/Imagines/black_tower.png"))));
            towersList.add(tower);
        }

        for(Color color : Color.values()) {
            for(int i = 0; i < 10; i++) {
                studDin = new ImageView();
                studDin.setPreserveRatio(true);
                studDin.smoothProperty();
                studDin.setFitHeight(39);
                studDin.setFitWidth(39);
                studentsDinMap.get(color).add(studDin);
            }
        }
    }
}
