package de.hhu.propra14.team101.TerrainObjects;

/**
 * Class to create terrain with
 */
public abstract class Item extends AbstractTerrainObject {
    /** Name of the item */
    public String name;

    /**
     * @param x X-Coordinate of the object
     * @param y Y-Coordinate of the object
     */
    public Item(int x, int y) {
        super(x, y);
    }

    /**
     *
     * @deprecated Use the public property name instead
     * @return
     */
    public String getName() {
        return this.name;
    }
}
