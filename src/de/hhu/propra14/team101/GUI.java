package de.hhu.propra14.team101;

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

public class GUI {
    private Main main;
    protected String levelCreatorInputPath;
    protected String levelCreatoroutputPath;

    public GUI (Main main) {
        this.main = main;
    }

    public void addMainButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 0, 20, 20));

        Text sceneTitle = new Text("Name");
        Button startButton = new Button("Start");
        Button multiButton= new Button("Multiplayer");
        Button levelEditor=new Button ("Level Editor");
        Button optionsButton = new Button("Options");
        Button exitButton = new Button("Exit");


        startButton.setMaxWidth(Double.MAX_VALUE);
        multiButton.setMaxWidth(Double.MAX_VALUE);
        levelEditor.setMaxWidth(Double.MAX_VALUE);
        optionsButton.setMaxWidth(Double.MAX_VALUE);
        exitButton.setMaxWidth(Double.MAX_VALUE);

        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(0, 20, 10, 20));
        vbButtons.getChildren().addAll(startButton, multiButton, optionsButton, exitButton);


        // Configure each object
        sceneTitle.setFont(new Font(20));

        // Add the objects
        this.main.grid.add(sceneTitle, 0, 0, 2, 1);
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

    private void addEditorButtons() {
        this.main.grid.getChildren().clear();
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 0, 20, 20));

        Button inputButton = new Button("Select Input ...");
        Button outputButton = new Button("Select Output ...");
        Button convertEditor = new Button("Convert");
        Button returnButton = new Button("Return");

        final ComboBox<String> themeSelection = new ComboBox<>();
        themeSelection.getItems().addAll("Normal", "Horror", "Oriental");
        themeSelection.setValue("Normal");
        final String selectedTheme= themeSelection.getSelectionModel().getSelectedItem();

        inputButton.setMaxWidth(Double.MAX_VALUE);
        outputButton.setMaxWidth(Double.MAX_VALUE);
        convertEditor.setMaxWidth(Double.MAX_VALUE);
        returnButton.setMaxWidth(Double.MAX_VALUE);
        themeSelection.setMaxWidth(Double.MAX_VALUE);

        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(0, 20, 10, 20));
        vbButtons.getChildren().addAll(inputButton, outputButton, convertEditor, returnButton);

        this.main.grid.add(inputButton, 1, 2);
        this.main.grid.add(themeSelection, 1,6);
        this.main.grid.add(outputButton, 1, 4);
        this.main.grid.add(convertEditor, 1, 8);
        this.main.grid.add(returnButton, 1, 10);

        returnButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                addMainButtons();
            }
        });

        inputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                File inputFile = fileChooser.showOpenDialog(main.primaryStage);
                levelCreatorInputPath = inputFile.toString();
            }
        });


        outputButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                File outputFile = fileChooser.showSaveDialog(main.primaryStage);
                levelCreatoroutputPath = outputFile.toString();
            }
        });

        convertEditor.setOnAction(new EventHandler<ActionEvent>() {
            @Override
                public void handle(ActionEvent event) {
                LevelCreator.convert(levelCreatorInputPath,levelCreatoroutputPath,selectedTheme.toLowerCase());

            }
        });



    }

    public void addOptionsButtons() {
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
        resBox.getItems().addAll("300x200", "600x400", "900x600", "1200x800", "1500x1000");
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
            resBox.getSelectionModel().select(data.get("res").toString());
            musicvol.setValue(((double) data.get("musicvol")));
            soundvol.setValue(((double) data.get("soundvol")));
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find settings file!");
            initialValue1 = "schaepers.it";
            initialValue2 = "Worms-player";
            fpsBox.getSelectionModel().select("20");
            resBox.getSelectionModel().select("600x400");
            musicvol.setValue(50);
            soundvol.setValue(50);
        } catch (NullPointerException e) {
            System.out.println("Missing setting!");
            initialValue1 = "schaepers.it";
            initialValue2 = "Worms-player";
            fpsBox.getSelectionModel().select("20");
            resBox.getSelectionModel().select("600x400");
        }
        musicvol.setOnMouseMoved(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Main.mvol=(float)musicvol.getValue()/100;
                Main.music.setGain(Main.mvol);
            }
        });

        final TextField serverField = new TextField(initialValue1);
        final TextField nameField = new TextField(initialValue2);

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
                data.put("res", resBox.getSelectionModel().getSelectedItem());
                data.put("musicvol", musicvol.getValue());
                data.put("soundvol", soundvol.getValue());
                saver.save(data, "settings.gz");
                addMainButtons();
            }
        });
    }

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
                    main.field = new Canvas(Terrain.getWidthInPixel(), Terrain.getHeightInPixel());
                    main.grid.getChildren().clear();
                    main.grid.add(main.field, 0, 0);
                    GameSaves loader = new GameSaves();
                    //Show FileSelection for Savegame
                    FileChooser fileChooser = new FileChooser();
                    //fileChooser.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Maps", "gz"));
                    File gameSave = fileChooser.showOpenDialog(main.primaryStage);
                    System.out.println(gameSave.toString());
                    try {
                        main.game = loader.load(gameSave.toString());
                    } catch (FileNotFoundException ex) {
                        //
                    }
                    main.initializeHandlers();
                    main.game.gc = main.field.getGraphicsContext2D();
                    main.game.loaded = true;
                    main.game.startGameplay();
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

    private void addMapSelectionButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text sceneTitle = new Text("Select a map");
        Button backButton = new Button("Back");
        Button startButton = new Button("Start");

        final ComboBox<String> mapSelection = new ComboBox<>();
        mapSelection.getItems().addAll("Map1", "Map2", "Map3", "Map4", "Mountain", "Plains", "Castle");
        mapSelection.setValue("Map1");

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
                main.music.stop();
                main.field = new Canvas(Terrain.getWidthInPixel(), Terrain.getHeightInPixel());
                main.grid.getChildren().clear();
                main.grid.add(main.field, 0, 0);
                main.game = new Game(main.players);
                main.game.loadLevel(mapSelection.getValue());
                main.initializeHandlers();
                main.game.gc = main.field.getGraphicsContext2D();
                main.game.startGameplay();
            }
        });
    }

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
        Player tmpPlayer = new Player(wormsList, "Local");
        tmpPlayer.color = Player.deseserializeColor(colorName);
        this.main.availableColors.remove(this.main.availableColors.indexOf(colorName));
        tmpPlayer.name = playerName;
        return tmpPlayer;
    }
}
