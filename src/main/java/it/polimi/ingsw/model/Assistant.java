package it.polimi.ingsw.model;

import java.util.ArrayList;
import java.util.List;

public class Assistant {


    private boolean isPlayable;
    private AssistantName assistant;



    public Assistant(AssistantName assistantName){
    assistant=assistantName;
    isPlayable=true;
    }


    /**
     * returns true if the assistant is playable in the current pianification phase
     * returns false if the assistant has already been played during the current pianification phase
     */
    public boolean isAlreadyPlayed(){
        return !isPlayable;
    }



    /**
     * sets the assistant state to playable or not playable
     * @param playability
     * if true sets the assistant state to playable
     * if false sets the assistant state to not playable (already played by another player)
     */
    public void setPlayability(boolean playability){
        isPlayable=playability;
    }

    public AssistantName getAssistant() {
        return assistant;
    }

    public int getValue(){
        return assistant.getValue();
    }

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
