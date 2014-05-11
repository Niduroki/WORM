package de.hhu.propra14.team101;

/**
 * Class to create terrain with
 */
public class Obstacle extends AbstractTerrainObject {

    protected int size = 10;

    protected int color = 0x777777; // Stone-ish html-color

    protected boolean destructibility = false;

    public Obstacle (int x, int y) {
        super(x, y);
    }

    /**
     * Draws the block
     */
    public void draw () {
        //
    }
}
