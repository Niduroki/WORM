package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Networking.Exceptions.TimeoutException;
import de.hhu.propra14.team101.Networking.NetworkClient;
import de.hhu.propra14.team101.Savers.SettingSaves;
import de.hhu.propra14.team101.Weapons.AbstractWeapon;
import de.hhu.propra14.team101.Weapons.AtomicBomb;
import de.hhu.propra14.team101.Weapons.Bazooka;
import de.hhu.propra14.team101.Weapons.Grenade;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GUI {
    private Main main;

    public GUI (Main main) {
        this.main = main;
    }

    public void addMainButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 0, 20, 20));

        Text scenetitle = new Text("Name");
        Button startbtn = new Button("Start");
        Button multibtn = new Button("Multiplayer");
        Button optionsbtn = new Button("Options");
        Button exitbtn = new Button("Exit");


        startbtn.setMaxWidth(Double.MAX_VALUE);
        multibtn.setMaxWidth(Double.MAX_VALUE);
        optionsbtn.setMaxWidth(Double.MAX_VALUE);
        exitbtn.setMaxWidth(Double.MAX_VALUE);

        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(0, 20, 10, 20));
        vbButtons.getChildren().addAll(startbtn, multibtn, optionsbtn, exitbtn);


        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(scenetitle, 0, 0, 2, 1);
        this.main.grid.add(startbtn, 1, 2);
        this.main.grid.add(multibtn, 1, 4);
        this.main.grid.add(optionsbtn, 1, 6);
        this.main.grid.add(exitbtn, 1, 8);

        startbtn.setOnAction(new EventHandler<ActionEvent>() {
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

        optionsbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addOptionsButtons();
            }
        });

        multibtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main.client = new NetworkClient(main);
                main.lobby.addMpButtons();
            }
        });

        exitbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                main.primaryStage.close();
            }
        });
    }

    public void addOptionsButtons() {
        // Clean up
        this.main.grid.getChildren().clear();

        // Create buttons and other objects
        Text scenetitle = new Text("Options");
        Text title1 = new Text("Multiplayer Server");
        Text title2 = new Text("Multiplayer Name");
        Text title3 = new Text("Frames per second");
        Button returnbtn = new Button("Save & Return");


        final ComboBox<String> fpsBox = new ComboBox<>();
        fpsBox.getItems().addAll("15", "20", "30", "45", "60");
        String initialValue1;
        String initialValue2;
        SettingSaves loader = new SettingSaves();
        try {
            Map<String, Object> data = (Map<String, Object>) loader.load("settings.gz");
            initialValue1 = data.get("multiplayer_server").toString();
            initialValue2 = data.get("multiplayer_name").toString();
            fpsBox.getSelectionModel().select(data.get("fps").toString());
        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find settings file!");
            initialValue1 = "schaepers.it";
            initialValue2 = "Worms-player";
            fpsBox.getSelectionModel().select("20");
        } catch (NullPointerException e) {
            System.out.println("Missing setting!");
            initialValue1 = "schaepers.it";
            initialValue2 = "Worms-player";
            fpsBox.getSelectionModel().select("20");
        }

        final TextField serverField = new TextField(initialValue1);
        final TextField nameField = new TextField(initialValue2);

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(scenetitle, 0, 0, 2, 1);
        this.main.grid.add(title1, 1, 2);
        this.main.grid.add(serverField, 2, 2);
        this.main.grid.add(title2, 1, 4);
        this.main.grid.add(nameField, 2, 4);
        this.main.grid.add(title3, 1, 6);
        this.main.grid.add(fpsBox, 2, 6);
        this.main.grid.add(returnbtn, 1, 8);

        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                SettingSaves saver = new SettingSaves();
                Map<String, Object> data = new HashMap<>();
                data.put("multiplayer_server", serverField.getText());
                data.put("multiplayer_name", nameField.getText());
                data.put("fps", fpsBox.getSelectionModel().getSelectedItem());
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
        Text scenetitle = new Text( "Player " + currentPlayer.toString());
        Text title1 = new Text("Name");
        Text title2 = new Text("Color");
        Button backbtn = new Button("Back");

        final ComboBox<String> colorSelection = new ComboBox<>();
        colorSelection.getItems().addAll(this.main.availableColors);
        colorSelection.setValue(this.main.availableColors.get(0));
        final CheckBox weaponBox1 = new CheckBox("Atomic Bomb");
        final CheckBox weaponBox2 = new CheckBox("Grenade");
        final CheckBox weaponBox3 = new CheckBox("Bazooka");
        final TextField nameField = new TextField();
        nameField.setMaxSize(100, 20);

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(scenetitle, 0, 0, 2, 1);
        this.main.grid.add(title1, 1, 2);
        this.main.grid.add(nameField, 2, 2);
        this.main.grid.add(title2, 1, 4);
        this.main.grid.add(colorSelection, 2, 4);
        this.main.grid.add(weaponBox1, 1, 6);
        this.main.grid.add(weaponBox2, 2, 6);
        this.main.grid.add(weaponBox3, 3, 6);
        this.main.grid.add(backbtn, 1, 8);

        if (this.main.players.size() != 0) {
            Button startbtn = new Button("Start");
            this.main.grid.add(startbtn, 3, 8);

            startbtn.setOnAction(new EventHandler<ActionEvent>() {
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

        backbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
            }
        });

        if (this.main.players.size() <= 2) {
            Button addplayerbtn = new Button("Another Player");

            this.main.grid.add(addplayerbtn, 2, 8);

            addplayerbtn.setOnAction(new EventHandler<ActionEvent>() {
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
        Text scenetitle = new Text("Select a map");
        Button backbtn = new Button("Back");
        Button startbtn = new Button("Start");

        final ComboBox<String> mapSelection = new ComboBox<>();
        mapSelection.getItems().addAll("Map 1", "Map 2", "Map 3");
        mapSelection.setValue("Map 1");

        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(scenetitle, 0, 0, 2, 1);
        this.main.grid.add(mapSelection, 0, 4);
        this.main.grid.add(backbtn, 0, 8);
        this.main.grid.add(startbtn, 1, 8);

        backbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
            }
        });

        startbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                main.field = new Canvas(600, 400);
                main.grid.getChildren().clear();
                main.grid.add(main.field, 0, 0);
                int levelNumber = Integer.parseInt(mapSelection.getValue().split(" ")[1])-1;
                main.startGameplay(levelNumber, main.field.getGraphicsContext2D());
            }
        });
    }

    public void winScreen(String winner) {
        // Clean up
        main.grid.getChildren().clear();

        // Create buttons and other objects
        BorderPane border = new BorderPane();
        border.setPadding(new Insets(20, 0, 20, 20));

        Text scenetitle = new Text("Player "+winner+" won!");
        Button returnbtn = new Button("Return");

        returnbtn.setMaxWidth(Double.MAX_VALUE);

        VBox vbButtons = new VBox();
        vbButtons.setSpacing(10);
        vbButtons.setPadding(new Insets(0, 20, 10, 20));
        vbButtons.getChildren().add(returnbtn);


        // Configure each object
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // Add the objects
        this.main.grid.add(scenetitle, 0, 0, 2, 1);
        this.main.grid.add(returnbtn, 1, 2);

        returnbtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                addMainButtons();
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
