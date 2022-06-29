package it.polimi.ingsw.model;
import java.util.*;

/**
 * Manages the deck of the player containing assistant cards
 */
public class PlayerHand {

    private List<Assistant> assistantCards;

    /**
     * Constructor of the PlayerHand, which initialize the deck with the list of assistants given as parameter.
     * @param assistants list of assistants to add
     */
    public PlayerHand(List<Assistant> assistants){
        assistantCards = new LinkedList<>();
        if(assistants!=null) {
            assistantCards.addAll(assistants);
        }
    }

    /**
     * removes the specified Assistant from the player hand and update the Assistant state to already played.
     * If the specified Assistant is not in the player hand, it does nothing
     * @param assistantCard specify which assistant to remove
     */
    public void remove(Assistant assistantCard){
        if(assistantCards.contains(assistantCard)) {
            assistantCard.setAlreadyPlayed(true);
            assistantCards.remove(assistantCard);
        }
    }

    /**
     *
     * @param assistantCard Assistant of which check the presence
     * @return
     * rerurns true if the specified card is in the player hand,
     * returns false otherwise
     */
    public boolean isPresent(Assistant assistantCard){
        return assistantCards.contains(assistantCard);
    }


    /**
     * get the assistants the player can play during his pianification phase
     * @return list containing the assistants the player
     */
    public List<Assistant> getPlayableAssistants(){
        List<Assistant> playable= new LinkedList<>();

        boolean everyCardPlayed=isEveryCardAlreadyPlayed();

        if(everyCardPlayed){
            playable.addAll(assistantCards);
        }else
        {
            for(Assistant a : assistantCards)
                if(!a.isAlreadyPlayed())
                    playable.add(a);
        }

        return playable;
    }


    /**
     * tells if the player can play the specified assistant
     * @param assistant
     * @return
     * {@code true} the player can play the specified assistant
     * {@code false} the player can not play the specified assistant
     */
    public boolean canPlay(Assistant assistant){
        if(!assistantCards.contains(assistant))
            return false;

        boolean isEveryCardPlayed=isEveryCardAlreadyPlayed();

        if(isEveryCardPlayed){
            return true;
        }else{
            if(assistant.isAlreadyPlayed())
                return false;
            else
                return true;
        }
    }


    /**
     * tells if every assistant in the player hand has been already played in the current round
     * @return
     * {@code true} every assistant in the player hand has been already played in the current round
     * {@code false} not every assistant in the player hand has been already played in the current round
     */
    private boolean isEveryCardAlreadyPlayed(){
        boolean isEveryCardPlayed=true;
        for(Assistant a : assistantCards){
            if(!a.isAlreadyPlayed())
                isEveryCardPlayed=false;
        }
        return isEveryCardPlayed;
    }


    /**
     * Gets the number of assistant cards are there in the deck
     * @return
     */
    public int size(){
        return assistantCards.size();
    }
}
