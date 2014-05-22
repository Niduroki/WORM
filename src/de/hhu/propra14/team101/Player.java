package de.hhu.propra14.team101;


import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {

    public ArrayList<Worm> wormList;
    public int currentWorm = 0;
    public String name;
    public Color color;

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

    public Map serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", this.type);
        map.put("current_worm", this.currentWorm);
        map.put("color", this.color);
        ArrayList<Map> wormArray = new ArrayList<Map>();
        for (int i=0; i<this.wormList.size(); i++) {
            wormArray.add(this.wormList.get(i).serialize());
        }
        map.put("worm_array", wormArray);
        return map;
    }

    public static Player deserialize(Map input) {
        ArrayList<Map> rawWorms = (ArrayList<Map>) input.get("worm_array");
        ArrayList<Worm> wormList = new ArrayList<>();
        for (int i=0; i<rawWorms.size(); i++) {
            wormList.add(Worm.deserialize(rawWorms.get(i)));
        }
        Player player = new Player(wormList, (String)input.get("type"));
        player.currentWorm = (Integer) input.get("current_worm");
        player.color = (Color) input.get("color");
        return player;
    }
}
