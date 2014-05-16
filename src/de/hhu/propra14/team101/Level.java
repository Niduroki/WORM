package de.hhu.propra14.team101;

import java.util.*;

/**
 *
 */
public class Level {
    private Terrain terrain;
    private int levelNumber;
    private ArrayList<int[]> wormStartPoints = new ArrayList<int[]>();

    public Level(Terrain terrain, int levelNumber) {
        if(terrain == null)
        {
            throw new IllegalArgumentException("terrain must not be null.");
        }

        this.terrain = terrain;
        this.levelNumber = levelNumber;
    }

    public int getCountWormStartPositions()
    {
        return wormStartPoints.size();
    }

    public int getNumber()
    {
        return this.levelNumber;
    }

    public void setNumber(int number)
    {
        this.levelNumber = number;
    }

    public int[] getWormStartPosition(int index)
    {
        return wormStartPoints.get(index);
    }

    public void addWormStartPosition(int xPosition, int yPosition)
    {
        int[] position = new int[2];
       position[0] = xPosition;
        position[1] = yPosition;
        wormStartPoints.add(position);
    }

    public Terrain getTerrain()
    {
        return this.terrain;
    }

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