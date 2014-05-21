package de.hhu.propra14.team101;


import java.util.ArrayList;

public class Player {

    public ArrayList<Worm> wormList;
    public int currentWorm = 0;

    /**
     * Whether the player is playing _this_ game, is on a network, or an AI
     * Possible values: "AI", "Local", "Network"
     */
    public String type;

    /**
     * @param wormList Worms that belong to the player wrapped in an ArrayList
     * @param type Possible values: "AI", "Local", "Network"
     */
    public Player(ArrayList<Worm> wormList, String type) {
        this.wormList = wormList;
        if (type.equals("AI") || type.equals("Local") || type.equals("Network")) {
            this.type = type;
        } else {
            /*throw WrongTypeException(){
                //
            }*/
        }
    }

    /**
     * Selects the next worm
     */
    public void selectNextWorm() {
        if (this.currentWorm == this.wormList.size()-1) {
            this.currentWorm = 0;
        } else {
            this.currentWorm += 1;
        }
    }
}
