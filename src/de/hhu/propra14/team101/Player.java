package de.hhu.propra14.team101;


public class Player {

    public Worm[] wormArray;

    /**
     * Whether the player is playing _this_ game, is on a network, or an AI
     * Possible values: "AI", "Local", "Network"
     */
    public String type;

    /**
     * @param wormArray Worms that belong to the player
     * @param type Possible values: "AI", "Local", "Network"
     */
    public Player(Worm[] wormArray, String type) {
        this.wormArray = wormArray;
        if (type.equals("AI") || type.equals("Local") || type.equals("Network")) {
            this.type = type;
        } else {
            /*throw WrongTypeException(){
                //
            }*/
        }
    }
}
