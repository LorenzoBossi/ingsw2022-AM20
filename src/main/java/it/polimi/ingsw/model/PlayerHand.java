package it.polimi.ingsw.model;
import java.util.*;
public class PlayerHand {

    private List<Assistant> assistantCards;


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
            assistantCard.setPlayability(false);
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
        boolean everyCardPlayed=true;

        for(Assistant a : assistantCards){
            if(!a.isAlreadyPlayed())
                everyCardPlayed=false;
        }

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


    public int size(){
        return assistantCards.size();
    }
}
