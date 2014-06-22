package de.hhu.propra14.team101.TerrainObjects;

/**
 * Class to create terrain with
 */
public abstract class Item extends AbstractTerrainObject {

    /**
     * @param x X-Coordinate of the object
     * @param y Y-Coordinate of the object
     */
    public Item(int x, int y) {
        super(x, y);
    }

   abstract public String getName();
}
