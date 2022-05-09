package it.polimi.ingsw.client;

import it.polimi.ingsw.model.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IslandView {
    private List<Color> students;
    private String owner;
    private int numberOfTower;
    private int banCards;

    public IslandView() {
        this.students = new ArrayList<>();
        this.owner = null;
        this.numberOfTower = 0;
        this.banCards = 0;
    }

    public IslandView(List<Color> students, String owner, int numberOfTower, int banCards) {
        this.students = students;
        this.owner = owner;
        this.numberOfTower = numberOfTower;
        this.banCards = banCards;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void addStudents(List<Color> students) {
        this.students.addAll(students);
    }

    public void removeStudent(Color student) {
        students.remove(student);
    }

    public void addTowers(int towersToAdd) {
        this.numberOfTower = numberOfTower + towersToAdd;
    }

    public void addBanCards(int banCardsToAdd) {
        banCards = banCards + banCardsToAdd;
    }

    public void removeBanCards(int banCardsToRemove) {
        banCards = banCards - banCardsToRemove;
    }

    public List<Color> getStudents() {
        return students;
    }

    public String getOwner() {
        return owner;
    }

    public int getNumberOfTower() {
        return numberOfTower;
    }

    public int getBanCards() {
        return banCards;
    }
}
