package de.hhu.propra14.team101;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to create and handle Worms
 */

public class Worm {

    public Weapons[] weaponArray;

    public int armor = 0;

    public int health = 100;

    protected int x_coord = 0;

    protected int y_coord = 0;

    protected char orientation = 'l';

    private int jumpProcess = 0;

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

    /**
     * Move the worm
     * @param direction either 'l' to move left or 'r' to move right
     */
    public void move (char direction) {
        // Can't move while jumping
        if (this.jumpProcess == 0) {
            if (direction == 'l') {
                // Don't run out of the terrain
                if (this.x_coord >= 5) {
                    this.x_coord -= 5;
                    // Send movement-left to the server
                }
            } else if (direction == 'r') {
                // Don't run out of the terrain TODO substitute 600 with terrain.getWidth
                if (this.x_coord <= 585) {
                    this.x_coord += 5;
                    // Send movement-right to the server
                }
            }
            this.orientation = direction;
        }
    }

    public void jump () {
        switch (this.jumpProcess) {
            case 0:
                this.y_coord -= 5;
                this.jumpProcess += 1;
                // Send jump-left/right-0 to the server
                break;
            case 1:
                this.y_coord -= 3;
                if (this.orientation == 'l') {
                    // Don't run out of the terrain
                    if (this.x_coord >= 5) {
                        this.x_coord -= 3;
                        // Send jump-left-1 to the server
                    }
                } else if (this.orientation == 'r') {
                    // Don't run out of the terrain TODO substitute 600 with terrain.getWidth
                    if (this.x_coord <= 585) {
                        this.x_coord += 3;
                        // Send jump-right-1 to the server
                    }
                }
                this.jumpProcess += 1;
                break;
            case 2:
                this.y_coord += 3;
                if (this.orientation == 'l') {
                    // Don't run out of the terrain
                    if (this.x_coord >= 2) {
                        this.x_coord -= 2;
                        // Send jump-left-2 to the server
                    }
                } else if (this.orientation == 'r') {
                    // Don't run out of the terrain TODO substitute 600 with terrain.getWidth
                    if (this.x_coord <= 588) {
                        this.x_coord += 2;
                        // Send jump-right-2 to the server
                    }
                }
                this.jumpProcess += 1;
                break;
            case 3:
                this.y_coord += 5;
                this.jumpProcess = 0;
                // Send jump-done to the server
                break;
        }
    }

    public void loseHealth (int amount) {
        this.health -= amount;

        if (this.health <= 0) {
            this.die();
        }
    }

    public void die () {
        // TODO is this needed? Worm should be removed on death
    }

    public Map serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("x", this.x_coord);
        data.put("y", this.y_coord);
        data.put("armor", this.armor);
        data.put("health", this.health);
        data.put("orientation", this.orientation);
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
        result.orientation = input.get("orientation").toString().charAt(0);
        ArrayList<Map> rawWeapons = new ArrayList<Map>();
        Weapons[] weaponArray = new Weapons[rawWeapons.size()];
        for (int i=0; i<rawWeapons.size(); i++) {
            weaponArray[i] = Weapons.deserialize(rawWeapons.get(i));
        }
        result.weaponArray = weaponArray;
        return result;
    }
}
