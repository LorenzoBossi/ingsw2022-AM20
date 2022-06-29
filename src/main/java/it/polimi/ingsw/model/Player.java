package it.polimi.ingsw.model;


import it.polimi.ingsw.network.messages.serverMessage.AssistantPlayed;
import it.polimi.ingsw.network.messages.serverMessage.MoveStudents;
import it.polimi.ingsw.network.messages.serverMessage.PlayerCoinsEvent;
import it.polimi.ingsw.utils.ObservableSubject;

import java.util.*;
import java.util.Objects;

/**
 * Represents a player of the game containing every component related to him, like playerboard, choices and assistant deck
 */
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

    /**
     * Gets the nickaname of the player
     * @return The nickname of the player
     */
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

    /**
     * Adds a new student to the dining room of the player
     * @param student the color of the student to add
     */
    public void moveStudentToDiningRoom(Color student) {
        List<Color> studentToAdd = new ArrayList<>();
        studentToAdd.add(student);

        playerBoard.moveStudentFromEntranceToDiningRoom(student);
        notifyObserver(new MoveStudents(GameComponent.ENTRANCE, GameComponent.DINING_ROOM, studentToAdd, nickname, nickname));
    }

    /**
     * Moves every student from a cloud to the player entrance
     * @param chosenCloud The cloud from which the students are taken
     * @param cloudID The id of the cloud from which the students are taken
     */
    public void addStudentFromCloud(Cloud chosenCloud, int cloudID) {
        Entrance entrance = playerBoard.getEntrance();
        List<Color> studentsToAdd = new ArrayList<>(chosenCloud.getStudents());

        entrance.addStudentFromCloud(chosenCloud);
        chosenCloud.setChosen(true);
        notifyObserver(new MoveStudents(GameComponent.CLOUD, GameComponent.ENTRANCE, studentsToAdd, cloudID, nickname));
    }

    /**
     * Gets the player hand , which contains and manage all the assistants of the player
     * @return the PlayerHand of the player
     */
    public PlayerHand getPlayerHand() {
        return playerHand;
    }

    /**
     * Sets the priotity of the player in the action phase.
     * @param newPriority numbeber of priority contained on the assistant played during the pianification phase
     */
    public void setPlayerPriority(int newPriority) {
        this.playerPriority = newPriority;
    }

    /**
     * Gets the priority of the player in the current round
     * @return an integer corresponding to the priority of the last assistan player
     */
    public int getPlayerPriority() {
        return this.playerPriority;
    }

    /**
     * Sets the max movements of mother nature the player can make in the action phase.
     * @param MotherNatureMaxMove number of mother nature moves contained on the assistant played during the pianification phase
     */
    public void setMotherNatureMaxMove(int MotherNatureMaxMove) {
        this.motherNatureMaxMove = MotherNatureMaxMove;
    }

    /**
     * Gets the max number of movements of mother nature the player can make in the action phase.
     * @return an integer corresponding to the maximum movements of mother nature the player can make in the action phase.
     */
    public int getMotherNatureMaxMove() {
        return this.motherNatureMaxMove;
    }

    /**
     * Gets the playerboard of the player conatining entrance, dining room and tower.
     * @return The PlayerBoard of the player
     */
    public PlayerBoard getPlayerBoard() {
        return this.playerBoard;
    }

    /**
     * Sets the number of coins owned by the player
     * @param amount number of coins to set
     */
    public void setCoins(int amount) {
        this.coins = amount;
    }
    /**
     * Gets the number of coins owned by the player
     * @return number of coins
     */
    public int getCoins() {
        return this.coins;
    }

    /**
     * Gives the player one more coin
     */
    public void addCoin() {
        this.coins++;
    }

    /**
     * Gets the number of students movements the player can still do in the current round
     * @return number of movements the player can do
     */
    public int getAvailableMoves() {
        return availableMoves;
    }

    /**
     * Sets the number of students movements the player can still do in the current round
     */
    public void setAvailableMoves(int availableMoves) {
        this.availableMoves = availableMoves;
    }

    /**
     * Remove from the player the specified number of coins if he has enough. Otherwise it do nothing.
     * @param payment number of coins to take
     */
    public void useCoins(int payment) {
        if (isEnoughCoin(payment))
            this.coins = this.coins - payment;
    }

    /**
     * Tells if the player has more or equals coins than the specified number
     * @param coinRequired number of coins to compare with the player coins
     * @return true if the player has enough coins, false otherwise
     */
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


    /**
     * Gets the player choice containing all the choices made by the player regarding character cards.
     * @return a PlayerCoice object containing all the player choices.
     */
    public PlayerChoice getPlayerChoice() {
        return playerChoice;
    }

    /**
     * Sets the player choice used by character cards
     * @param playerChoice PlayerChoice object to set
     */
    public void setPlayerChoice(PlayerChoice playerChoice) {
        this.playerChoice = playerChoice;
    }

    /**
     * Tells if the player is equals to the specified object.
     * In our case two players are equals if they has the same nickname
     * @param o object to compare with the player
     * @return true if equals, false otherwise
     */
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
