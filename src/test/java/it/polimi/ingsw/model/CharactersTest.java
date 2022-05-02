package it.polimi.ingsw.model;

import it.polimi.ingsw.model.characterCards.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;


    /**
     * This class tests some characters cards:
     * banker, monk, jester, postman, profCard, princess, musician
     */
    public class CharactersTest {

        Game game;
        private Player p1;
        private Player p2;
        private Player p3;


        @BeforeEach
        public void init(){
            game= new Game(3);
            game.addPlayer("Ada");
            game.addPlayer("Billy");
            game.addPlayer("Charlie");
            game.start();
            p1= game.getPlayers().get(0);
            p2= game.getPlayers().get(1);
            p3= game.getPlayers().get(2);

        }


        @Test
        public void bankerTest(){

            CharacterCard banker = new Banker();
            p1.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);

            p2.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            p2.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            p2.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            p2.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);

            p1.getPlayerChoice().selectColor(Color.BLUE);
            banker.activateEffect(game);

            assertEquals(p1.getPlayerBoard().getDiningRoom().getNumberOfStudent(Color.BLUE),0);
            assertEquals(p2.getPlayerBoard().getDiningRoom().getNumberOfStudent(Color.BLUE),1);
            assertEquals(p3.getPlayerBoard().getDiningRoom().getNumberOfStudent(Color.BLUE),0);

            assertTrue(banker.checkRequirements());
        }

        /**
         * Tests the correct placement of professors after the activation of the banker card
         */
        @Test
        public void bankerProfResetTest() {
            CharacterCard banker = new Banker();
            ProfessorManager professorManager=game.getProfessorManager();
            p1.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            if(professorManager.canTakeProfessor(p1,Color.BLUE))
                professorManager.takeProfessor(p1,Color.BLUE);

            p2.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            p2.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            p2.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            p2.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);

            if(professorManager.canTakeProfessor(p2,Color.BLUE))
                professorManager.takeProfessor(p2,Color.BLUE);

            p1.getPlayerChoice().selectColor(Color.BLUE);
            banker.activateEffect(game);

            p3.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            assertFalse(game.getProfessorManager().canTakeProfessor(p3,Color.BLUE));
            p3.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            assertTrue(game.getProfessorManager().canTakeProfessor(p3,Color.BLUE));
        }

        @Test
        public void monkTest(){

            int initialNumber;
            CharacterCard monk = new Monk(game.getBag());
            List<Color> selection= new ArrayList<>();
            Island island= game.getArchipelago().getIsland(4);
            Color selectedColor=monk.getStudents().get(1);
            selection.add(selectedColor);
            assertTrue(monk.getStudents().contains(selection.get(0)));

            p1.getPlayerChoice().selectStudents(selection);
            p1.getPlayerChoice().selectIsland(island);

            initialNumber=island.getSelectedStudents(selectedColor);

            monk.activateEffect(game);

            assertEquals(island.getSelectedStudents(selectedColor),initialNumber+1);
            assertTrue(monk.checkRequirements());
        }


        @Test
        public void jesterTest(){
            CharacterCard jester= new Jester(game.getBag());
            List<Color> selectedStudents= new ArrayList<>();
            p1.getPlayerBoard().getEntrance().addStudent(Color.BLUE);
            p1.getPlayerBoard().getEntrance().addStudent(Color.GREEN);
            p1.getPlayerBoard().getEntrance().addStudent(Color.GREEN);

            selectedStudents.add(jester.getStudents().get(2));
            selectedStudents.add(jester.getStudents().get(4));
            p1.getPlayerChoice().selectStudents(selectedStudents);

            p1.getPlayerChoice().selectStudentFromEntrance(asList(Color.BLUE,Color.GREEN));

            jester.activateEffect(game);

            assertTrue(jester.getStudents().contains(Color.BLUE));
            assertTrue(jester.getStudents().contains(Color.GREEN));

            assertTrue(p1.getPlayerBoard().getEntrance().isPresent(selectedStudents.get(0)));
            assertTrue(p1.getPlayerBoard().getEntrance().isPresent(selectedStudents.get(1)));
            assertTrue(p1.getPlayerBoard().getEntrance().isPresent(Color.GREEN));
            assertTrue(jester.checkRequirements());
        }


        @Test
        public void postManTest(){
            CharacterCard postman = new PostMan();

            p1.playAssistant(game.getAssistantByName(AssistantName.ASSISTANT3));
            assertEquals(p1.getMotherNatureMaxMove(),2);

            postman.activateEffect(game);

            assertEquals(p1.getMotherNatureMaxMove(),4);
            assertTrue(postman.checkRequirements());
        }


        @Test
        public void profCardTest(){
            CharacterCard profCard= new ProfCard();

            p1.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            if(game.getProfessorManager().canTakeProfessor(p1,Color.BLUE))
                game.getProfessorManager().takeProfessor(p1,Color.BLUE);

            game.setCurrPlayer(p2);
            p2.getPlayerBoard().getDiningRoom().addStudent(Color.BLUE);
            assertTrue(game.getProfessorManager().getProfessorsOwnedBy(p1).contains(Color.BLUE));

            profCard.activateEffect(game);

            if(game.getProfessorManager().canTakeProfessor(p2,Color.BLUE))
                game.getProfessorManager().takeProfessor(p2,Color.BLUE);
            assertTrue(game.getProfessorManager().getProfessorsOwnedBy(p2).contains(Color.BLUE));
            assertFalse(game.getProfessorManager().getProfessorsOwnedBy(p1).contains(Color.BLUE));

            assertTrue(profCard.checkRequirements());
        }


        @Test
        public void princessTest(){
            CharacterCard princess= new Princess(game.getBag());

            List<Color> selectedStudent= new ArrayList<>();
            selectedStudent.add(princess.getStudents().get(2));

            assertEquals(1,selectedStudent.size());
            p1.getPlayerChoice().selectStudents(selectedStudent);
            DiningRoom diningRoom=p1.getPlayerBoard().getDiningRoom();

            assertEquals(diningRoom.getNumberOfStudent(selectedStudent.get(0)),0);

            princess.activateEffect(game);

            assertEquals(diningRoom.getNumberOfStudent(selectedStudent.get(0)),1);
            assertEquals(4,princess.getStudents().size());

            assertTrue(princess.checkRequirements());
        }

        @Test
        public void MusicianTest(){
            CharacterCard musician = new Musician();

            Entrance entrance= p1.getPlayerBoard().getEntrance();
            DiningRoom diningRoom=p1.getPlayerBoard().getDiningRoom();

            entrance.addStudent(Color.RED);
            entrance.addStudent(Color.GREEN);
            assertTrue(entrance.isPresent(Color.RED));
            assertTrue(entrance.isPresent(Color.GREEN));

            diningRoom.addStudent(Color.YELLOW);
            diningRoom.addStudent(Color.PINK);
            assertEquals(1,diningRoom.getNumberOfStudent(Color.YELLOW));
            assertEquals(1,diningRoom.getNumberOfStudent(Color.PINK));

            List<Color> selectedEntrance=new ArrayList<>();
            List<Color> selectedDining=new ArrayList<>();

            selectedEntrance.add(Color.RED);
            selectedEntrance.add(Color.GREEN);

            selectedDining.add(Color.YELLOW);
            selectedDining.add(Color.PINK);

            p1.getPlayerChoice().selectStudentFromEntrance(selectedEntrance);
            p1.getPlayerChoice().selectStudents(selectedDining);

            musician.activateEffect(game);

            assertTrue(entrance.isPresent(Color.YELLOW));
            assertTrue(entrance.isPresent(Color.PINK));
            assertEquals(1,diningRoom.getNumberOfStudent(Color.RED));
            assertEquals(1,diningRoom.getNumberOfStudent(Color.GREEN));
            assertEquals(0,diningRoom.getNumberOfStudent(Color.YELLOW));
            assertEquals(0,diningRoom.getNumberOfStudent(Color.PINK));

            assertTrue(musician.checkRequirements());
        }

    }
