package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents informations about an assistant in a game, like if it has been already played in the current round
 */
public class Assistant {


    private boolean isAlreadyPlayed;
    private AssistantName assistant;


    /**
     * Constructor of assistant
     * @param assistantName the assistant you want to create
     */
    public Assistant(AssistantName assistantName){
    assistant=assistantName;
    isAlreadyPlayed=false;
    }


    /**
     * returns true if the assistant is playable in the current pianification phase
     * returns false if the assistant has already been played during the current pianification phase
     */
    public boolean isAlreadyPlayed(){
        return isAlreadyPlayed;
    }



    /**
     * sets the assistant state to playable or not playable
     * @param isPlayed
     * if true sets the assistant has been played during the last round
     * if false sets the assistant has not been played during the last round
     */
    public void setAlreadyPlayed(boolean isPlayed){
        isAlreadyPlayed=isPlayed;
    }

    public AssistantName getAssistant() {
        return assistant;
    }

    /**
     * Getter of the priority of the card
     * @return the number of the card which determines the priority
     */
    public int getValue(){
        return assistant.getValue();
    }

    /**
     * Getter of the maximum mother nature moves of the card
     * @return the number of the card which determines the priority
     */
    public int getMotherNatureMove(){
        return  assistant.getMotherNatureMove();
    }



    /**
     * creates a new List of Assistants containing every assistant in the enumeration AssistantName
     * @return the list created by the function
     */
    public static List<Assistant> getEveryAssistant(){
        List<Assistant> assistants = new ArrayList<>();

        AssistantName[] assistantNames= AssistantName.values();

        for(AssistantName assistantName: assistantNames){
            assistants.add(new Assistant(assistantName));
        }
        return assistants;
    }
}
