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
        assertEquals(playerHand.size(),assistantNames.length);

        Assistant assistant3=assistants.get(3);
        assertFalse(assistant3.isAlreadyPlayed());

        playerHand.remove(assistant3);
        assertTrue(assistant3.isAlreadyPlayed());
        assertEquals(playerHand.size(), assistantNames.length-1);
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
        assertFalse(playerHand.isPresent(assistants.get(8)));
        playerHand.remove(assistants.get(9));
        assertEquals(0,playerHand.size());
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
        assertTrue(assistants.get(9).isAlreadyPlayed());
        assistants.get(9).setPlayability(true);
        playerHand.remove(assistants.get(9));
        assertFalse(assistants.get(9).isAlreadyPlayed());

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
        assertEquals(playerHand.getPlayableAssistants().size(),1);
        assertEquals(playerHand.size(),2);
        assistants.get(9).setPlayability(false);
        assertEquals(playerHand.getPlayableAssistants().size(),2);
        assertTrue(playerHand.getPlayableAssistants().contains(assistants.get(8)));
        assertTrue(playerHand.getPlayableAssistants().contains(assistants.get(9)));
        assertEquals(playerHand.size(),2);
        playerHand.remove(assistants.get(8));
        playerHand.remove(assistants.get(9));
        assertEquals(playerHand.getPlayableAssistants().size(),0);

    }

    /**
     * testing if  getValue() and getMotherNatureMove() returns the correct values
     */
    @Test
    public void assistantValuesTest(){
        Assistant assistant= new Assistant(AssistantName.ASSISTANT10);
        assertEquals(10,assistant.getValue());
        assertEquals(5,assistant.getMotherNatureMove());
        assistant= new Assistant(AssistantName.ASSISTANT1);
        assertEquals(1,assistant.getValue());
        assertEquals(1,assistant.getMotherNatureMove());

    }

    /**
     * testing player methods to use asssistants
     */
    @Test
    public void playerAssistantTest(){

        List<Assistant> assistants= Assistant.getEveryAssistant();

        Player p1= new Player("Anna");
        p1.setAssistants(assistants);
        Player p2= new Player("Bob",assistants);

        assertTrue(p1.playAssistant(assistants.get(3)));
        assertEquals(p1.getMotherNatureMaxMove(),assistants.get(3).getMotherNatureMove());
        assertEquals(p1.getPlayerPriority(),assistants.get(3).getValue());

        assertFalse(p2.playAssistant(assistants.get(3)));
        assertEquals(p2.getMotherNatureMaxMove(),0);
        assertEquals(p2.getPlayerPriority(),0);

    }

}
