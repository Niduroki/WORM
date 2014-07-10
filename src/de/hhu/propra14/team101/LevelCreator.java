package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Savers.LevelSaves;
import de.hhu.propra14.team101.TerrainObjects.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class used for ingame level editing
 * <pre>
 * {@code
 * LevelCreator level = new Level();
 * //level creation, for example:
 * level.cycleBlock(0,0);
 * level.cycleBlock(0,0);
 * level.cycleBlock(1,0);
 * level.spawn(1,1);
 * level.draw();
 * level.save(newLevel.gz);
 * }
 * </pre>
 */
public class LevelCreator {
    /** Graphics context to draw onto, needs to be set before calling draw */
    public GraphicsContext gc;

    /** created level */
    private Level level;

    /**
     * Initialize a new level creation.
     */
    public LevelCreator() {
        this.level = new Level(new Terrain());
    }

    /**
     * Save created level
     * @param path location path of level
     */
    public void save(String path) {
        LevelSaves saver = new LevelSaves();
        saver.save(this.level, path);
    }

    /**
     * Sets the theme.
     * @param theme theme value
     */
    public void setTheme(String theme) {
        Level.theme = theme;
    }

    /**
     * Change block type at given point.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void cycleBlock(int x, int y) {
        AbstractTerrainObject currentBlock = this.level.getTerrain().isTerrain(
                x*(Main.sizeMultiplier*AbstractTerrainObject.baseSize),
                y*(Main.sizeMultiplier*AbstractTerrainObject.baseSize)
        );
        AbstractTerrainObject nextBlock;
        if (currentBlock == null) {
            nextBlock = new SquareBuildingBlock(x, y);
        } else if (currentBlock instanceof SquareBuildingBlock) {
            nextBlock = new TriangleBuildingBlock(x, y, true);
        } else if (currentBlock instanceof TriangleBuildingBlock) {
            if (((TriangleBuildingBlock) currentBlock).getSlopedLeft()) {
                nextBlock = new TriangleBuildingBlock(x, y, false);
            } else {
                nextBlock = new Obstacle(x, y);
            }
        } else if (currentBlock instanceof Obstacle) {
            nextBlock = new ExplosiveBuildingBlock(x, y);
        } else if (currentBlock instanceof ExplosiveBuildingBlock) {
            nextBlock = new SandBuildingBlock(x, y);
        } else if (currentBlock instanceof SandBuildingBlock) {
            nextBlock = new Elixir(x, y);
        } else if (currentBlock instanceof Elixir) {
            nextBlock = new Shoe(x, y);
        } else if (currentBlock instanceof Shoe) {
            nextBlock = new Spring(x, y);
        } else {
            nextBlock = null;
        }
        this.level.getTerrain().removeTerrainObject(currentBlock, false);
        if (nextBlock != null) {
            this.level.getTerrain().addTerrainObject(nextBlock);
        }
        this.draw();
    }

    /**
     * Add or remove spawn.
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void cycleSpawn(int x, int y) {
        int roundedX = (int) Math.round(x/(AbstractTerrainObject.baseSize*Main.sizeMultiplier));
        int roundedY = (int) Math.round(y/(AbstractTerrainObject.baseSize*Main.sizeMultiplier));
        for (int i = 0; i < this.level.getCountWormStartPositions(); i++) {
            int[] coords = this.level.getWormStartPosition(i);
            if (coords[0] == roundedX && coords[1] == roundedY) {
                this.level.removeWormStartPosition(i);
                // Removed the spawn - Done
                this.draw();
                return;
            }
        }

        // There isn't a worm here
        this.level.addWormStartPosition(roundedX, roundedY);

        this.draw();
    }

    /**
     * Draw level
     */
    public void draw() {
        assert this.gc != null;

        this.gc.clearRect(0, 0, this.gc.getCanvas().getWidth(), this.gc.getCanvas().getHeight());

        this.gc.drawImage(new Image("images/Normal-Background.jpg"), 0.0, 0.0, this.gc.getCanvas().getWidth(), this.gc.getCanvas().getHeight());

        this.level.getTerrain().draw(this.gc);

        for (int i = 0; i < this.level.getCountWormStartPositions(); i++) {
            int[] coords = this.level.getWormStartPosition(i);
            this.gc.drawImage(
                    new Image("images/worm-Normal.gif"),
                    coords[0]*AbstractTerrainObject.baseSize*Main.sizeMultiplier,
                    coords[1]*AbstractTerrainObject.baseSize*Main.sizeMultiplier,
                    Worm.size,
                    Worm.size
            );
        }

    }
}
