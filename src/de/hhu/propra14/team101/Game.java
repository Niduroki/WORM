package de.hhu.propra14.team101;

import com.sun.istack.internal.Nullable;
import javafx.scene.canvas.*;
import java.util.*;

/**
 * Game class to manage players, levels etc.
 */
public class Game {
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Level> levels = new ArrayList<Level>();
    private int selectedLevelNumber;

    /**
    * Initialize a new game.
    */
    public Game() {
        //TODO: Remove hard-coded players and levels
        //player 1
        Worm[] wormsPlayer1 = {new Worm(), new Worm(), new Worm()};
        players.add(new Player(wormsPlayer1,"Local"));

        //player 2
        Worm[] wormsPlayer2 = {new Worm(), new Worm(), new Worm()};
        players.add(new Player(wormsPlayer2,"Local"));

        //level1
        Terrain terrain1 = new Terrain(60, 40);
        for(int i = 0; i < 60; i++)
        {
            terrain1.addTerrainObject(new SquareBuildingBlock(), i, terrain1.getHeight()- 1);
            terrain1.addTerrainObject(new SquareBuildingBlock(), i, terrain1.getHeight() -2);
        }
        terrain1.addTerrainObject(new Obstacle(), 7, terrain1.getHeight() -3);
        Level level1 = new Level(terrain1, 0);
        for(int i = 5; i < 35;i += 5) {
            level1.addWormStartPosition(i, terrain1.getHeight() -4);
        }
        levels.add(level1);

        //level2
        Terrain terrain2 = new Terrain(60, 40);
        for(int i = 0; i < 60; i++)
        {
            terrain2.addTerrainObject(new SquareBuildingBlock(), i, terrain2.getHeight()- 1);
            terrain2.addTerrainObject(new SquareBuildingBlock(), i, terrain2.getHeight()- 2);
            terrain2.addTerrainObject(new SquareBuildingBlock(), i, terrain2.getHeight()- 3);
        }
        terrain2.addTerrainObject(new Obstacle(), 7, terrain2.getHeight()- 4);
        terrain2.addTerrainObject(new Obstacle(), 7, terrain2.getHeight()- 5);
        Level level2 = new Level(terrain2, 1);
        for(int i = 4; i < 35;i += 5) {
            level2.addWormStartPosition(i, terrain2.getHeight()- 5);
        }
        levels.add(level2);

        //level3
        Terrain terrain3 = new Terrain(60, 40);
        for(int i = 0; i < 55; i++)
        {
            terrain3.addTerrainObject(new SquareBuildingBlock(), i, terrain3.getHeight()- 1);
            terrain3.addTerrainObject(new SquareBuildingBlock(), i, terrain3.getHeight()- 2);
            terrain3.addTerrainObject(new SquareBuildingBlock(), i, terrain3.getHeight()- 3);
        }
        terrain3.addTerrainObject(new Obstacle(), 7, terrain3.getHeight()- 4);
        terrain3.addTerrainObject(new Obstacle(), 7, terrain3.getHeight()- 5);
        Level level3 = new Level(terrain3, 2);
        for(int i = 4; i < 35;i += 5) {
            level3.addWormStartPosition(i, terrain3.getHeight()- 5);
        }
        levels.add(level3);
    }

    /**
     * Get number of level, which is selected.
     */
    public int getSelectedLevelNumber()
    {
        return selectedLevelNumber;
    }

    /**
     * Get count of levels.
     */
    public int getCountLevel()
    {
        return levels.size();
    }

    /**
     * Add a level. Overwrites level, if level number exists.
     * @param level The new level.
     * @exception java.lang.IllegalArgumentException if level number is negative
     */
    public void addLevel(Level level)
    {
        if(level.getNumber() < 0)
        {
            throw new IllegalArgumentException("level number must be positive or null");
        }

        if(level.getNumber() < levels.size())
        {
            levels.set(level.getNumber(), level);
        } else {
            int index;
            for(index = levels.size(); index < level.getNumber(); index++) {
                levels.add(null);
            }
            levels.add(level);
        }
    }

    /**
     * Gets a level.
     * @param number the level number
     * @return a specific level
     * @exception java.lang.IllegalArgumentException
     */
    @Nullable
    public Level getLevel(int number) {
        if(number >= levels.size() || number < 0)
        {
            throw new IllegalArgumentException("number has to exist");
        }

        return  levels.get(number);
    }

    /**
     * Draw the level.
     * @param gc GraphicsContext to draw the level.
     */
    public void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        levels.get(selectedLevelNumber).getTerrain().draw(gc);

        for(int i = 0; i < players.size();i++)
        {
            for(int indexWorms = 0;indexWorms < players.get(i).wormArray.length;indexWorms++)
            {
                players.get(i).wormArray[indexWorms].draw(gc);
            }
        }
    }

    /**
    * Start the level and initialize terrain and worms.
    * @exception java.lang.IllegalArgumentException if levelNumber does not exist.
    */
    public void startLevel(int levelNumber, GraphicsContext gc) {
        if(levelNumber >= levels.size() || levelNumber < 0)
        {
            throw new IllegalArgumentException("Level does not exist.");
        }
        selectedLevelNumber = levelNumber;
        levels.get(selectedLevelNumber).setWormsStartPosition(players);

        draw(gc);
    }
}