package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import de.hhu.propra14.team101.Weapons.AtomicBomb;
import de.hhu.propra14.team101.Weapons.Bazooka;
import de.hhu.propra14.team101.Weapons.Grenade;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to create and handle Worms
 */

public class Worm {

    public ArrayList<AbstractWeapon> weaponList;

    public int size = 25;

    public int armor = 0;

    public int health = 100;

    public int currentWeapon = 0;

    protected int x_coord = 0;

    protected int y_coord = 0;

    protected char orientation = 'l';

    private int jumpProcess = 0;

    public Worm (ArrayList weapons) {
        this.weaponList = new ArrayList<>();
        for (int i=0; i<weapons.size(); i++) {
            this.weaponList.add((AbstractWeapon)weapons.get(i));
        }
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
    public void draw (GraphicsContext gc, Color color) {
        Image image = new Image("worm.gif");
        gc.drawImage(image, this.x_coord, this.y_coord, this.size, this.size);
        gc.setFill(color);
        gc.fillText("H"+String.valueOf(this.health), this.x_coord, this.y_coord-4);
        if (this.armor != 0) {
            gc.fillText("A" + String.valueOf(this.armor), this.x_coord, this.y_coord-14);
        }
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

    public void nextWeapon() {
        if (this.currentWeapon == this.weaponList.size()-1) {
            this.currentWeapon = 0;
        } else {
            this.currentWeapon += 1;
        }
    }

    public void prevWeapon() {
        if (this.currentWeapon == 0) {
            this.currentWeapon = this.weaponList.size()-1;
        } else {
            this.currentWeapon -= 1;
        }
    }

    public Bullet fireWeapon(double xPos, double yPos) {
        Bullet bullet = this.weaponList.get(this.currentWeapon).fire(new Physics(this.getXCoordinate(), this.getYCoordinate(), xPos, yPos));
        return bullet;
    }

    public boolean isHitted(double xCoordinate, double yCoordinate) {
        if (this.getXCoordinate() < xCoordinate && this.getXCoordinate() + this.size > xCoordinate) {
            if (this.getYCoordinate() < yCoordinate && this.getYCoordinate() + this.size > yCoordinate) {
                return true;
            }
        }

        return false;
    }

    public Map serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("x", this.x_coord);
        data.put("y", this.y_coord);
        data.put("armor", this.armor);
        data.put("health", this.health);
        data.put("orientation", this.orientation);
        ArrayList<Map> weapons = new ArrayList<Map>();
        for (int i=0; i<this.weaponList.size(); i++) {
            weapons.add(this.weaponList.get(i).serialize());
        }
        data.put("weapons", weapons);
        return data;
    }

    public static Worm deserialize(Map input) {
        ArrayList<Map> rawWeapons = new ArrayList<Map>();
        ArrayList<AbstractWeapon> weaponList = new ArrayList<>();
        for (int i=0; i<rawWeapons.size(); i++) {
            weaponList.add(AbstractWeapon.deserialize(rawWeapons.get(i)));
        }
        Worm result = new Worm(weaponList);
        result.x_coord = (Integer) input.get("x");
        result.y_coord = (Integer) input.get("y");
        result.armor = (Integer) input.get("armor");
        result.health = (Integer) input.get("health");
        result.orientation = input.get("orientation").toString().charAt(0);
        return result;
    }
}
