package it.polimi.ingsw.model;


import it.polimi.ingsw.network.messages.serverMessage.AssistantPlayed;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;
import it.polimi.ingsw.network.messages.serverMessage.PlayerCoinsEvent;
import it.polimi.ingsw.utils.ObservableSubject;

import java.util.*;
import java.util.Objects;

public class Player extends ObservableSubject {

    private final String nickname;
    private List<Assistant> hand;
    private int playerPriority;
    private int motherNatureMaxMove;
    private int coins;
    private PlayerHand playerHand;
    private final PlayerBoard playerBoard;
    private PlayerChoice playerChoice;
    private int availableMoves;

    /**
     * Constructor
     *
     * @param nickname nickname of the player
     */

    public Player(String nickname) {
        this.nickname = nickname;
        this.hand = new ArrayList<>();
        this.playerPriority = 0;
        this.motherNatureMaxMove = 0;
        this.coins = 0;
        this.playerHand = new PlayerHand(null);
        this.playerBoard = new PlayerBoard();
        this.playerChoice = new PlayerChoice();
        this.availableMoves = 0;
    }

    /**
     * gives the player all the references to the assistants shared by every player in the game
     *
     * @param assistants a list of Assistant created by the game and given to every player
     */
    public void setAssistants(List<Assistant> assistants) {
        playerHand = new PlayerHand(assistants);
    }

    /**
     * Constructor
     *
     * @param nickname   nickname of the player
     * @param assistants a list of Assistant created by the game and given to every player
     */
    public Player(String nickname, List<Assistant> assistants) {
        this.nickname = nickname;
        this.playerPriority = 0;
        this.motherNatureMaxMove = 0;
        this.coins = 0;
        this.playerHand = new PlayerHand(assistants);
        this.playerBoard = new PlayerBoard();
        this.playerChoice = new PlayerChoice();
    }


    public String getNickname() {
        return nickname;
    }

    /**
     * If the specified assistant is playable in the current turn this method sets the priority and the max mother nature moves of the player
     * and removes the assistant from the player hand.
     *
     * @param assistant the assistant to play
     * @return {@code true} if the assistant is playable
     * {@code false} if the assistant is not playable
     */
    public boolean playAssistant(Assistant assistant) {
        if (playerHand.getPlayableAssistants().contains(assistant)) {
            playerPriority = assistant.getValue();
            motherNatureMaxMove = assistant.getMotherNatureMove();
            playerHand.remove(assistant);
            notifyObserver(new AssistantPlayed(nickname, assistant.getAssistant()));
            return true;
        }
        return false;
    }

    public void moveStudentToDiningRoom(Color student) {
        List<Color> studentToAdd = new ArrayList<>();
        studentToAdd.add(student);

        playerBoard.moveStudentFromEntranceToDiningRoom(student);
        notifyObserver(new MoveStudents(GameComponent.ENTRANCE, GameComponent.DINING_ROOM, studentToAdd, nickname, nickname));
    }

    public void addStudentFromCloud(Cloud chosenCloud, int cloudID) {
        Entrance entrance = playerBoard.getEntrance();
        List<Color> studentsToAdd = new ArrayList<>(chosenCloud.getStudents());

        entrance.addStudentFromCloud(chosenCloud);
        chosenCloud.setChosen(true);
        notifyObserver(new MoveStudents(GameComponent.CLOUD, GameComponent.ENTRANCE, studentsToAdd, cloudID, nickname));
    }

    public PlayerHand getPlayerHand() {
        return playerHand;
    }

    public void setPlayerPriority(int newPriority) {
        this.playerPriority = newPriority;
    }

    public int getPlayerPriority() {
        return this.playerPriority;
    }

    public void setMotherNatureMaxMove(int MotherNatureMaxMove) {
        this.motherNatureMaxMove = MotherNatureMaxMove;
    }

    public int getMotherNatureMaxMove() {
        return this.motherNatureMaxMove;
    }

    public PlayerBoard getPlayerBoard() {
        return this.playerBoard;
    }

    public void setCoins(int amount) {
        this.coins = amount;
    }

    public int getCoins() {
        return this.coins;
    }

    public void addCoin() {
        this.coins++;
    }

    public int getAvailableMoves() {
        return availableMoves;
    }

    public void setAvailableMoves(int availableMoves) {
        this.availableMoves = availableMoves;
    }

    public void useCoins(int payment) {
        if (isEnoughCoin(payment))
            this.coins = this.coins - payment;
    }

    private boolean isEnoughCoin(int coinRequired) {
        return this.coins >= coinRequired;
    }


    /**
     * Method isMotherNatureMoveLegit checks if the player can move Mother Nature how much he wants
     *
     * @param selectedMotherNatureMove how much the player wants to move Mother Nature
     * @return {@code true} if the selected Mother Nature move is legit,
     * {@code false} if the selected Mother Nature move is  illicit
     */
    public boolean isMotherNatureMoveLegit(int selectedMotherNatureMove) {
        return selectedMotherNatureMove <= motherNatureMaxMove && selectedMotherNatureMove > 0;
    }


    public PlayerChoice getPlayerChoice() {
        return playerChoice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(nickname, player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }

}
