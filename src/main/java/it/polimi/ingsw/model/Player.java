package it.polimi.ingsw.model;
<<<<<<< HEAD
import java.util.*;

public class Player {

    private final String nickname;
    private List<Assistant> hand;
    private int playerPriority;
    private int motherNatureMaxMove;
    private int coins;
    private final PlayerHand playerHand;
    private final PlayerBoard playerBoard;
    private List<Color> selectedColors;
    private int selectedIsland;
    private PlayerChoice playerChoice;

    public Player(String nickname) {
        this.nickname = nickname;
        this.hand = new ArrayList<>();
        this.playerPriority=0;
        this.motherNatureMaxMove=0;
        this.coins=0;
        this.playerHand = new PlayerHand();
        this.playerBoard = new PlayerBoard();
        this.selectedColors = new ArrayList<>();
        this.selectedIsland = 0;
        this.playerChoice = new PlayerChoice();
=======

import java.util.Objects;

public class Player {
    private String nickname;
    Player(String nickname){
        this.nickname = nickname;
>>>>>>> origin/master
    }

    public String getNickname() {
        return nickname;
    }

<<<<<<< HEAD
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

    public void setMotherNatureMaxMove(int MNMM ){
        this.motherNatureMaxMove= MNMM;
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
    /*
    public void isMotherNatureMoveLegit(int selectedMotherNatureMove){
        IslandsManager arch;
        if(selectedMotherNatureMove <= motherNatureMaxMove)
            arch = Game.getArchipelago();
            arch.moveMotherNature(selectedMotherNatureMove);
    }
    */

    public PlayerChoice getPlayerChoice(){
        return playerChoice;
    }

=======
    public void setNickname(String nickname) {
        this.nickname = nickname;
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
>>>>>>> origin/master
}
