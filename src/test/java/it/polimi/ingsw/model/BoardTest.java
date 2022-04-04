package it.polimi.ingsw.model;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class BoardTest {
    private PlayerBoard board = new PlayerBoard();

    @Test
    public void getEntrance() {
        Entrance entrance = board.getEntrance();
        assertNotNull(entrance);
    }
    @Test
    public void getDining() {
        DiningRoom dining = board.getDiningRoom();
        assertNotNull(dining);
    }
    @Test
    public void Prof(){
        board.changeProfessor(Color.RED);
        System.out.println(board.getProfessor(Color.RED));
        assertTrue(board.getProfessor(Color.RED));
        board.changeProfessor(Color.GREEN);
        System.out.println(board.getProfessor(Color.GREEN));
        assertTrue(board.getProfessor(Color.GREEN));
        board.changeProfessor(Color.RED);
        System.out.println(board.getProfessor(Color.RED));
        assertFalse(board.getProfessor(Color.RED));
        board.changeProfessor(Color.BLUE);
        System.out.println(board.getProfessor(Color.BLUE));
        assertTrue(board.getProfessor(Color.BLUE));

    }

    @Test
    public void Tower(){
        assertEquals(8,board.getNumberTower());
        board.removeTowers(5);
        assertEquals(3,board.getNumberTower());
        board.addTowers(2);
        assertEquals(5,board.getNumberTower());
        board.removeTowers(8);
        assertEquals(5,board.getNumberTower());
    }

    @Test
    public void Move(){
        Entrance en = board.getEntrance();
        DiningRoom di = board.getDiningRoom();
        en.addStudent(Color.BLUE);
        en.addStudent(Color.GREEN);
        en.addStudent(Color.RED);
        en.addStudent(Color.BLUE);
        en.addStudent(Color.YELLOW);
        en.addStudent(Color.YELLOW);
        en.addStudent(Color.YELLOW);

        board.moveStudentFromEntranceToDiningRoom(Color.BLUE);
        board.moveStudentFromEntranceToDiningRoom(Color.YELLOW);
        board.moveStudentFromEntranceToDiningRoom(Color.PINK);
        //rimasti in entrance
        assertEquals(2,en.getNumberOfStudent(Color.YELLOW));
        assertEquals(1,en.getNumberOfStudent(Color.BLUE));
        assertEquals(1,en.getNumberOfStudent(Color.GREEN));
        assertEquals(1,en.getNumberOfStudent(Color.RED));
        assertEquals(0,en.getNumberOfStudent(Color.PINK));

        //in dining
        assertEquals(1,di.getNumberOfStudent(Color.YELLOW));
        assertEquals(1,di.getNumberOfStudent(Color.BLUE));
        assertEquals(0,di.getNumberOfStudent(Color.GREEN));
        assertEquals(0,di.getNumberOfStudent(Color.RED));
        assertEquals(0,di.getNumberOfStudent(Color.PINK));
    }
}