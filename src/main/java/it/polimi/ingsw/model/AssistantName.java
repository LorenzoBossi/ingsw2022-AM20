package it.polimi.ingsw.model;

public enum AssistantName {
    ASSISTANT1(1,1),
    ASSISTANT2(2,1),
    ASSISTANT3(3,2),
    ASSISTANT4(4,2),
    ASSISTANT5(5,3),
    ASSISTANT6(6,3),
    ASSISTANT7(7,4),
    ASSISTANT8(8,4),
    ASSISTANT9(9,5),
    ASSISTANT10(10,5);



    private int value;
    private int moves;


    
     AssistantName(int value, int moves){
        this.value=value;
        this.moves=moves;
    }


    /**
     * gets the value of the assistant to determine the order of the next turn
     * @return
     * an integer corresponding to the card value
     */
    public int getValue(){
        return value;
    }


    /**
     * gets the maximum number of moves of the assistant
     * @return
     * an integer corresponding to the maximum numbers of moves allowed
     */
    public int getMotherNatureMove(){
        return moves;
    }

}
