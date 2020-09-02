package GamePackage.GameObjects;

import utilities.ImageManager;
import utilities.Vector2D;

import java.awt.*;
import java.awt.geom.AffineTransform;

import static GamePackage.Constants.*;

public class BackgroundMuffinObject extends GameObject {

    private static final double HALF_PI = Math.PI/2;

    private boolean hasBeenInBounds;

    private double rotationAngle;

    public BackgroundMuffinObject() {
        super(new Vector2D(), new Vector2D());
        this.img = ImageManager.getImage("Muffin");
    }

    @Override
    void individualUpdate() {
        position.addScaled(velocity, DT);
        if (!hasBeenInBounds){
            hasBeenInBounds = isInBounds();
        }
    }

    @Override
    void amIAlive(){
        if (hasBeenInBounds){
            alive = isInBounds();
        }
    }

    @Override
    void renderObject(Graphics2D g) {

        g.rotate(rotationAngle);
        g.drawImage(
                img,
                -32,
                -32,
                64,
                64,
                null
        );
    }



    public BackgroundMuffinObject revive(){
        Vector2D pos = Vector2D.randomVectorFromOrigin(MID_VECTOR,1024,512);
        Vector2D vel = Vector2D.polar(pos.getAngleTo(MID_VECTOR),(Math.random()*768)+256);
        super.revive(pos,vel);
        rotationAngle = velocity.angle() + HALF_PI;
        hasBeenInBounds = false;
        return this;
    }

    private boolean isInBounds(){
        return (position.x > -32 && position.x < (GAME_WIDTH+32) && position.y > -32 && position.y < GAME_WIDTH+32);
    }
}
