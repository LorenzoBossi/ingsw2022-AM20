package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.model.Color;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;

import java.util.List;

/**
 * Class that contains the method to update the playerboard
 */
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


    /**
     * Updates the playerBoard
     * @param nickname the owner of the playerBoard
     */
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

    /**
     * Updates the number of coins of the player
     * @param coinsNumber the number of coins of the player
     */
    private void updateCoins(int coinsNumber) {
        coinsLabel.setText(coinsNumber + " x");
        coinView.setImage(new Image(String.valueOf(getClass().getResource("/Images/coin.png"))));
    }

    /**
     * Update the students in the entrance
     * @param studentsEntrance the students in the entrance
     */
    private void updateEntrance(List<Color> studentsEntrance) {
        int i = 0;
        for (Color stud : studentsEntrance) {
            students.get(i).setImage(gui.getStudentImage(stud));
            i++;
        }
    }

    /**
     * Updates the professors
     * @param professors the professors
     */
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

    /**
     * Updates the towers
     * @param numberOfTowers the number of towers
     * @param nickname the nickname of the players
     */
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

    /**
     * Updates the dining
     * @param dining the dining to update
     */
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

    /**
     * Clears the dining
     */
    public void clearDining() {
        for (TilePane pane : diningRooms)
            pane.getChildren().clear();
    }

    /**
     * Clear the playerBoard
     */
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
