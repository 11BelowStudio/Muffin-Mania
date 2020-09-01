package GamePackage.GameObjects;

import utilities.ImageManager;
import utilities.Vector2D;

import java.awt.*;

public class CupcakeBot extends LaneTravellingObject {

    private final static double[][] SPAWN_SETTINGS = {
            {392, 40, DOWN_RADIANS},
            {744, 392, LEFT_RADIANS},
            {392, 744, UP_RADIANS},
            {40, 392, RIGHT_RADIANS}
    };
    //x: laneNumber value
    //y: [0] x revive location, [1] y revive location, [2] travel direction

    private final static int[][][] CUPCAKE_SPRITESHEET_COORDS = {
            {{0,0,64,64},{64,0,128,64}},
            {{0,64,64,128},{64,64,128,128}},
            {{0,128,64,192},{64,128,128,192}},
            {{0,192,64,256},{64,192,128,256}}
    };
    //X: laneNumber
    //Y: which frame (0: normal, 1: alt frame)
    //Z: [0]: sx1 [1]: sy1 [2]: sx2 [3]: sy2

    private static final int ANIMATION_TIMER_LENGTH = 5;
    private int animationTimer;
    private int animationFrame;

    public CupcakeBot(){
        super();
        this.img = ImageManager.getImage("CupcakeBot");
    }

    public CupcakeBot revive(int laneNumber){
        super.revive(new Vector2D(SPAWN_SETTINGS[laneNumber][0],SPAWN_SETTINGS[laneNumber][1]),SPAWN_SETTINGS[laneNumber][2],laneNumber);
        animationFrame = 0;
        animationTimer = ANIMATION_TIMER_LENGTH;
        return this;
    }

    @Override
    void individualUpdate(){
        super.individualUpdate();
        if (animationTimer > 0){
            animationTimer--;
        } else{
            animationFrame++;
            animationFrame %= 2; //sets it to 0 if it goes up to 2
            animationTimer = ANIMATION_TIMER_LENGTH;
        }
    }


    @Override
    void renderObject(Graphics2D g) {
        g.drawImage(
                img,
                -32,
                -32,
                32,
                32,
                CUPCAKE_SPRITESHEET_COORDS[lane][animationFrame][0],
                CUPCAKE_SPRITESHEET_COORDS[lane][animationFrame][1],
                CUPCAKE_SPRITESHEET_COORDS[lane][animationFrame][2],
                CUPCAKE_SPRITESHEET_COORDS[lane][animationFrame][3],
                null
        );
    }


}
