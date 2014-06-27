package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Savers.LevelSaves;
import de.hhu.propra14.team101.TerrainObjects.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * Class used for ingame level editing
 */
public class LevelCreator {
    /** Graphics context to draw onto, needs to be set before calling draw */
    public GraphicsContext gc;

    /** */
    private Level level;

    /**
     *
     */
    public LevelCreator() {
        this.level = new Level(new Terrain());
    }

    public void save(String path) {
        LevelSaves saver = new LevelSaves();
        saver.save(this.level, path);
    }

    public void setTheme(String theme) {
        this.level.theme = theme;
    }

    public void cycleBlock(int x, int y) {
        AbstractTerrainObject currentBlock = this.level.getTerrain().isTerrain(x*(Main.sizeMultiplier*AbstractTerrainObject.baseSize), y*(Main.sizeMultiplier*AbstractTerrainObject.baseSize));
        try {
            System.out.println(currentBlock.toString() + ":" + x + ":" + y);
        } catch (NullPointerException e) {
            System.out.println("Null:" + x + ":" + y);
        }
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

    public void cycleSpawn(int x, int y) {
        for (int i = 0; i < this.level.getCountWormStartPositions(); i++) {
            int[] coords = this.level.getWormStartPosition(i);
            if (coords[0] == x && coords[1] == y) {
                this.level.removeWormStartPosition(i);
                // Removed the spawn - Done
                return;
            }
        }

        // There isn't a worm here
        this.level.addWormStartPosition(x, y);

        this.draw();
    }

    public void draw() {
        assert this.gc != null;

        this.gc.clearRect(0, 0, this.gc.getCanvas().getWidth(), this.gc.getCanvas().getHeight());

        this.gc.drawImage(new Image("images/Background.jpg"), 0.0, 0.0, this.gc.getCanvas().getWidth(), this.gc.getCanvas().getHeight());

        this.level.getTerrain().draw(this.gc);

        for (int i = 0; i < this.level.getCountWormStartPositions(); i++) {
            int[] coords = this.level.getWormStartPosition(i);
            this.gc.drawImage(new Image("images/Worm.gif"), coords[0], coords[1], Worm.size*Main.sizeMultiplier, Worm.size*Main.sizeMultiplier);
        }

    }
}
