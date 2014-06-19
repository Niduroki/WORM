package de.hhu.propra14.team101;

import de.hhu.propra14.team101.TerrainObjects.AbstractTerrainObject;
import de.hhu.propra14.team101.TerrainObjects.ExplosiveBuildingBlock;
import de.hhu.propra14.team101.TerrainObjects.SquareBuildingBlock;

import java.util.ArrayList;
import java.util.Random;

/**
 * Creates RandomLevel
 */
public class RandomLevel {
    private int height = Terrain.height - 10;
    private int width = 0;
    private Random random = new Random();
    private ArrayList<int[]> wormSpawns = new ArrayList<>();


    public Level generate() {
        Terrain terrain = new Terrain();
        /*While not done because of Terrain*/
        while (width < Terrain.width) {

            // Fill a row
            for (int i = Terrain.height - 1; i > height; i--) {
                AbstractTerrainObject toAdd;
          /*If its a 10 the block is explosive*/
                if (random.nextInt(10) == 9) {
                    toAdd = new ExplosiveBuildingBlock(width, i);

                } else {
                    toAdd = new SquareBuildingBlock(width, i);
                }
                terrain.addTerrainObject(toAdd);
            }
       /* If Random is a 5 */
            if (random.nextInt(6) == 5) {
                wormSpawns.add(new int[]{width, height});
            }
            int nextAction = random.nextInt(5);
            /* If its a 5 or 1 we go down */
            if (nextAction == 0 && height < Terrain.height - 1) {
                height++;
            } else if (nextAction == 4 && height > 0) {
                height--;
            }
            /* Else we keep the height */
            width++;
        }
        Level level = new Level (terrain);
        for (int[] spawn : wormSpawns) {
            level.addWormStartPosition(spawn[0], spawn[1]);
        }
        int theme = random.nextInt(3);
        switch (theme) {
            case 0:
                level.theme = "normal";
                break;
            case 1:
                level.theme = "horror";
                break;
            case 2:
                level.theme = "oriental";
                break;
        }
        return level;
    }
}
