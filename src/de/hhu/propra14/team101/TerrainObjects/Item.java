package de.hhu.propra14.team101.TerrainObjects;

/**
 * Class to create terrain with
 *
 * @see de.hhu.propra14.team101.Terrain
 */
public abstract class Item extends AbstractTerrainObject {

    protected String name;

    /**
     * Initialize a new Item
     *
     * @param x x-coordinate of the object
     * @param y y-coordinate of the object
     */
    public Item(int x, int y) {
        super(x, y);
    }

    /**
     * Gets the name.
     *
     * @return name of the item
     */
    public String getName() {
        return name;
    }
}
