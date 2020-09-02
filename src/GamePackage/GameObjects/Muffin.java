package GamePackage.GameObjects;

import utilities.ImageManager;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static GamePackage.Constants.RIGHT_INT;
import static GamePackage.Constants.UP_INT;

public class Muffin extends LaneTravellingObject {

    private final Vector2D direction;

    private final static double[][] SPAWN_SETTINGS = {
            {392, 296, UP_RADIANS, LEFT_RADIANS},
            {488, 392, RIGHT_RADIANS, UP_RADIANS},
            {392, 488, DOWN_RADIANS, RIGHT_RADIANS},
            {296, 392, LEFT_RADIANS, DOWN_RADIANS}
    };
    //x: laneNumber value
    //y: [0] x revive location, [1] y revive location, [2] travel direction [4] rotation direction

    public Muffin(){
        super();
        this.img = ImageManager.getImage("Muffin");
        direction = new Vector2D(1,0);
    }

    public Muffin revive(int laneNumber){
        super.revive(new Vector2D(SPAWN_SETTINGS[laneNumber][0],SPAWN_SETTINGS[laneNumber][1]),SPAWN_SETTINGS[laneNumber][2],laneNumber);
        this.direction.set(Vector2D.polar(SPAWN_SETTINGS[laneNumber][2],1));
        return this;
    }

    public boolean hit(CupcakeBot c){
        double cDist = c.getDistanceLeft();
        cDist = TOTAL_DISTANCE_TO_TRAVEL - cDist;
        if (this.getDistanceLeft() <= cDist){
            this.kill();
            return true;
        } else{
            return false;
        }
    }

    @Override
    void renderObject(Graphics2D g) {
        //AffineTransform unrotated = g.getTransform();

        g.rotate(SPAWN_SETTINGS[lane][3]);
        g.drawImage(
                img,
                -32,
                -32,
                64,
                64,
                null
        );
        //g.setTransform(unrotated);
    }
}
