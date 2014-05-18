package de.hhu.propra14.team101;

import java.util.*;

/**
 * A level of the game.
 */
public class Level {
    private Terrain terrain;
    private int levelNumber;
    private ArrayList<int[]> wormStartPoints = new ArrayList<int[]>();

    /**
     * Initialized a new level.
     * @param terrain terrain of the new level.
     * @param levelNumber the level number.
     */
    public Level(Terrain terrain, int levelNumber) {
        if(terrain == null)
        {
            throw new IllegalArgumentException("terrain must not be null.");
        }

        this.terrain = terrain;
        this.levelNumber = levelNumber;
    }

    /**
     * Count of all worm start positions.
     */
    public int getCountWormStartPositions()
    {
        return wormStartPoints.size();
    }

    /**
     * Get the number of level.
     * @return the number
     */
    public int getNumber()
    {
        return this.levelNumber;
    }

    /**
     * Set level number.
     * @param number
     */
    public void setNumber(int number)
    {
        this.levelNumber = number;
    }

    /**
     * Get a specific start position
     * @param index index of position.
     * @return worm start position as a int array (length: 2)
     */
    public int[] getWormStartPosition(int index)
    {
        return wormStartPoints.get(index);
    }

    /**
     * Add a worm start position.
     * @param xPosition x coordinate
     * @param yPosition y coordinate
     */
    public void addWormStartPosition(int xPosition, int yPosition)
    {
        int[] position = new int[2];
        position[0] = xPosition;
        position[1] = yPosition;
        wormStartPoints.add(position);
    }

    /**
     * Get the terrain of level.
     * @return the terrain
     */
    public Terrain getTerrain()
    {
        return this.terrain;
    }

    /**
     * Initialize worms with their start positions.
     * @param players
     */
    public void setWormsStartPosition(ArrayList<Player> players)
    {
        int indexStartPosition = 0;
        for(int index = 0; index < players.size(); index++)
        {
            for(int indexWorm = 0; indexWorm < players.get(index).wormArray.length; indexWorm++)
            {
                if(indexStartPosition < wormStartPoints.size()) {
                    players.get(index).wormArray[indexWorm].setXCoordinate(wormStartPoints.get(indexStartPosition)[0]*AbstractTerrainObject.getSize());
                    players.get(index).wormArray[indexWorm].setYCoordinate(wormStartPoints.get(indexStartPosition)[1]*AbstractTerrainObject.getSize());
                    indexStartPosition++;
                }
            }
        }
        //TODO: set positions randomized, when more worms than positions
    }
}