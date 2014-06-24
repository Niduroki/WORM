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
            this.type = "Local";
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

    /**
     *
     * @return
     */
    public Map serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("type", this.type);
        map.put("current_worm", this.currentWorm);
        map.put("color", Player.serializeColor(this.color));
        ArrayList<Map> wormArray = new ArrayList<>();
        for (Worm aWormList : this.wormList) {
            wormArray.add(aWormList.serialize());
        }
        map.put("worm_array", wormArray);
        return map;
    }

    /**
     *
     * @param input
     * @return
     */
    public static Player deserialize(Map input) {
        @SuppressWarnings("unchecked")
        ArrayList<Map> rawWorms = (ArrayList<Map>) input.get("worm_array");
        ArrayList<Worm> wormList = new ArrayList<>();
        for (Map rawWorm : rawWorms) {
            wormList.add(Worm.deserialize(rawWorm));
        }
        Player player = new Player(wormList, (String)input.get("type"));
        player.currentWorm = (Integer) input.get("current_worm");
        player.color = Player.deseserializeColor((String) input.get("color"));
        return player;
    }

    /**
     *
     * @param color
     * @return
     */
    public static String serializeColor(Color color) {
        if (color.equals(Color.RED)) {
            return "Red";
        } else if (color.equals(Color.GREEN)) {
            return "Green";
        } else if (color.equals(Color.BLUE)) {
            return "Blue";
        } else if (color.equals(Color.YELLOW)) {
            return "Yellow";
        } else {
            return "Gray";
        }
    }

    /**
     *
     * @param name
     * @return
     */
    public static Color deseserializeColor(String name) {
        switch (name) {
            case "Red":
                return Color.RED;
            case "Blue":
                return Color.BLUE;
            case "Green":
                return Color.GREEN;
            case "Yellow":
                return Color.YELLOW;
            default:
                return Color.GREY;
        }
    }
}
