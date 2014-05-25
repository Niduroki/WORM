package de.hhu.propra14.team101;

import com.sun.istack.internal.Nullable;
import de.hhu.propra14.team101.Savers.LevelSaves;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.util.*;

/**
 * Game class to manage players, levels etc.
 */
public class Game {
    private ArrayList<Player> players = new ArrayList<Player>();
    private ArrayList<Level> levels = new ArrayList<Level>();
    private Bullet bullet;
    public boolean bulletFired = false;
    private boolean gameFinished = false;
    private int selectedLevelNumber;
    private Terrain currentTerrain;
    public int round = 0;
    public int turnOfPlayer = 0;

    /**
     * Initialize a new game.
     */
    public Game() {
        //TODO: Remove hard-coded players
        //player 1
        ArrayList<Worm> wormsPlayer1 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            wormsPlayer1.add(new Worm());
        }
        Player player1 = new Player(wormsPlayer1, "Local");
        player1.name = "player1";
        player1.color = Color.GREEN;
        this.getPlayers().add(player1);

        //player 2
        ArrayList<Worm> wormsPlayer2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            wormsPlayer2.add(new Worm());
        }
        Player player2 = new Player(wormsPlayer2, "Local");
        player2.name = "player2";
        player2.color = Color.BLUE;
        this.getPlayers().add(player2);


        LevelSaves loader = new LevelSaves();
        try {
            levels.add(loader.load("maps/Map1.yml"));
            levels.add(loader.load("maps/Map2.yml"));
            levels.add(loader.load("maps/Map3.yml"));
        } catch (FileNotFoundException e) {
            //
        }

        // Hard coded levels - if save-files change uncomment this and save this structure
        /*
        //level1
        Terrain terrain1 = new Terrain(60, 40);
        for(int i = 0; i < terrain1.getWidth(); i++)
        {
            terrain1.addTerrainObject(new SquareBuildingBlock(i, terrain1.getHeight()-1));
            terrain1.addTerrainObject(new SquareBuildingBlock(i, terrain1.getHeight()-2));
        }
        terrain1.addTerrainObject(new Obstacle(7, terrain1.getHeight()-3));
        terrain1.addTerrainObject(new TriangleBuildingBlock(0, terrain1.getHeight()-3, false));
        terrain1.addTerrainObject(new TriangleBuildingBlock(terrain1.getWidth()-1, terrain1.getHeight()-3, true));

        Level level1 = new Level(terrain1, 0);
        for(int i = 5; i < 35;i += 5) {
            level1.addWormStartPosition(new Coordinate(i, terrain1.getHeight() -4));
        }
        levels.add(level1);

        //level2
        Terrain terrain2 = new Terrain(60, 40);
        for(int i = 0; i < 60; i++)
        {
            terrain2.addTerrainObject(new SquareBuildingBlock(i, terrain2.getHeight()- 1));
            terrain2.addTerrainObject(new SquareBuildingBlock(i, terrain2.getHeight()- 2));
            terrain2.addTerrainObject(new SquareBuildingBlock(i, terrain2.getHeight()- 3));
        }
        terrain2.addTerrainObject(new Obstacle(7, terrain2.getHeight()- 4));
        terrain2.addTerrainObject(new Obstacle(7, terrain2.getHeight()- 5));
        Level level2 = new Level(terrain2, 1);
        for(int i = 4; i < 35;i += 5) {
            level2.addWormStartPosition(new Coordinate(i, terrain2.getHeight()- 5));
        }
        levels.add(level2);

        //level3
        Terrain terrain3 = new Terrain(60, 40);
        for(int i = 0; i < 55; i++) {
            terrain3.addTerrainObject(new SquareBuildingBlock(i, terrain3.getHeight()- 1));
            terrain3.addTerrainObject(new SquareBuildingBlock(i, terrain3.getHeight()- 2));
            terrain3.addTerrainObject(new SquareBuildingBlock(i, terrain3.getHeight()- 3));
        }
        terrain3.addTerrainObject(new Obstacle(7, terrain3.getHeight()- 4));
        terrain3.addTerrainObject(new Obstacle(7, terrain3.getHeight()- 5));
        Level level3 = new Level(terrain3, 2);
        for(int i = 4; i < 35;i += 5) {
            level3.addWormStartPosition(new Coordinate(i, terrain3.getHeight()- 5));
        }
        levels.add(level3);
        */

    }

    /**
     * Get number of level, which is selected.
     */
    public int getSelectedLevelNumber() {
        return selectedLevelNumber;
    }

    /**
     * Get count of levels.
     */
    public int getCountLevel() {
        return levels.size();
    }

    /**
     * Add a level. Overwrites level, if level number exists.
     *
     * @param level The new level.
     * @throws java.lang.IllegalArgumentException if level number is negative
     */
    public void addLevel(Level level) {
        if (level.getNumber() < 0) {
            throw new IllegalArgumentException("level number must be positive or null");
        }

        if (level.getNumber() < levels.size()) {
            levels.set(level.getNumber(), level);
        } else {
            int index;
            for (index = levels.size(); index < level.getNumber(); index++) {
                levels.add(null);
            }
            levels.add(level);
        }
    }

    public void fireBullet(Bullet bullet) {
        this.bullet = bullet;
        bulletFired = true;
    }

    /**
     * Gets a level.
     *
     * @param number the level number
     * @return a specific level
     * @throws java.lang.IllegalArgumentException
     */
    @Nullable
    public Level getLevel(int number) {
        if (number >= levels.size() || number < 0) {
            throw new IllegalArgumentException("number has to exist");
        }

        return levels.get(number);
    }

    /**
     * Gets the current terrain.
     *
     * @return the current terrain
     */
    public Terrain getCurrentTerrain() {
        return currentTerrain;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    /**
     * Sets the current terrain.
     *
     * @param terrain Terrain to be used
     */
    public void setCurrentTerrain(Terrain terrain) {
        this.currentTerrain = terrain;
    }

    /**
     * Gets the players
     *
     * @return ArrayList of players
     */
    public ArrayList<Player> getPlayers() {
        return this.players;
    }

    /**
     * Sets the players
     *
     * @param players ArrayList of players
     */
    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    /**
     * Draw the level.
     *
     * @param gc GraphicsContext to draw the level.
     */
    private void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        this.currentTerrain.draw(gc);
        gc.fillText(String.valueOf(this.round), 300, 20);

        for (int i = 0; i < this.getPlayers().size(); i++) {
            for (int indexWorms = 0; indexWorms < this.getPlayers().get(i).wormList.size(); indexWorms++) {
                this.getPlayers().get(i).wormList.get(indexWorms).draw(gc, this.getPlayers().get(i).color);
            }
        }
        if(bulletFired)
        {
            bullet.draw(gc);
        }
    }

    /**
     * Update game
     */
    public void updateGame(GraphicsContext gc) {
        Worm currentWorm = this.getPlayers().get(turnOfPlayer).wormList.get(this.getPlayers().get(turnOfPlayer).currentWorm);

        if(!bulletFired) {
            String text;
            if (currentWorm.weaponList.size() == 0) {
                text = "No weapon";
            } else {
                text = currentWorm.weaponList.get(currentWorm.currentWeapon).name;
            }
            gc.fillText("Current weapon: " + text, 0, 10);

            // Remove dead players
            for (int i = 0; i < this.getPlayers().size(); i++) {
                if (this.getPlayers().get(i).wormList.size() == 0) {
                    this.getPlayers().remove(i);
                }
            }

            // Remove dead worms
            for (int i = 0; i < this.getPlayers().size(); i++) {
                for (int j = 0; j < this.getPlayers().get(i).wormList.size(); j++) {
                    if (this.getPlayers().get(i).wormList.get(j).health <= 0) {
                        this.getPlayers().get(i).wormList.remove(j);
                    }
                }
            }

            if (this.getPlayers().size() == 1) {
                gameFinished = true;
            }
        } else {
            bullet.physics.move();
            ArrayList<Worm> wormArrayList = new ArrayList<>();
            for(Player playerItem: this.getPlayers())
            {
                wormArrayList.addAll(playerItem.wormList);
            }
            Worm collisionWorm = bullet.physics.hasCollision(currentWorm, wormArrayList);
            if(collisionWorm != null) {
                collisionWorm.health -= bullet.weapon.damage;
                bulletFired = false;
                nextRound();
            }
        }

        draw(gc);
    }

    public void nextRound() {
        this.round += 1;
        this.getPlayers().get(turnOfPlayer).selectNextWorm();
        if (this.turnOfPlayer == this.getPlayers().size() - 1) {
            this.turnOfPlayer = 0;
        } else {
            this.turnOfPlayer += 1;
        }
    }

    /**
     * Start the level and initialize terrain and worms.
     *
     * @throws java.lang.IllegalArgumentException if levelNumber does not exist.
     */
    public void startLevel(int levelNumber, GraphicsContext gc) {
        if (levelNumber >= levels.size() || levelNumber < 0) {
            throw new IllegalArgumentException("Level does not exist.");
        }
        selectedLevelNumber = levelNumber;
        this.currentTerrain = levels.get(selectedLevelNumber).getTerrain();
        levels.get(selectedLevelNumber).setWormsStartPosition(this.getPlayers());

        draw(gc);
    }
}