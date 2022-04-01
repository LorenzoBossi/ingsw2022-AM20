package it.polimi.ingsw.model;
import java.util.*;
import java.util.Objects;

public class Player {

    private final String nickname;
    private List<Assistant> hand;
    private int playerPriority;
    private int motherNatureMaxMove;
    private int coins;
    private final PlayerHand playerHand;
    private final PlayerBoard playerBoard;
    private PlayerChoice playerChoice;

    public Player(String nickname) {
        this.nickname = nickname;
        this.hand = new ArrayList<>();
        this.playerPriority = 0;
        this.motherNatureMaxMove = 0;
        this.coins = 0;
        this.playerHand = new PlayerHand(hand);
        this.playerBoard = new PlayerBoard();
        this.playerChoice = new PlayerChoice();
    }



    public String getNickname() {
        return nickname;
    }

    /*
    public boolean playAssistant(Assistant assistant){
        int index = hand.indexOf(assistant);
        Assistant card = hand.get(index);
        if(card.isAlreadyPlayed(assistant) && this.playerHand.isPresent(assistant)){
            this.setMotherNatureMaxMove(assistant.getMotherNatureMove());
            this.setPlayerPriority(assistant.getValue());
        }
    }

     */

    public void setPlayerPriority(int newPriority){
        this.playerPriority = newPriority;
    }
    public int getPlayerPriority() {
        return this.playerPriority;
    }

    public void setMotherNatureMaxMove(int MOtherNatureMaxMove ){
        this.motherNatureMaxMove= MOtherNatureMaxMove ;
    }
    public int getMotherNatureMaxMove() {
        return this.motherNatureMaxMove;
    }

    public PlayerBoard getPlayerBoard(){
        return this.playerBoard;
    }


    public void setCoins(int amount) {
        this.coins=amount;
    }
    public int getCoins() {
        return this.coins;
    }

    public void addCoin(){
        this.coins++;
    }
    public void useCoins(int payment){
        if(isEnoughCoin(payment))
            this.coins=this.coins-payment;
        else
            System.out.println("impossibile fare operazione");
    }

    private boolean isEnoughCoin(int coinRequired){
        return this.coins >= coinRequired;
    }


    /**
     * Method isMotherNatureMoveLegit checks if the player can move Mother Nature how much he wants
     * @param selectedMotherNatureMove how much the player wants to move Mother Nature
     * @return {@code true} if the selected Mother Nature move is legit,
     *         {@code false} if the selected Mother Nature move is  illicit
     */
    public boolean isMotherNatureMoveLegit(int selectedMotherNatureMove){
        if(selectedMotherNatureMove <= motherNatureMaxMove && selectedMotherNatureMove > 0)
            return true;
        return false;
    }


    public PlayerChoice getPlayerChoice(){
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
