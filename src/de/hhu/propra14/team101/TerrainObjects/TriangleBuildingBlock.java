package de.hhu.propra14.team101.TerrainObjects;

import de.hhu.propra14.team101.Main;
import javafx.scene.image.Image;

/**
 * Creates a triangle, dirt building block, either sloped left or right
 */
public class TriangleBuildingBlock extends AbstractTerrainObject {

    protected boolean slopedLeft;

    /**
     *
     * @param x
     * @param y
     * @param slopedLeft
     */
    public TriangleBuildingBlock (int x, int y, boolean slopedLeft) {
        super(x, y);
        this.slopedLeft = slopedLeft;
        this.destructible = true;

        if (!Main.headless) {
            if (this.getSlopedLeft()) {
                this.image = new Image("images/Normal-Ground-Triangle-Right.png");
            } else {
                this.image = new Image("images/Normal-Ground-Triangle-Left.png");
            }
        }
    }

    /**
     *
     * @param slopedLeft
     */
    public void setSlopedLeft (boolean slopedLeft) {
        this.slopedLeft = slopedLeft;
    }

    /**
     *
     * @return
     */
    public boolean getSlopedLeft() {
        return this.slopedLeft;
    }
}













