package de.hhu.propra14.team101;

import com.sun.istack.internal.Nullable;
import de.hhu.propra14.team101.Physics.*;
import de.hhu.propra14.team101.Savers.LevelSaves;
import de.hhu.propra14.team101.Savers.SettingSaves;
import de.hhu.propra14.team101.TerrainObjects.AbstractTerrainObject;
import de.hhu.propra14.team101.TerrainObjects.ExplosiveBuildingBlock;
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
    private boolean isJumping = false;
    public boolean online = false;
    public boolean loaded = false;
    public int round = 0;
    public int turnOfPlayer = 0;
    public int roundTimer = 20;
    public int fps;
    public GraphicsContext gc;
    public Queue<String> onlineCommandQueue = new PriorityQueue<>();
    // Necessary to tell the lobby javafx process to start the game now
    public static boolean startMe = false;
    public OggClip music;

    private ArrayList<Player> players = new ArrayList<>();
    private Level level;
    private Bullet bullet;
    private boolean gameFinished = false;
    private Terrain currentTerrain;
    private int secondCounter = 0;
    private Image background;
    protected Timeline timeline;
    public Thread gameUpdateThread;

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
            this.background = new Image("images/Background.jpg");
        }
    }

    public void fireBullet(Bullet bullet) {
        this.bullet = bullet;
        bulletFired = true;
    }

    /**
     * Gets a level.
     *
     * @return a specific level
     * @throws java.lang.IllegalArgumentException
     */
    @Nullable
    public Level getLevel() {
        return level;
    }

    public void loadLevel(String levelName) {
        if (levelName.equals("Random")) {
            RandomLevel generator = new RandomLevel();
            level = generator.generate();
        } else {
            LevelSaves loader = new LevelSaves();
            try {
                level = loader.load("maps/" + levelName + ".gz");
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't find level-file");
            }
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

    public boolean getIsJumping() {
        return isJumping;
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
        gc.setFont(new Font(12*Main.sizeMultiplier));
        gc.fillText("Current weapon: " + text, 5, 20*Main.sizeMultiplier);
        gc.fillText("Round: " + String.valueOf(this.round), gc.getCanvas().getWidth() / 2-50, 20*Main.sizeMultiplier);
        gc.fillText("Remaining Time: " + String.valueOf(this.roundTimer), gc.getCanvas().getWidth() - 125*Main.sizeMultiplier, 20*Main.sizeMultiplier);

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
            if (!this.getIsJumping()) {
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
                            try {
                                OggClip goodbyeClip = new OggClip("sfx/worms/Goodbye.ogg");
                                goodbyeClip.setGain(Main.svol);
                                goodbyeClip.play();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

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
                    bullet.physics.move(1);
                    ArrayList<Worm> wormArrayList = new ArrayList<>();
                    for (Player playerItem : this.getPlayers()) {
                        wormArrayList.addAll(playerItem.wormList);
                    }
                    Worm currentWorm = this.getPlayers().get(turnOfPlayer).wormList.get(this.getPlayers().get(turnOfPlayer).currentWorm);
                    Collision collision;
                    if (Main.headless) {
                        collision = bullet.physics.hasCollision(currentWorm, wormArrayList, this.getCurrentTerrain(), Terrain.getWidthInPixel(), Terrain.getHeightInPixel());
                    } else {
                        collision = bullet.physics.hasCollision(currentWorm, wormArrayList, this.getCurrentTerrain(), gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
                    }
                    if (collision != null) {
                        switch (collision.getType()) {
                            case Worm:
                                ((Worm) collision.getCollisionElement()).health -= bullet.weapon.damage;
                                Random random = new Random();
                                int randomInt = random.nextInt(3);

                                // Play a random sound, if a worm is hit
                                OggClip damageClip = null;
                                if (randomInt == 0) {
                                    try {
                                        damageClip = new OggClip("sfx/worms/Fleshwound.ogg");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (randomInt == 1) {
                                    try {
                                        damageClip = new OggClip("sfx/worms/Oh no.ogg");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    try {
                                        damageClip = new OggClip("sfx/worms/You'll pay.ogg");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                if (damageClip != null) {
                                    damageClip.setGain(Main.svol);
                                    damageClip.play();
                                }

                                bulletFired = false;
                                nextRound();
                                break;
                            case Terrain:
                                AbstractTerrainObject terrainObject = (AbstractTerrainObject) collision.getCollisionElement();
                                if (terrainObject.getDestructible()) {
                                    if (terrainObject.getClass() == ExplosiveBuildingBlock.class) {
                                        this.getCurrentTerrain().removeTerrainObject(terrainObject, true);
                                    } else {
                                        this.getCurrentTerrain().removeTerrainObject(terrainObject, false);
                                    }
                                }
                                try {
                                    OggClip doThisClip = new OggClip("sfx/weapons/Explosion.ogg");
                                    doThisClip.setGain(Main.svol);
                                    doThisClip.play();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            case TopOrDown:
                                bulletFired = false;
                                nextRound();
                                break;
                            case LeftOrRight:
                                bullet.physics = BallisticMovement.revert(bullet.physics);
                                break;
                        }
                    }
                }
            } else {
                ArrayList<Worm> wormArrayList = new ArrayList<>();
                for (Player playerItem : this.getPlayers()) {
                    wormArrayList.addAll(playerItem.wormList);
                }
                Worm currentWorm = this.getPlayers().get(turnOfPlayer).wormList.get(this.getPlayers().get(turnOfPlayer).currentWorm);
                if (currentWorm.jump(this.getCurrentTerrain(), wormArrayList)) {
                    isJumping = false;
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

        if (!Main.headless) {
            Worm currentWorm = this.getPlayers().get(turnOfPlayer).wormList.get(this.getPlayers().get(turnOfPlayer).currentWorm);
            if (currentWorm.health <= 15) {
                try {
                    OggClip goodbyeClip = new OggClip("sfx/worms/Heartbeat.ogg");
                    goodbyeClip.setGain(Main.svol);
                    goodbyeClip.play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                // Make the worm say "Let's do this"
                try {
                    OggClip doThisClip = new OggClip("sfx/worms/Do this.ogg");
                    doThisClip.setGain(Main.svol);
                    doThisClip.play();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        if (!Main.headless) {
            try {
                // Default music: Normal
                String musicPath = "Normal-Game.ogg";
                if (level.theme.equals("oriental")) {
                    musicPath = "Oriental-Game.ogg";
                } else if (level.theme.equals("horror")) {
                    musicPath = "Horror-Game.ogg";
                }
                music = new OggClip("music/"+musicPath);

                music.setGain(Main.mvol);

                music.loop();
            } catch (IOException f) {
                f.printStackTrace();
            }
        }

        if (!this.online && !this.loaded) {
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
        final Duration oneFrameAmt = Duration.millis(1000 / fps);
        final KeyFrame keyFrame = new KeyFrame(oneFrameAmt,
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        if (gameFinished) {
                            music.pause();
                            stopUpdating();
                        } else {
                            updateGame();
                            draw(gc);
                        }

                    }
                }
        );

        if (!Main.headless) {
            // Construct a timeline with the mainloop
            this.timeline = new Timeline(keyFrame);
            this.timeline.setCycleCount(Animation.INDEFINITE);
            this.timeline.play();
        } else {
            gameUpdateThread = new Thread(new GameUpdateThread(this));
            gameUpdateThread.setDaemon(true);
            gameUpdateThread.start();
        }
    }

    private void stopUpdating() {
        this.timeline.stop();

        if (Main.headless) {
            // If we're using a thread instead of a timeline interrupt (i.e. stop) it
            gameUpdateThread.interrupt();
        }

        if (!Main.headless) {
            try {
                music = new OggClip("music/Victory.ogg");
                music.setGain(Main.mvol);
                music.resume();
            } catch (IOException f) {
                f.printStackTrace();
            }
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
            players.get(this.turnOfPlayer).wormList.get(currentWorm).move('r', this.getCurrentTerrain(), this.getPlayers());
        } else if (action.equals("move_left")) {
            int currentWorm = players.get(this.turnOfPlayer).currentWorm;
            players.get(this.turnOfPlayer).wormList.get(currentWorm).move('l', this.getCurrentTerrain(), this.getPlayers());
        } else if (action.equals("jump")) {
            ArrayList<Worm> wormArrayList = new ArrayList<>();
            for (Player playerItem : this.getPlayers()) {
                wormArrayList.addAll(playerItem.wormList);
            }
            isJumping = true;
            int currentWorm = players.get(this.turnOfPlayer).currentWorm;
            players.get(this.turnOfPlayer).wormList.get(currentWorm).jump(this.getCurrentTerrain(), wormArrayList);
        } else if (action.equals("next_weapon")) {
            players.get(this.turnOfPlayer).wormList.get(players.get(this.turnOfPlayer).currentWorm).nextWeapon();
        } else if (action.equals("prev_weapon")) {
            players.get(this.turnOfPlayer).wormList.get(players.get(this.turnOfPlayer).currentWorm).prevWeapon();
        } else if (action.equals("pause")) {
            this.paused = !this.paused;
        } else if (action.matches("fire .+ .+")) {
            Worm currentWorm = players.get(this.turnOfPlayer).wormList.get(players.get(this.turnOfPlayer).currentWorm);
            // Don't fire without a weapon
            if (currentWorm.weaponList.size() != 0) {
                double xCoordinate = Double.parseDouble(action.split(" ")[1]);
                double yCoordinate = Double.parseDouble(action.split(" ")[2]);
                // Network fired weapon coordinates are with Main.sizeMultiplier = 1, scale it up if needed
                if (this.online==true) {
                    xCoordinate *= Main.sizeMultiplier;
                    yCoordinate *= Main.sizeMultiplier;
                }
                this.fireBullet(currentWorm.fireWeapon(xCoordinate, yCoordinate));
            }
        } else {
            System.out.println("Unknown action");
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("level", this.level.serialize());
        result.put("players", this.serializePlayerArray());
        result.put("round", this.round);
        result.put("turn_of_player", this.turnOfPlayer);

        return result;
    }

    @SuppressWarnings("unchecked")
    public static Game deserialize(Map<String, Object> data) {
        Game game = new Game(Game.deserializePlayerArray((ArrayList<Map>) data.get("players")));
        game.level = Level.deserialize((Map<String, Object>) data.get("level"));
        game.setCurrentTerrain(game.level.getTerrain());
        game.round = (Integer) data.get("round");
        game.turnOfPlayer = (Integer) data.get("turn_of_player");
        return game;
    }

    private Object[] serializePlayerArray() {
        Object[] result = new Object[players.size()];
        for (int i = 0; i < players.size(); i++) {
            result[i] = players.get(i).serialize();
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
            // Will be set to false when interrupted, thus interrupt=stop
            boolean run = true;

            while (run && !game.isGameFinished()) {
                game.updateGame();
                try {
                    Thread.sleep(1000 / game.fps);
                } catch (InterruptedException e) {
                    System.out.println("GameUpdateThread was interrupted. Stopping now.");
                    run = false;
                }
            }
        }
    }
}