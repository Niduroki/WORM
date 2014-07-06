package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Physics.BallisticMovement;
import de.hhu.propra14.team101.Physics.Collision;
import de.hhu.propra14.team101.Physics.CollisionType;
import de.hhu.propra14.team101.TerrainObjects.*;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import org.newdawn.easyogg.OggClip;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to create and handle Worms
 * <p>
 * Code example:
 * <pre>
 * {@code
 * ArrayList<AbstractWeapon> weapons = new ArrayList<AbstractWeapon>();
 * weapons.add(new AtomicBomb());
 * weapons.add(new Bazooka());
 * Worm worm = new Worm(weapons);
 * worm.setXCoordinate(2.5);
 * worm.setYCoordinate(10);
 * System.out.println(worm.getXCoordinate());
 * System.out.println(worm.getYCoordinate());
 * System.out.println(worm.getCurrentWeapon().name);
 * worm.nextWeapon();
 * System.out.println(worm.getCurrentWeapon().name);
 * System.out.println(worm.isHitted(3,10));
 * worm.draw(graphicsContext);
 * worm.move('r',terrain,players);
 * System.out.println(worm.getXCoordinate());
 * System.out.println(worm.getYCoordinate());
 *
 * ==Output==
 * 2.5
 * 10
 * AtomicBomb
 * Bazooka
 * true
 * 7.5
 * 10
 * }
 * </pre>
 */
public class Worm {
    /**
     * Contains weapons of the worm.
     */
    public ArrayList<AbstractWeapon> weaponList;

    /**
     * width and height of worm
     */
    public static double size = 25;

    /**
     * health point of worm.
    */
    public int health = 100;

    private int currentWeapon = 0;
    private BallisticMovement jumpPhysic = null;
    private double shoeFactor = 1;
    private double springFactor = 1;
    protected double xCoord = 0;
    protected double yCoord = 0;
    protected char orientation = 'l';
    private ArrayList<Item> items = new ArrayList<>();
    private Image image;

    /**
     * Initialize a new worm.
     *
     * @param weapons weapons of new worm
     */
    public Worm(ArrayList weapons) {
        this.weaponList = new ArrayList<>();
        for (Object weapon : weapons) {
            this.weaponList.add((AbstractWeapon) weapon);
        }

        if (!Main.headless) {
            String capitalizedTheme = Level.theme.substring(0, 1).toUpperCase() + Level.theme.substring(1);
            this.image = new Image("images/worm-"+capitalizedTheme+".gif");
        }
    }

    /**
     * Updates the image – necessary for loading the proper themed worm<br>
     * Don't call this in headless mode!
     */
    public void updateImage() {
        String capitalizedTheme = Level.theme.substring(0, 1).toUpperCase() + Level.theme.substring(1);
        this.image = new Image("images/worm-"+capitalizedTheme+".gif");
    }

    /**
     * Gets x-coordinate of worm.
     *
     * @return value of x-coordinate
     */
    public double getXCoordinate() {
        return xCoord;
    }

    /**
     * Sets x-coordinate of worm.
     *
     * @param xCoordinate new value of x-coordinate
     */
    public void setXCoordinate(double xCoordinate) {
        xCoord = xCoordinate;
    }

    /**
     * Gets y-coordinate of worm.
     *
     * @return value of y-coordinate
     */
    public double getYCoordinate() {
        return yCoord;
    }

    /**
     * Sets y-coordinate of worm
     *
     * @param yCoordinate new value of y-coordinate
     */
    public void setYCoordinate(double yCoordinate) {
        yCoord = yCoordinate;
    }

    /**
     * Gets all item, which was picked up.
     *
     * @return array list of items
     */
    public ArrayList<Item> getItems() {
        return items;
    }

    /**
     * Gets current selected weapon
     *
     * @return current weapon
     */
    public AbstractWeapon getCurrentWeapon() {
        return weaponList.get(currentWeapon);
    }


    /**
     * Use an item.
     *
     * @param index index of the item, which is used.
     */
    public void useItem(int index) {
        if (index > 0 && index <= this.getItems().size()) {
            index--;
            switch (this.getItems().get(index).getName()) {
                case "Elixir":
                    this.health = this.health * 2;
                    this.getItems().remove(index);
                    break;
                case "Shoe":
                    shoeFactor = 2;
                    this.getItems().remove(index);
                    break;
                case "Spring":
                    springFactor = 1.3;
                    this.getItems().remove(index);
                    break;
            }
        }
    }

    /**
     * Draws the worm
     * @param gc Canvas to draw on
     * @param color Color of the worms team
     */
    public void draw(GraphicsContext gc, Color color) {
        gc.drawImage(this.image,
                // Worms are huge, thus we can't start drawing them like a normal terrain block, otherwise they'd clip the ground
                this.xCoord - (AbstractTerrainObject.baseSize / 2) * Main.sizeMultiplier,
                this.yCoord - AbstractTerrainObject.baseSize * Main.sizeMultiplier,
                size * Main.sizeMultiplier, size * Main.sizeMultiplier
        );
        gc.setFill(color);
        gc.fillText(
                "H" + String.valueOf(this.health),
                this.xCoord - (AbstractTerrainObject.baseSize / 2) * Main.sizeMultiplier,
                this.yCoord - AbstractTerrainObject.baseSize * Main.sizeMultiplier
        );
    }

    /**
     * Move the worm
     *
     * @param direction either 'l' to move left or 'r' to move right
     * @param terrain   current terrain of the game
     * @param players   all players of the game
     */
    public void move(char direction, Terrain terrain, ArrayList<Player> players) {
        double newXPos = 0;
        double collisionXPos = 0;
        double velocity = ((AbstractTerrainObject.baseSize * Main.sizeMultiplier) / 2);
        AbstractTerrainObject terrainObject = terrain.isTerrain(this.getXCoordinate(), this.getYCoordinate() + size - 5 * Main.sizeMultiplier);
        if (terrainObject != null) {
            if (terrainObject.getClass() == Obstacle.class) {
                velocity = (AbstractTerrainObject.baseSize * Main.sizeMultiplier);
            } else if (terrainObject.getClass() == SandBuildingBlock.class) {
                velocity = ((AbstractTerrainObject.baseSize * Main.sizeMultiplier) / 6) * shoeFactor;
            }
        }
        if (direction == 'l') {
            newXPos = this.getXCoordinate() - velocity;
            collisionXPos = newXPos;
        } else if (direction == 'r') {
            newXPos = this.getXCoordinate() + velocity;
            collisionXPos = newXPos + size;
        }

        //Collision?
        terrainObject = terrain.isTerrain(collisionXPos, this.getYCoordinate() + size - 6 * Main.sizeMultiplier);
        if (terrainObject == null) {
            for (Player player : players) {
                for (Worm worm : player.wormList) {
                    if (worm != this && worm.isHitted(collisionXPos, this.getYCoordinate())) {
                        return;
                    }
                }
            }

            // Don't run out of the terrain
            if (
                    (direction == 'l' && this.getXCoordinate() >= (AbstractTerrainObject.baseSize * Main.sizeMultiplier) / 2) ||
                            (direction == 'r' && this.getXCoordinate() < Terrain.getWidthInPixel() - size)
                    ) {
                this.xCoord = newXPos;
            }
            freeFall(terrain);
        } else {
            if (terrainObject.getClass() == Elixir.class || terrainObject.getClass() == Shoe.class || terrainObject.getClass() == Spring.class) {
                Item item = (Item) terrainObject;
                items.add(item);
                terrain.removeTerrainObject(terrainObject, false);
            } else {
                if (terrainObject.getClass() == TriangleBuildingBlock.class) {
                    if (direction == 'l' && ((TriangleBuildingBlock) terrainObject).getSlopedLeft() == false) {
                        this.xCoord = this.xCoord -AbstractTerrainObject.baseSize * Main.sizeMultiplier;
                        this.yCoord = this.yCoord -AbstractTerrainObject.baseSize * Main.sizeMultiplier;
                    } else {
                        this.xCoord = this.xCoord +AbstractTerrainObject.baseSize * Main.sizeMultiplier;
                        this.yCoord = this.yCoord -AbstractTerrainObject.baseSize * Main.sizeMultiplier;
                    }
                }
            }
        }
        this.orientation = direction;
    }

    private void freeFall(Terrain terrain) {
        int height = 0;
        while (
                (terrain.isTerrain(this.getXCoordinate(), this.getYCoordinate() + size - 5 * Main.sizeMultiplier) == null)
                        && terrain.isTerrain(this.getXCoordinate() + size, this.getYCoordinate() + size - 5 * Main.sizeMultiplier) == null
                        && this.getYCoordinate() + size - ((AbstractTerrainObject.baseSize * Main.sizeMultiplier) / 2) < Terrain.getHeightInPixel()
                ) {
            this.setYCoordinate(this.getYCoordinate() + 1);
            height++;
        }

        if (height > size - 2 * Main.sizeMultiplier) {
            health -= height / 4;
            try {
                OggClip goodbyeClip = new OggClip("sfx/worms/Bones.ogg");
                goodbyeClip.setGain(Main.svol);
                goodbyeClip.play();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Perform one step of a jump.
     *
     * @param terrain current terrain
     * @param worms   all worms
     * @return true, if finished
     */
    public boolean jump(Terrain terrain, ArrayList<Worm> worms) {
        if (jumpPhysic == null) {
            if (orientation == 'l') {
                jumpPhysic = new BallisticMovement(this.getXCoordinate(), this.getYCoordinate(), this.getXCoordinate() - 50 * springFactor * Main.sizeMultiplier, this.getYCoordinate() - 50 * springFactor * Main.sizeMultiplier, true);
            } else {
                jumpPhysic = new BallisticMovement(this.getXCoordinate(), this.getYCoordinate(), this.getXCoordinate() + 50 * springFactor * Main.sizeMultiplier, this.getYCoordinate() - 50 * springFactor * Main.sizeMultiplier, true);
            }
        }
        Collision collision = jumpPhysic.move(1,this, worms, terrain, Terrain.getWidthInPixel(), Terrain.getHeightInPixel());
        if (collision != null) {
            if (collision.getType() == CollisionType.Terrain) {
                jumpPhysic = null;
                this.setYCoordinate(this.getYCoordinate() - AbstractTerrainObject.baseSize * Main.sizeMultiplier);
                freeFall(terrain);
                return true;
            } else if (collision.getType() == CollisionType.Worm) {
                jumpPhysic = null;
                this.setYCoordinate(this.getYCoordinate() - size);
                freeFall(terrain);
                return true;
            } else {
                jumpPhysic = null;
                this.health = 0;
                return true;
            }
        } else {
            this.setXCoordinate(jumpPhysic.getXCoordinate());
            this.setYCoordinate(jumpPhysic.getYCoordinate());
        }
        if (jumpPhysic.isFinished()) {
            jumpPhysic = null;
            this.setYCoordinate(this.getYCoordinate() - AbstractTerrainObject.baseSize * Main.sizeMultiplier);
            freeFall(terrain);
            return true;
        }

        return false;
    }

    /**
     * Select next weapon.
     */
    public void nextWeapon() {
        if (this.currentWeapon == this.weaponList.size() - 1) {
            this.currentWeapon = 0;
        } else {
            this.currentWeapon += 1;
        }
    }

    /**
     * Select previous weapon.
     */
    public void prevWeapon() {
        if (this.currentWeapon == 0) {
            this.currentWeapon = this.weaponList.size() - 1;
        } else {
            this.currentWeapon -= 1;
        }
    }

    /**
     * Fire current weapon.
     *
     * @param xPos x-coordinate of direction
     * @param yPos y-coordinate of direction
     * @return bullet of the shoot
     */
    public Bullet fireWeapon(double xPos, double yPos) {
        Bullet bullet = this.weaponList.get(this.currentWeapon).fire(
                new BallisticMovement(this.getXCoordinate(),
                        this.getYCoordinate(),
                        xPos, yPos, false)
        );
        return bullet;
    }

    /**
     * Check if the worm contains the point.
     *
     * @param xCoordinate x-coordinate of point
     * @param yCoordinate y-coordinate of point
     * @return true, if the worm is hit by point, otherwise false
     */
    public boolean isHitted(double xCoordinate, double yCoordinate) {
        if (this.getXCoordinate() <= xCoordinate && this.getXCoordinate() + size >= xCoordinate) {
            if (this.getYCoordinate() <= yCoordinate && this.getYCoordinate() + size >= yCoordinate) {
                return true;
            }
        }

        return false;
    }

    /**
     * Serialize worm.
     *
     * @return serialized data
     */
    public Map serialize() {
        Map<String, Object> data = new HashMap<>();
        data.put("x", this.xCoord);
        data.put("y", this.yCoord);
        data.put("health", this.health);
        data.put("orientation", this.orientation);
        ArrayList<Map> weapons = new ArrayList<>();
        for (AbstractWeapon aWeaponList : this.weaponList) {
            weapons.add(aWeaponList.serialize());
        }
        data.put("weapons", weapons);
        return data;
    }

    /**
     * Deserialize worm.
     *
     * @param input data
     * @return deserialzed worm
     */
    public static Worm deserialize(Map input) {
        @SuppressWarnings("unchecked")
        ArrayList<Map> rawWeapons = (ArrayList<Map>) input.get("weapons");
        ArrayList<AbstractWeapon> weaponList = new ArrayList<>();
        for (Map rawWeapon : rawWeapons) {
            weaponList.add(AbstractWeapon.deserialize(rawWeapon));
        }
        Worm result = new Worm(weaponList);
        result.xCoord = ((Double) input.get("x")).intValue();
        result.yCoord = ((Double) input.get("y")).intValue();

        // When this is an online sync request these values need to be scaled
        if (Game.online) {
            result.xCoord *= Main.sizeMultiplier;
            result.yCoord *= Main.sizeMultiplier;
        }
        result.health = (Integer) input.get("health");
        result.orientation = input.get("orientation").toString().charAt(0);
        return result;
    }
}
