package de.hhu.propra14.team101;

import de.hhu.propra14.team101.Savers.LevelSaves;
import de.hhu.propra14.team101.TerrainObjects.*;

import java.io.*;
import java.util.ArrayList;

/**
 * Used to create levels without fiddling around with code
 */
public class LevelCreator {
    private static Terrain terrain;
    private static ArrayList<int[]> spawns = new ArrayList<>();

    private static int currentLine = 0;
    private static int currentChar = 0;

    public static void convert(String input, String output, String theme) {
        // Can't show any graphics
        Main.headless = true;

        if (!(theme.equals("normal") || theme.equals("horror") || theme.equals("oriental"))) {
            theme = "normal";
        }

        InputStream stream;
        Reader reader;
        try {
            stream = new FileInputStream(input);
        } catch (FileNotFoundException e) {
            System.out.println("File not found!");
            return;
        }

        reader = new InputStreamReader(stream);

        // Terrain will be created in here
        terrain = new Terrain();

        int character;

        try {
            while ((character = reader.read()) != -1) {
                parseChar((char) character);  // this method will do whatever you want
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Level level = new Level(terrain);
        // Add the spawnpoints now
        for (int[] coordinate : spawns) {
            level.addWormStartPosition(coordinate[0], coordinate[1]);
        }
        level.theme = theme;

        LevelSaves saver = new LevelSaves();
        saver.save(level, output);
    }

    private static void parseChar(char character) {
        switch (character) {
            case ' ':
                // No need to add anything (air == nil)
                break;
            case 'S':
                terrain.addTerrainObject(new SquareBuildingBlock(currentChar, currentLine));
                break;
            case 'E':
                terrain.addTerrainObject(new ExplosiveBuildingBlock(currentChar, currentLine));
                break;
            case 'T':
                terrain.addTerrainObject(new TriangleBuildingBlock(currentChar, currentLine, true));
                break;
            case 't':
                terrain.addTerrainObject(new TriangleBuildingBlock(currentChar, currentLine, false));
                break;
            case 'B':
                terrain.addTerrainObject(new SandBuildingBlock(currentChar, currentLine));
                break;
            case 'O':
                terrain.addTerrainObject(new Obstacle(currentChar, currentLine));
                break;
            case 'j':
                terrain.addTerrainObject(new Spring(currentChar, currentLine));
                break;
            case 's':
                terrain.addTerrainObject(new Shoe(currentChar, currentLine));
                break;
            case 'e':
                terrain.addTerrainObject(new Elixir(currentChar, currentLine));
                break;
            case 'W':
                spawns.add(new int[]{currentChar, currentLine});
                break;
            case '\n':
                currentLine++;
                currentChar = 0;
                break;
            default:
                System.out.println("Unknown char '" + character + "'");
                break;
        }

        if (character != '\n') {
            currentChar++;
        }
    }

    // TODO put this help message somewhere useful
    /*private static void helpMessage() {
        System.out.println("Creates a level from a supplied text file.");
        System.out.println("Supplied text file must have 60 columns and 40 rows");
        System.out.println("each containing one of these characters:");
        System.out.println(" (Space): for air");
        System.out.println("S: For a square block");
        System.out.println("E: For an exploding block");
        System.out.println("T: For a sloped left triangle block");
        System.out.println("t: For a sloped right triangle block");
        System.out.println("B: For a sand block (Beach)");
        System.out.println("O: For an obstacle");
        System.out.println("W: For a worm-spawn");
        System.out.println("j: For a spring item (Jump)");
        System.out.println("s: For a shoe item");
        System.out.println("e: For an elixir item");
        System.out.println("Usage:");
        System.out.println("java -jar LevelCreator.jar textfile [theme]");
        System.out.println("Textfile is the path to the supplied textfile described above");
        System.out.println("Theme is optional and has to be one of: normal, horror, oriental");
        System.out.println("Created map will be written to Map.gz");
    }*/
}
