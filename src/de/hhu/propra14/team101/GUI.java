package de.hhu.propra14.team101;

import de.hhu.propra14.team101.GUIElements.DisallowedCharTextField;
import de.hhu.propra14.team101.GUIElements.Popup;
import de.hhu.propra14.team101.Networking.NetworkClient;
import de.hhu.propra14.team101.Savers.GameSaves;
import de.hhu.propra14.team101.Savers.SettingSaves;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import de.hhu.propra14.team101.Weapons.AtomicBomb;
import de.hhu.propra14.team101.Weapons.Bazooka;
import de.hhu.propra14.team101.Weapons.Grenade;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static javafx.geometry.Pos.*;

/**
 * Class that shows the GUI
 *
 * <pre>
 * {@code
 * // From Main
 * GUI gui = new GUI(this);
 * gui.addMainButtons();
 * gui.addEditorButtons();
 * }
 * </pre>
 */
public class GUI {
    private Main main;
    protected LevelCreator levelCreator;
    protected String levelCreatorOutputPath;

    /**
     * Creates a new GUI class
     * @param main Main class the application was started with
     */
    public GUI (Main main) {
        this.main = main;
    }

    /**
     * Adds main menu buttons
     */
    public void addMainButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 0, 20, 20));

        Text sceneTitle = new Text("Wreck Opponents Repeatedly Meaningless");
        Button startButton = new Button("Start");
        Button multiButton= new Button("Multiplayer");
        Button levelEditor=new Button ("Level Editor");
        Button optionsButton = new Button("Options");
        Button exitButton = new Button("Exit");



        startButton.setMaxWidth(Double.valueOf(150));
        multiButton.setMaxWidth(Double.valueOf(150));
        levelEditor.setMaxWidth(Double.valueOf(150));
        optionsButton.setMaxWidth(Double.valueOf(150));
        exitButton.setMaxWidth(Double.valueOf(150));

        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(0, 20, 10, 20));
        vbButtons.getChildren().addAll(startButton, multiButton, optionsButton, exitButton);


        // Configure each object
        sceneTitle.setFont(new Font(20));

        // Add the objects
        this.main.grid.add(sceneTitle, 0, 0, 2, 2);
        this.main.grid.add(startButton, 1, 2);
        this.main.grid.add(multiButton, 1, 4);
        this.main.grid.add(levelEditor,1,6);
        this.main.grid.add(optionsButton, 1, 8);
        this.main.grid.add(exitButton, 1, 10);

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                main.players = new ArrayList<>();
                main.availableColors = new ArrayList<>();
                main.availableColors.add("Red");
                main.availableColors.add("Green");
                main.availableColors.add("Blue");
                main.availableColors.add("Yellow");
                addPlayerButtons();
            }
        });

        optionsButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addOptionsButtons();
            }
        });

        multiButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main.client = new NetworkClient(main);
                main.lobby.addMpButtons();
            }
        });

        levelEditor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                levelCreator = new LevelCreator();
                addEditorButtons();
            }
        });

        exitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main.primaryStage.close();
            }
        });
    }

    /**
     * Adds the level editor buttons
     */
    public void addEditorButtons() {
        this.main.grid.getChildren().clear();
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 0, 20, 20));

        Button showeditorbutton = new Button("Show Editor");
        Button outputButton = new Button("Select Output ...");
        Button createButton = new Button("Create");
        Button returnButton = new Button("Back");

        final ComboBox<String> themeSelection = new ComboBox<>();
        themeSelection.getItems().addAll("Normal", "Horror", "Oriental");
        themeSelection.setValue("Normal");

        showeditorbutton.setMaxWidth(Double.MAX_VALUE);
        outputButton.setMaxWidth(Double.MAX_VALUE);
        createButton.setMaxWidth(Double.MAX_VALUE);
        returnButton.setMaxWidth(Double.MAX_VALUE);
        themeSelection.setMaxWidth(Double.MAX_VALUE);

        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(0, 20, 10, 20));
        vbButtons.getChildren().addAll(showeditorbutton, outputButton, createButton, returnButton);

        this.main.grid.add(showeditorbutton, 1, 2);
        this.main.grid.add(themeSelection, 1,6);
        this.main.grid.add(outputButton, 1, 4);
        this.main.grid.add(createButton, 1, 8);
        this.main.grid.add(returnButton, 1, 10);

        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
            }
        });

        showeditorbutton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                main.field = new Canvas(Terrain.getWidthInPixel(), Terrain.getHeightInPixel());
                main.grid.getChildren().clear();
                main.grid.add(main.field, 0, 0);
                main.initializeLevelCreatorHandlers(levelCreator);
                levelCreator.gc = main.field.getGraphicsContext2D();
                levelCreator.draw();
            }
        });


        outputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                File outputFile = fileChooser.showSaveDialog(main.primaryStage);
                levelCreatorOutputPath = outputFile.toString();
            }
        });

        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                levelCreator.setTheme(themeSelection.getSelectionModel().getSelectedItem().toLowerCase());
                if (levelCreatorOutputPath != null) {
                    levelCreator.save(levelCreatorOutputPath);
                    addMainButtons();
                }
            }
        });

    }

    /**
     * Adds the option menu buttons
     */
    private void addOptionsButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text sceneTitle = new Text("Options");
        Text title1 = new Text("Multiplayer Server");
        Text title2 = new Text("Multiplayer Name");
        Text title3 = new Text("Frames per second");
        Text title4 = new Text("Resolution");
        Text title5 = new Text("Music Volume");
        Text title6 = new Text("Sound Volume");
        Button returnButton = new Button("Save & Return");


        final ComboBox<String> fpsBox = new ComboBox<>();
        fpsBox.getItems().addAll("15", "20", "30", "45", "60");
        final ComboBox<String> resBox = new ComboBox<>();
        resBox.getItems().addAll("600x400", "900x600", "1200x800", "1500x1000");
        final Slider musicvol = new Slider();
        musicvol.setMin(0);
        musicvol.setMax(100);
        musicvol.setValue(50);
        musicvol.setShowTickLabels(true);
        musicvol.setShowTickMarks(true);
        musicvol.setMajorTickUnit(50);
        musicvol.setMinorTickCount(5);
        musicvol.setBlockIncrement(10);
        final Slider soundvol = new Slider();
        soundvol.setMin(0);
        soundvol.setMax(100);
        soundvol.setValue(50);
        soundvol.setShowTickLabels(true);
        soundvol.setShowTickMarks(true);
        soundvol.setMajorTickUnit(50);
        soundvol.setMinorTickCount(5);
        soundvol.setBlockIncrement(10);
        String initialValue1;
        String initialValue2;
        SettingSaves loader = new SettingSaves();
        try {
            Map<String, Object> data = loader.load("settings.gz");
            initialValue1 = data.get("multiplayer_server").toString();
            initialValue2 = data.get("multiplayer_name").toString();
            fpsBox.getSelectionModel().select(data.get("fps").toString());
            resBox.getSelectionModel().select(data.get("resstring").toString());
            musicvol.setValue(((double) data.get("musicvol")));
            soundvol.setValue(((double) data.get("soundvol")));
        } catch (FileNotFoundException e) {
            Popup.popup("Couldn't find settings file!", "Error Message");
            initialValue1 = "schaepers.it";
            initialValue2 = "Worms-player";
            fpsBox.getSelectionModel().select("20");
            resBox.getSelectionModel().select("900x600");
            musicvol.setValue(50);
            soundvol.setValue(50);
        } catch (NullPointerException e) {
            Popup.popup("Missing setting!", "Error Message");
            initialValue1 = "schaepers.it";
            initialValue2 = "Worms-player";
            fpsBox.getSelectionModel().select("20");
            resBox.getSelectionModel().select("900x600");
        }
        //so that music volume changes are effective immediately
        musicvol.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.mvol=(float)musicvol.getValue()/100;
                Main.music.setGain(Main.mvol);
            }
        });
        musicvol.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.mvol=(float)musicvol.getValue()/100;
                Main.music.setGain(Main.mvol);
            }
        });

        final TextField serverField = new TextField(initialValue1);
        final DisallowedCharTextField nameField = new DisallowedCharTextField(' ', initialValue2);

        // Configure each object
        sceneTitle.setFont(new Font(20));

        // Add the objects
        this.main.grid.add(sceneTitle, 0, 0, 2, 1);
        this.main.grid.add(title1, 1, 2);
        this.main.grid.add(serverField, 2, 2);
        this.main.grid.add(title2, 1, 4);
        this.main.grid.add(nameField, 2, 4);
        this.main.grid.add(title3, 1, 6);
        this.main.grid.add(fpsBox, 2, 6);
        this.main.grid.add(title4, 1, 8);
        this.main.grid.add(resBox, 2, 8);
        this.main.grid.add(title5, 1, 10);
        this.main.grid.add(musicvol, 2, 10);
        this.main.grid.add(title6, 1, 12);
        this.main.grid.add(soundvol, 2, 12);
        this.main.grid.add(returnButton, 1, 14);

        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SettingSaves saver = new SettingSaves();
                Map<String, Object> data = new HashMap<>();
                data.put("multiplayer_server", serverField.getText());
                data.put("multiplayer_name", nameField.getText());
                data.put("fps", fpsBox.getSelectionModel().getSelectedItem());
                data.put("resstring", resBox.getSelectionModel().getSelectedItem());
                if (data.get("resstring").toString().equals("600x400")) {
                    data.put("res", 1d);
                    Main.sizeMultiplier = 1;
                } else if (data.get("resstring").toString().equals("900x600")) {
                    data.put("res", 1.5d);
                    Main.sizeMultiplier = 1.5;
                } else if (data.get("resstring").toString().equals("1200x800")) {
                    data.put("res", 2d);
                    Main.sizeMultiplier = 2;
                } else if (data.get("resstring").toString().equals("1500x1000")) {
                    data.put("res", 2.5d);
                    Main.sizeMultiplier = 2.5;
                }
                data.put("musicvol", musicvol.getValue());
                data.put("soundvol", soundvol.getValue());
                saver.save(data, "settings.gz");
                // resize window
                main.primaryStage.setHeight(Terrain.getHeightInPixel()+30);
                main.primaryStage.setWidth(Terrain.getWidthInPixel()+10);
                addMainButtons();
            }
        });
    }

    /**
     * Add player selection buttons
     */
    private void addPlayerButtons() {
        // Clean up
        this.main.grid.getChildren().clear();
        Integer currentPlayer = this.main.players.size()+1;

        // Create buttons and other objects
        Text sceneTitle = new Text( "Player " + currentPlayer.toString());
        Text title1 = new Text("Name");
        Text title2 = new Text("Color");
        Button backButton = new Button("Back");

        final ComboBox<String> colorSelection = new ComboBox<>();
        colorSelection.getItems().addAll(this.main.availableColors);
        colorSelection.setValue(this.main.availableColors.get(0));
        final CheckBox weaponBox1 = new CheckBox("Atomic Bomb");
        final CheckBox weaponBox2 = new CheckBox("Grenade");
        final CheckBox weaponBox3 = new CheckBox("Bazooka");
        final TextField nameField = new TextField();
        nameField.setMaxSize(100, 20);

        // Configure each object
        sceneTitle.setFont(new Font(20));

        // Add the objects
        this.main.grid.add(sceneTitle, 0, 0, 2, 1);
        this.main.grid.add(title1, 1, 2);
        this.main.grid.add(nameField, 2, 2);
        this.main.grid.add(title2, 1, 4);
        this.main.grid.add(colorSelection, 2, 4);
        this.main.grid.add(weaponBox1, 1, 6);
        this.main.grid.add(weaponBox2, 2, 6);
        this.main.grid.add(weaponBox3, 3, 6);
        this.main.grid.add(backButton, 1, 8);

        if (this.main.players.size() == 0) {
            Button startButton = new Button("Load");
            this.main.grid.add(startButton, 3, 8);

            startButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    //Show FileSelection for Savegame
                    FileChooser fileChooser = new FileChooser();
                    //fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Maps", "gz"));
                    File gameSave = fileChooser.showOpenDialog(main.primaryStage);

                    if (gameSave != null) {
                        main.field = new Canvas(Terrain.getWidthInPixel(), Terrain.getHeightInPixel());
                        main.grid.getChildren().clear();
                        main.grid.add(main.field, 0, 0);
                        GameSaves loader = new GameSaves();

                        try {
                            main.game = loader.load(gameSave.toString());
                        } catch (FileNotFoundException ex) {
                            //
                        }
                        main.initializeHandlers();
                        main.game.gc = main.field.getGraphicsContext2D();
                        main.game.loaded = true;
                        Game.online = false;
                        main.game.startGameplay();
                    }
                }
            });
        } else {
            Button startButton = new Button("Start");
            this.main.grid.add(startButton, 3, 8);

            startButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    // Don't start without a weapon
                    if (!(
                            weaponBox1.selectedProperty().getValue() ||
                                    weaponBox2.selectedProperty().getValue() ||
                                    weaponBox3.selectedProperty().getValue()
                    )) {
                        return;
                    }
                    Map<String, Boolean> weaponMap = new HashMap<>();
                    weaponMap.put("AtomicBomb", weaponBox1.selectedProperty().getValue());
                    weaponMap.put("Grenade", weaponBox2.selectedProperty().getValue());
                    weaponMap.put("Bazooka", weaponBox3.selectedProperty().getValue());

                    main.players.add(parsePlayerSelection(weaponMap, colorSelection.getValue(), nameField.getText()));

                    addMapSelectionButtons();
                }
            });
        }

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
            }
        });

        if (this.main.players.size() <= 2) {
            Button addPlayerButton = new Button("Another Player");

            this.main.grid.add(addPlayerButton, 2, 8);

            addPlayerButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    // Don't start without a weapon
                    if (!(
                            weaponBox1.selectedProperty().getValue() ||
                                    weaponBox2.selectedProperty().getValue() ||
                                    weaponBox3.selectedProperty().getValue()
                    )) {
                        return;
                    }
                    Map<String, Boolean> weaponMap = new HashMap<>();
                    weaponMap.put("AtomicBomb", weaponBox1.selectedProperty().getValue());
                    weaponMap.put("Grenade", weaponBox2.selectedProperty().getValue());
                    weaponMap.put("Bazooka", weaponBox3.selectedProperty().getValue());

                    main.players.add(parsePlayerSelection(weaponMap, colorSelection.getValue(), nameField.getText()));
                    addPlayerButtons();
                }
            });
        }
    }

    /**
     * Add map selection buttons
     */
    private void addMapSelectionButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text sceneTitle = new Text("Select a map");
        Button backButton = new Button("Back");
        Button startButton = new Button("Start");

        final ComboBox<String> mapSelection = new ComboBox<>();
        mapSelection.getItems().addAll("Map1", "Map2", "Map3", "Map4", "Mountain", "Plains", "Castle","Random");
        mapSelection.setValue("Map1");
        String[] localMaps = Level.loadLocalLevels();
        mapSelection.getItems().addAll(localMaps);

        // Configure each object
        sceneTitle.setFont(new Font(20));

        // Add the objects
        this.main.grid.add(sceneTitle, 0, 0, 2, 1);
        this.main.grid.add(mapSelection, 0, 4);
        this.main.grid.add(backButton, 0, 8);
        this.main.grid.add(startButton, 1, 8);

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
            }
        });

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Main.music.stop();
                main.field = new Canvas(Terrain.getWidthInPixel(), Terrain.getHeightInPixel());
                main.grid.getChildren().clear();
                main.grid.add(main.field, 0, 0);
                Game.online = false;
                main.game = new Game(main.players);
                main.game.loadLevel(mapSelection.getValue());
                main.initializeHandlers();
                main.game.gc = main.field.getGraphicsContext2D();
                main.game.startGameplay();
            }
        });
    }

    /**
     * Creates from several objects a player
     * @param weaponMap Weapons for the player
     * @param colorName Color for the player
     * @param playerName Name for the player
     * @return Created player
     */
    private Player parsePlayerSelection(Map<String, Boolean> weaponMap, String colorName, String playerName) {
        ArrayList<AbstractWeapon> weapons = new ArrayList<>();
        if (weaponMap.get("AtomicBomb")) {
            weapons.add(new AtomicBomb());
        }
        if (weaponMap.get("Grenade")) {
            weapons.add(new Grenade());
        }
        if (weaponMap.get("Bazooka")) {
            weapons.add(new Bazooka());
        }

        ArrayList<Worm> wormsList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            wormsList.add(new Worm(weapons));
        }
        Player tmpPlayer = new Player(wormsList);
        tmpPlayer.color = Player.deseserializeColor(colorName);
        this.main.availableColors.remove(this.main.availableColors.indexOf(colorName));
        tmpPlayer.name = playerName;
        return tmpPlayer;
    }
}
