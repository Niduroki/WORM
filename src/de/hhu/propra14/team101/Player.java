package de.hhu.propra14.team101;


import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a player of the game.
 * <pre>
 * {@code
 * Worm worm = new Worm();
 * ArrayList<Worm> worms = new ArrayList<Worm>();
 * worms.add(worm);
 * Player player = new Player(worms,"Local");
 * player.selectNextWorm();
 * }
 * </pre>
 */
public class Player {
    /**
     * list of worms
     */
    public ArrayList<Worm> wormList;
    /**
     * index of current worm (zero-based)
     */
    public int currentWorm = 0;
    /**
     * name ot the player
     */
    public String name;
    /**
     * color of the player
     */
    public Color color;

    /**
     * Whether the player is playing _this_ game, is on a network, or an AI
     * Possible values: "AI", "Local", "Network"
     */
    public String type;

    /**
     * Initialize a new player.
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
     *  Serialize a player.
     * @return serialized worm
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
     * Deserialize a player
     * @param input serialized data
     * @return deserialize player
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
     * Serialize  color of player
     * @return String value of color
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
     * Deserialize  color of player
     * @param name String, which represents a color
     * @return color Color object
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
