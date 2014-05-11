package de.hhu.propra14.team101;

/**
 * Class to create terrain with
 */
public class SquareBuildingBlock extends AbstractTerrainObject {

    protected int size = 10;

    protected int color = 0x883300; // Dirt-ish html-color

    protected boolean destructibility = true;

    public SquareBuildingBlock (int x, int y) {
        super(x, y);
    }

    /**
     * Draws the block
     */
    public void draw () {
        //
    }
}
