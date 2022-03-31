package it.polimi.ingsw.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;



/**
 * Unit test for simple App.
 */
public class AssistantsTest
{
    /**
     * TESTING:
     * -removing an assistant set the assistant to already played (if it is in the player hand)
     * -after removing an assistant, the assistant is not in the player hand
     */
    @Test
    public void removeTest(){
        AssistantName[] assistantNames= AssistantName.values();
        List<Assistant> assistants= new ArrayList<>();

        for(AssistantName assistantName: assistantNames)
            assistants.add(new Assistant(assistantName));

        PlayerHand playerHand=new PlayerHand(assistants);
        assertTrue(playerHand.size()== assistantNames.length);

        Assistant assistant3=assistants.get(3);
        assertTrue(assistant3.isAlreadyPlayed() == false);

        playerHand.remove(assistant3);
        assertTrue(assistant3.isAlreadyPlayed() == true);
        assertTrue(playerHand.size()== assistantNames.length-1);
        assertTrue(assistants.contains(assistant3));
        playerHand.remove(assistant3);
        playerHand.remove(assistants.get(0));
        playerHand.remove(assistants.get(1));
        playerHand.remove(assistants.get(2));
        playerHand.remove(assistants.get(3));
        playerHand.remove(assistants.get(4));
        playerHand.remove(assistants.get(5));
        playerHand.remove(assistants.get(6));
        playerHand.remove(assistants.get(7));
        playerHand.remove(assistants.get(8));
        assertTrue(playerHand.isPresent(assistants.get(9)));
        assertTrue(!playerHand.isPresent(assistants.get(8)));
        playerHand.remove(assistants.get(9));
        assertTrue(playerHand.size()== 0);
    }


    /**
     * TESTING:
     * - trying to remove an invalid assistant from PlayerHand does not affect the assistant playability
     * - if every card in the player hand is already played, getPlayableAssistants returns every assistant in the player hand
     *   otherwise it returns only assistants not already played in the player hand
     */
    @Test
    public void getPlayableAssistantsTest(){
        AssistantName[] assistantNames= AssistantName.values();
        List<Assistant> assistants= new ArrayList<>();

        for(AssistantName assistantName: assistantNames)
            assistants.add(new Assistant(assistantName));

        PlayerHand playerHand=new PlayerHand(assistants);

        playerHand.remove(assistants.get(0));
        playerHand.remove(assistants.get(1));
        playerHand.remove(assistants.get(2));
        playerHand.remove(assistants.get(3));
        playerHand.remove(assistants.get(4));
        playerHand.remove(assistants.get(5));
        playerHand.remove(assistants.get(6));
        playerHand.remove(assistants.get(7));
        playerHand.remove(assistants.get(8));
        playerHand.remove(assistants.get(9));
        assertTrue(assistants.get(9).isAlreadyPlayed()==true);
        assistants.get(9).setPlayability(true);
        playerHand.remove(assistants.get(9));
        assertTrue(assistants.get(9).isAlreadyPlayed()==false);

        playerHand=new PlayerHand(assistants);
        playerHand.remove(assistants.get(0));
        playerHand.remove(assistants.get(1));
        playerHand.remove(assistants.get(2));
        playerHand.remove(assistants.get(3));
        playerHand.remove(assistants.get(4));
        playerHand.remove(assistants.get(5));
        playerHand.remove(assistants.get(6));
        playerHand.remove(assistants.get(7));
        assistants.get(8).setPlayability(false);
        assertTrue(playerHand.getPlayableAssistants().size()==1);
        assertTrue(playerHand.size()==2);
        assistants.get(9).setPlayability(false);
        assertTrue(playerHand.getPlayableAssistants().size()==2);
        assertTrue(playerHand.getPlayableAssistants().contains(assistants.get(8)));
        assertTrue(playerHand.getPlayableAssistants().contains(assistants.get(9)));
        assertTrue(playerHand.size()==2);
        playerHand.remove(assistants.get(8));
        playerHand.remove(assistants.get(9));
        assertTrue(playerHand.getPlayableAssistants().size()==0);

    }

    /**
     * testing if  getValue() and getMotherNatureMove() returns the correct values
     */
    @Test
    public void assistantValuesTest(){
        Assistant assistant= new Assistant(AssistantName.ASSISTANT10);
        assertTrue(assistant.getValue()==10);
        assertTrue(assistant.getMotherNatureMove()==5);
        assistant= new Assistant(AssistantName.ASSISTANT1);
        assertTrue(assistant.getValue()==1);
        assertTrue(assistant.getMotherNatureMove()==1);

    }

}
