package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to create Worms
 */

public class Worm {

    public Weapons[] weaponArray;

    public int armor = 0;

    public int health = 100;

    protected int x_coord = 0;

    protected int y_coord = 0;

    public Worm () {
        this.weaponArray = new Weapons[3];
        this.weaponArray[0] = new Bazooka();
        this.weaponArray[1] = new AtomicBomb();
        this.weaponArray[2] = new Grenade();
    }

    public int getXCoordinate()
    {
        return x_coord;
    }

    public void setXCoordinate(int xCoordinate)
    {
        x_coord = xCoordinate;
    }

    public int getYCoordinate()
    {
        return y_coord;
    }

    public void setYCoordinate(int yCoordinate)
    {
        y_coord = yCoordinate;
    }

    /**
     * @param gc Canvas to draw on
     * Draws the worm
     */
    public void draw (GraphicsContext gc) {
        Image image = new Image("resources/wurm.gif");
        gc.drawImage(image, this.x_coord, this.y_coord);
    }

    public void move (char direction) {
        if (direction == 'l') {
            this.x_coord -= 5;
            // Send movement-left to the server
        } else if (direction == 'r') {
            this.x_coord += 5;
            // Send movement-right to the server
        }
    }

    public void jump (char direction) {
        //
    }

    public void loseHealth (int amount) {
        this.health -= amount;

        if (this.health <= 0) {
            this.die();
        }
    }

    public void die () {
        //
    }

    public Map serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("x", this.x_coord);
        data.put("y", this.y_coord);
        data.put("armor", this.armor);
        data.put("health", this.health);
        ArrayList<Map> weapons = new ArrayList<Map>();
        for (int i=0; i<this.weaponArray.length; i++) {
            weapons.add(this.weaponArray[i].serialize());
        }
        data.put("weapons", weapons);
        return data;
    }

    public static Worm deserialize(Map input) {
        Worm result = new Worm();
        result.x_coord = (Integer) input.get("x");
        result.y_coord = (Integer) input.get("y");
        result.armor = (Integer) input.get("armor");
        result.health = (Integer) input.get("health");
        ArrayList<Map> rawWeapons = new ArrayList<Map>();
        Weapons[] weaponArray = new Weapons[rawWeapons.size()];
        for (int i=0; i<rawWeapons.size(); i++) {
            weaponArray[i] = Weapons.deserialize(rawWeapons.get(i));
        }
        result.weaponArray = weaponArray;
        return result;
    }
}
