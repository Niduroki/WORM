package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Physics.BallisticMovement;
import de.hhu.propra14.team101.Physics.LineMovement;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;
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

    protected int xCoord = 0;
    protected int yCoord = 0;
    protected char orientation = 'l';

    private Image image;

    public Worm (ArrayList weapons) {
        this.weaponList = new ArrayList<>();
        for (Object weapon : weapons) {
            this.weaponList.add((AbstractWeapon) weapon);
        }

        if (!Main.headless) {
            this.image = new Image("images/Worm.gif");
        }
    }

    public int getXCoordinate()
    {
        return xCoord;
    }

    public void setXCoordinate(int xCoordinate)
    {
        xCoord = xCoordinate;
    }

    public int getYCoordinate()
    {
        return yCoord;
    }

    public void setYCoordinate(int yCoordinate)
    {
        yCoord = yCoordinate;
    }

    /**
     * @param gc Canvas to draw on
     * Draws the worm
     */
    public void draw (GraphicsContext gc, Color color) {
        gc.drawImage(this.image, this.xCoord, this.yCoord, this.size, this.size);
        gc.setFill(color);
        gc.fillText("H"+String.valueOf(this.health), this.xCoord, this.yCoord -4);
        if (this.armor != 0) {
            gc.fillText("A" + String.valueOf(this.armor), this.xCoord, this.yCoord -14);
        }
    }

    /**
     * Move the worm
     * @param direction either 'l' to move left or 'r' to move right
     */
    public void move (char direction) {
        // Can't move while jumping
        //if (this.jumpProcess == 0) {
            if (direction == 'l') {
                // Don't run out of the terrain
                if (this.xCoord >= 5) {
                    this.xCoord -= 5;
                }
            } else if (direction == 'r') {
                // Don't run out of the terrain TODO substitute 600 with terrain.getWidth
                if (this.xCoord <= 585) {
                    this.xCoord += 5;
                }
            }
            this.orientation = direction;
        //}
    }

    public void jump () {
        /*switch (this.jumpProcess) {
            case 0:
                this.yCoord -= 5;
                this.jumpProcess += 1;
                break;
            case 1:
                this.yCoord -= 3;
                if (this.orientation == 'l') {
                    // Don't run out of the terrain
                    if (this.xCoord >= 5) {
                        this.xCoord -= 3;
                    }
                } else if (this.orientation == 'r') {
                    // Don't run out of the terrain TODO substitute 600 with terrain.getWidth
                    if (this.xCoord <= 585) {
                        this.xCoord += 3;
                    }
                }
                this.jumpProcess += 1;
                break;
            case 2:
                this.yCoord += 3;
                if (this.orientation == 'l') {
                    // Don't run out of the terrain
                    if (this.xCoord >= 2) {
                        this.xCoord -= 2;
                    }
                } else if (this.orientation == 'r') {
                    // Don't run out of the terrain TODO substitute 600 with terrain.getWidth
                    if (this.xCoord <= 588) {
                        this.xCoord += 2;
                    }
                }
                this.jumpProcess += 1;
                break;
            case 3:
                this.yCoord += 5;
                this.jumpProcess = 0;
                break;
        }*/
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
        Bullet bullet = this.weaponList.get(this.currentWeapon).fire(
                new BallisticMovement(this.getXCoordinate(),
                this.getYCoordinate(),
                xPos, yPos)
        );
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
        Map<String, Object> data = new HashMap<>();
        data.put("x", this.xCoord);
        data.put("y", this.yCoord);
        data.put("armor", this.armor);
        data.put("health", this.health);
        data.put("orientation", this.orientation);
        ArrayList<Map> weapons = new ArrayList<>();
        for (AbstractWeapon aWeaponList : this.weaponList) {
            weapons.add(aWeaponList.serialize());
        }
        data.put("weapons", weapons);
        return data;
    }

    public static Worm deserialize(Map input) {
        ArrayList<Map> rawWeapons = (ArrayList<Map>) input.get("weapons");
        ArrayList<AbstractWeapon> weaponList = new ArrayList<>();
        for (Map rawWeapon : rawWeapons) {
            weaponList.add(AbstractWeapon.deserialize(rawWeapon));
        }
        Worm result = new Worm(weaponList);
        result.xCoord = (Integer) input.get("x");
        result.yCoord = (Integer) input.get("y");
        result.armor = (Integer) input.get("armor");
        result.health = (Integer) input.get("health");
        result.orientation = input.get("orientation").toString().charAt(0);
        return result;
    }
}
