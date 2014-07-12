package de.hhu.propra14.team101;

import de.hhu.propra14.team101.TerrainObjects.AbstractTerrainObject;

import java.io.File;
import java.util.*;

/**
 * A level of the game.
 */
public class Level {
    private Terrain terrain;
    private ArrayList<int[]> wormStartPoints = new ArrayList<>();

    /** Theme of the level we're playing */
    public static String theme = "normal";

    /**
     * Initialized a new level.
     * @param terrain terrain of the new level.
     */
    public Level(Terrain terrain) {
        if(terrain == null)
        {
            throw new IllegalArgumentException("terrain must not be null.");
        }

        this.terrain = terrain;
    }

    /**
     * Count of all worm start positions.
     * @return count of worm start positions
     */
    public int getCountWormStartPositions()
    {
        return wormStartPoints.size();
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
     * Remove a specific start position
     * @param index Number to remove
     */
    public void removeWormStartPosition(int index) {
        wormStartPoints.remove(index);
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
     * @param players players with worms
     */
    public void setWormsStartPosition(ArrayList<Player> players)
    {
        //calculate: how many worms per player depending on available startPoints
        int wormsPerPlayer = wormStartPoints.size() / players.size();

        int indexStartPosition = 0;
        for (Player player : players) {
            ArrayList<Worm> wormsToRemove = new ArrayList<>();
            for (int indexWorm = 0; indexWorm < player.wormList.size(); indexWorm++) {
                if (indexStartPosition < wormStartPoints.size() && indexWorm < wormsPerPlayer) {
                    player.wormList.get(indexWorm).setXCoordinate(wormStartPoints.get(indexStartPosition)[0] * (int) (AbstractTerrainObject.baseSize * Main.sizeMultiplier));
                    player.wormList.get(indexWorm).setYCoordinate(wormStartPoints.get(indexStartPosition)[1] * (int) (AbstractTerrainObject.baseSize * Main.sizeMultiplier));
                    indexStartPosition++;
                } else if(!(indexWorm < wormsPerPlayer)) {
                   wormsToRemove.add(player.wormList.get(indexWorm));
                }
            }
            player.wormList.removeAll(wormsToRemove);
        }

    }

    /**
     * Serialize a level.
     * @return serialized data
     */
    public Map<String, Object> serialize () {
        Map<String, Object> result = new HashMap<>();
        result.put("spawns", wormStartPoints);
        result.put("terrain", this.terrain.serialize());
        result.put("theme", Level.theme);
        return result;
    }

    /**
     * Deserialize a level.
     * @param input serialized data
     * @return deserialzed level
     */
    public static Level deserialize (Map<String, Object> input) {
        Level.theme = input.get("theme").toString();
        @SuppressWarnings("unchecked")
        Terrain terrain = Terrain.deserialize((ArrayList<ArrayList<Map>>) input.get("terrain"));
        ArrayList spawns = (ArrayList) input.get("spawns");
        Level result = new Level(terrain);
        for (Object spawn : spawns) {
            ArrayList currentSpawn = (ArrayList) spawn;
            result.addWormStartPosition((int) currentSpawn.get(0), (int) currentSpawn.get(1));
        }
        return result;
    }

    /**
     * Loads levels from a local level directory
     * @return Array of local map names
     */
    public static ArrayList<String> loadLocalLevels() {
        File[] f = (new File("maps/")).listFiles();
        if (f == null) {
            return new ArrayList<>();
        }

        ArrayList<String> result = new ArrayList<>();
        for (File aFile : f) {
            String filename = aFile.toString();
            if (filename.endsWith(".gz")) {
                result.add(aFile.toString());
            }
        }
        return result;
    }
}