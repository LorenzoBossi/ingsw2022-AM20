package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.model.Color;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.util.List;

public abstract class BoardUpdater {

    protected GUI gui;

    protected List<ImageView> students;

    protected List<ImageView> profs;

    protected List<TilePane> diningRooms;

    protected TilePane towersPane;

    protected ImageView coinView;

    protected Label coinsLabel;

    protected double towerX;

    protected double towerY;

    protected double studentX;

    protected double studentY;


    public void updatePlayerBoard(String nickname) {
        List<Color> studentsEntrance = gui.getClientModel().getEntrances().get(nickname);
        List<Integer> studentsDin = gui.getClientModel().getDiningRooms().get(nickname);
        List<Color> profs = gui.getClientModel().getProfessors().get(nickname);
        int numberTowers = gui.getClientModel().getTowers().get(nickname);

        if(gui.getGameMode().equals("experts") && coinsLabel != null && coinView != null)
            updateCoins(gui.getClientModel().getCoins().get(nickname));

        updateEntrance(studentsEntrance);
        updateDining(studentsDin);
        updateProf(profs);
        updateTowers(numberTowers, nickname);
    }

    private void updateCoins(int coinsNumber) {
        coinsLabel.setText(coinsNumber + " x");
        coinView.setImage(new Image(String.valueOf(getClass().getResource("/Imagines/coin.png"))));
    }

    private void updateEntrance(List<Color> studentsEntrance) {
        int i = 0;
        for (Color stud : studentsEntrance) {
            students.get(i).setImage(gui.getStudentImage(stud));
            i++;
        }
    }

    private void updateProf(List<Color> professors) {
        for (Color color : professors) {
            switch (color) {
                case GREEN -> profs.get(0).setImage(gui.getProfImage(color));
                case RED -> profs.get(1).setImage(gui.getProfImage(color));
                case YELLOW -> profs.get(2).setImage(gui.getProfImage(color));
                case PINK -> profs.get(3).setImage(gui.getProfImage(color));
                case BLUE -> profs.get(4).setImage(gui.getProfImage(color));
            }
        }
    }

    private void updateTowers(int numberOfTowers, String nickname) {
        ImageView tower;

        for (int i = 0; i < numberOfTowers; i++) {
            tower = new ImageView(gui.getTowerImage(nickname));
            tower.smoothProperty();
            tower.setPreserveRatio(true);
            tower.setFitWidth(towerX);
            tower.setFitHeight(towerY);
            this.towersPane.getChildren().add(tower);
        }
    }

    public void updateDining(List<Integer> dining) {
        ImageView student;
        int numberOfStudents;

        for (Color color : Color.values()) {
            numberOfStudents = dining.get(color.ordinal());
            for (int i = 0; i < numberOfStudents; i++) {
                student = new ImageView(gui.getStudentImage(color));
                student.smoothProperty();
                student.setPreserveRatio(true);
                student.setFitWidth(studentX);
                student.setFitHeight(studentY);
                switch (color) {
                    case GREEN -> diningRooms.get(0).getChildren().add(student);
                    case RED -> diningRooms.get(1).getChildren().add(student);
                    case YELLOW -> diningRooms.get(2).getChildren().add(student);
                    case PINK -> diningRooms.get(3).getChildren().add(student);
                    case BLUE -> diningRooms.get(4).getChildren().add(student);
                }
            }
        }
    }

    public void clearDining() {
        for (TilePane pane : diningRooms)
            pane.getChildren().clear();
    }

    public void clearBoard() {
        for (ImageView student : students)
            student.setImage(null);
        for (ImageView prof : profs)
            prof.setImage(null);
        for (TilePane pane : diningRooms)
            pane.getChildren().clear();
        towersPane.getChildren().clear();
    }
}