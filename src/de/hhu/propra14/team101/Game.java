package de.hhu.propra14.team101;

import com.sun.istack.internal.Nullable;
import de.hhu.propra14.team101.Savers.LevelSaves;
import de.hhu.propra14.team101.Savers.SettingSaves;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.*;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import org.newdawn.easyogg.OggClip;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Game class to manage players, levels etc.
 */
public class Game {
    public boolean paused = false;
    public boolean bulletFired = false;
    public boolean online = false;
    public int round = 0;
    public int turnOfPlayer = 0;
    public int roundTimer = 20;
    public int fps;
    public GraphicsContext gc;
    public Queue<String> onlineCommandQueue = new PriorityQueue<>();
    // Necessary to tell the lobby javafx process to start the game now
    public static boolean startMe = false;
    private OggClip music;

    private ArrayList<Player> players = new ArrayList<>();
    private Level level;
    private Bullet bullet;
    private boolean gameFinished = false;
    private Terrain currentTerrain;
    private int secondCounter = 0;
    private Image background;
    protected Timeline timeline;

    /**
     * Initialize a new game.
     */
    public Game(ArrayList players) {
        // Load fps from settings
        SettingSaves settingsLoader = new SettingSaves();
        try {
            this.fps = Integer.parseInt((String) settingsLoader.load("settings.gz").get("fps"));
        } catch (FileNotFoundException | NumberFormatException e) {
            this.fps = 16;
        }

        for (Object player : players) {
            this.getPlayers().add((Player) player);
        }

        if (!Main.headless) {
            this.background = new Image("Background.jpg");
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

    public void fireBullet(Bullet bullet) {
        this.bullet = bullet;
        bulletFired = true;
    }

    /**
     * Gets a level.
     * @return a specific level
     * @throws java.lang.IllegalArgumentException
     */
    @Nullable
    public Level getLevel() {
        return level;
    }

    public void loadLevel(String levelName) {
        LevelSaves loader = new LevelSaves();
        try {
            level = loader.load("maps/"+levelName+".gz");
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find level-file");
        }
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
     * Draw the level.
     *
     * @param gc GraphicsContext to draw the level.
     */
    private void draw(GraphicsContext gc) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        gc.drawImage(this.background, 0.0, 0.0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        Worm currentWorm;
        try {
            currentWorm = this.getPlayers().get(turnOfPlayer).wormList.get(this.getPlayers().get(turnOfPlayer).currentWorm);
        } catch (IndexOutOfBoundsException e) {
            // FIXME when a player has 0 worms left he isn't removed now, in that case just draw nothing this frame for now
            System.out.println("FIXME IndexOutOfBoundsException in Game.draw()");
            return;
        }

        String text;
        if (currentWorm.weaponList.size() == 0) {
            text = "No weapon";
        } else {
            text = currentWorm.weaponList.get(currentWorm.currentWeapon).name;
        }
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(12));
        gc.fillText("Current weapon: " + text, 0, 10);
        gc.fillText(String.valueOf(this.round), gc.getCanvas().getWidth()/2, 15);
        gc.fillText(String.valueOf(this.roundTimer), gc.getCanvas().getWidth()-15, 10);

        this.currentTerrain.draw(gc);

        for (int i = 0; i < this.getPlayers().size(); i++) {
            for (int indexWorms = 0; indexWorms < this.getPlayers().get(i).wormList.size(); indexWorms++) {
                this.getPlayers().get(i).wormList.get(indexWorms).draw(gc, this.getPlayers().get(i).color);
            }
        }
        if (bulletFired) {
            bullet.draw(gc);
        }

        if (this.paused) {
            gc.setFill(Color.BLACK);
            gc.setFont(new Font(25));
            gc.fillText("PAUSED", 260, 180);
        }
    }

    /**
     * Update game
     */
    public void updateGame() {
        if (this.online && !this.onlineCommandQueue.isEmpty()) {
            this.doAction(this.onlineCommandQueue.poll());
        }

        if (!this.paused) {
            this.secondCounter += 1;

            if (this.secondCounter == this.fps) {
                this.secondCounter = 0;
                this.roundTimer -= 1;
                if (this.roundTimer == 0) {
                    this.nextRound();
                    this.roundTimer = 20;
                }
            }


            // Remove dead players
            for (int i = 0; i < this.getPlayers().size(); i++) {
                if (this.getPlayers().get(i).wormList.isEmpty()) {
                    this.getPlayers().remove(i);
                }
            }

            // Remove dead worms
            for (int i = 0; i < this.getPlayers().size(); i++) {
                for (int j = 0; j < this.getPlayers().get(i).wormList.size(); j++) {
                    if (this.getPlayers().get(i).wormList.get(j).health <= 0) {
                        this.getPlayers().get(i).wormList.remove(j);

                        // Decrement current worm to prevent an IndexOutOfBoundsException
                        if (this.getPlayers().get(i).currentWorm != 0) {
                            this.getPlayers().get(i).currentWorm -= 1;
                        }
                    }
                }
            }

            if (this.getPlayers().size() == 1) {
                this.gameFinished = true;
            }

            if (bulletFired) {
                bullet.physics.move(4);
                ArrayList<Worm> wormArrayList = new ArrayList<>();
                for (Player playerItem : this.getPlayers()) {
                    wormArrayList.addAll(playerItem.wormList);
                }
                Worm currentWorm = this.getPlayers().get(turnOfPlayer).wormList.get(this.getPlayers().get(turnOfPlayer).currentWorm);
                Collision collision;
                if (Main.headless) {
                    collision = bullet.physics.hasCollision(currentWorm, wormArrayList, this.getCurrentTerrain(), 600, 400);
                } else {
                    collision = bullet.physics.hasCollision(currentWorm, wormArrayList, this.getCurrentTerrain(),  gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                }
                if (collision != null) {
                    switch (collision.getType()) {
                        case Worm:
                            ((Worm) collision.getCollisionElement()).health -= bullet.weapon.damage;
                            bulletFired = false;
                            nextRound();
                            break;
                        case Terrain:
                        case TopOrDown:
                            bulletFired = false;
                            nextRound();
                            break;
                        case LeftOrRight:
                            bullet.physics = Physics.Revert(bullet.physics);
                            break;
                    }
                }
            }
        }
    }

    public void nextRound() {
        this.round += 1;
        this.getPlayers().get(turnOfPlayer).selectNextWorm();
        if (this.turnOfPlayer == this.getPlayers().size() - 1) {
            this.turnOfPlayer = 0;
        } else {
            this.turnOfPlayer += 1;
        }

        // Reset frame counter for seconds and roundTimer
        this.secondCounter = 0;
        this.roundTimer = 20;
        System.out.println("Next");
    }

    /**
     * Start the level and initialize terrain and worms.
     */
    public void startLevel() {
        try {
            music = new OggClip("music/Main-Theme.ogg");
            music.loop();
        } catch (IOException f) {
            f.printStackTrace();
        }
        if (!this.online) {
            this.currentTerrain = level.getTerrain();
            level.setWormsStartPosition(this.getPlayers());
        }

        if (!Main.headless) {
            draw(this.gc);
        }
    }

    /**
     * Starts the gameplay
     */
    public void startGameplay() {
        this.startLevel();

        //Prepare updating game
        final Duration oneFrameAmt = Duration.millis(1000/fps);
        final KeyFrame keyFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        if(gameFinished) {
                            music.pause();
                            stopUpdating();
                        } else{
                            updateGame();
                            draw(gc);
                        }

                    }
                });

        if (!Main.headless) {
            // Construct a timeline with the mainloop
            this.timeline = new Timeline(keyFrame);
            this.timeline.setCycleCount(Animation.INDEFINITE);
            this.timeline.play();
        } else {
            Thread gameUpdateThread = new Thread(new GameUpdateThread(this));
            gameUpdateThread.setDaemon(true);
            gameUpdateThread.start();
        }
    }

    private void stopUpdating() {
        this.timeline.stop();
        try {
            music = new OggClip("music/Victory.ogg");
            music.resume();
        } catch (IOException f) {
            f.printStackTrace();
        }
        this.winScreen(players.get(0).name);
    }

    private void winScreen(String name) {
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
        gc.setFill(Color.BLACK);
        gc.setFont(new Font(25));
        gc.fillText("Player " + name + " won!", 70, 150);
        gc.setFont(new Font(15));
        gc.fillText("Press Escape to return to main menu", 70, 200);
    }

    public void doAction(String action) {
        System.out.println("Do the action");
        if (action.equals("move_right")) {
            int currentWorm = players.get(this.turnOfPlayer).currentWorm;
            players.get(this.turnOfPlayer).wormList.get(currentWorm).move('r');
        } else if (action.equals("move_left")) {
            int currentWorm = players.get(this.turnOfPlayer).currentWorm;
            players.get(this.turnOfPlayer).wormList.get(currentWorm).move('l');
        } else if (action.equals("pause")) {
            this.paused = !this.paused;
        } else if (action.matches("fire .+ .+")) {
            Worm currentWorm = players.get(this.turnOfPlayer).wormList.get(players.get(this.turnOfPlayer).currentWorm);
            // Don't fire without a weapon
            if (currentWorm.weaponList.size() != 0) {
                this.fireBullet(currentWorm.fireWeapon(Double.parseDouble(action.split(" ")[1]), Double.parseDouble(action.split(" ")[2])));
            }
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("terrain", this.currentTerrain.serialize());
        result.put("players", this.serializePlayerArray());
        result.put("round", this.round);
        result.put("turn_of_player", this.turnOfPlayer);

        return result;
    }

    public static Game deserialize(Map<String, Object> data) {
        Game game = new Game(Game.deserializePlayerArray((ArrayList<Map>) data.get("players")));
        game.setCurrentTerrain(Terrain.deserialize((ArrayList<ArrayList<Map>>) data.get("terrain")));
        game.round = (Integer) data.get("round");
        game.turnOfPlayer = (Integer) data.get("turn_of_player");
        return game;
    }

    private Object[] serializePlayerArray() {
        Object[] result = new Object[players.size()];
        for (int i=0; i<players.size(); i++) {
            result[i] =  players.get(i).serialize();
        }
        return result;
    }

    private static ArrayList<Player> deserializePlayerArray(ArrayList<Map> input) {
        ArrayList<Player> result = new ArrayList<>();
        for (Map anInput : input) {
            result.add(Player.deserialize(anInput));
        }
        return result;
    }

    static class GameUpdateThread implements Runnable {

        private Game game;

        public GameUpdateThread(Game game) {
            this.game = game;
        }

        @Override
        public void run() {
            while (!game.isGameFinished()) {
                game.updateGame();
                try {
                    Thread.sleep(1000 / game.fps);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}