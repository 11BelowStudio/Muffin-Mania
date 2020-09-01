package GamePackage.GameObjects;

import utilities.ImageManager;
import utilities.Vector2D;

import java.awt.*;

public class CupcakeSpawnerObject extends GameObject {

    private static double[][] SPAWNER_POSITIONS = {
            {392, 40},
            {744, 392},
            {392, 744},
            {40, 392}
    };

    private static int[][] SPAWNER_SPRITESHEET_Y_COORDS = {
            {0,80},
            {80,160},
            {160,240},
            {240,320}
    };

    private int y1;
    private int y2;


    public CupcakeSpawnerObject(int pos){
        super(new Vector2D(SPAWNER_POSITIONS[pos][0],SPAWNER_POSITIONS[pos][1]), new Vector2D());
        this.img = ImageManager.getImage("CupcakeSpawners");
        y1 = SPAWNER_SPRITESHEET_Y_COORDS[pos][0];
        y2 = SPAWNER_SPRITESHEET_Y_COORDS[pos][1];
    }

    @Override
    void individualUpdate() {

    }

    @Override
    void renderObject(Graphics2D g) {
        g.drawImage(
                img,
                -40,
                -40,
                40,
                40,
                0,
                y1,
                80,
                y2,
                null
        );
    }
}
