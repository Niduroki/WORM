package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Level;
import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * Creates a triangle, dirt building block, either sloped left or right
 *
 * @see de.hhu.propra14.team101.Terrain
 */
public class TriangleBuildingBlock extends AbstractTerrainObject {

    protected boolean slopedLeft;

    /**
     * Initialize a new AbstractTerrainObject.
     *
     * @param x          x-coordinate of the object
     * @param y          y-coordinate of the object
     * @param slopedLeft true, if the block is left sloped, otherwise false
     */
    public TriangleBuildingBlock(int x, int y, boolean slopedLeft) {
        super(x, y);
        this.slopedLeft = slopedLeft;
        this.destructible = true;

        if (!Main.headless) {
            String capitalizedTheme = Level.theme.substring(0, 1).toUpperCase() + Level.theme.substring(1);
            if (this.getSlopedLeft()) {
                this.image = new Image("images/"+capitalizedTheme+"-Ground-Triangle-Right.png");
            } else {
                this.image = new Image("images/"+capitalizedTheme+"-Ground-Triangle-Left.png");
            }
        }
    }

    /**
     * Gets a value indicating whether the block is left sloped.
     *
     * @return true, if the block is left sloped, otherwise false.
     */
    public boolean getSlopedLeft() {
        return this.slopedLeft;
    }
}













