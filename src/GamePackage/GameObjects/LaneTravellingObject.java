package GamePackage.GameObjects;

import utilities.Vector2D;

import java.awt.*;

import static GamePackage.Constants.DT;

public abstract class LaneTravellingObject extends GameObject {

    final static double TOTAL_DISTANCE_TO_TRAVEL = 256;
    double distanceLeft;
    final static double TRAVEL_SPEED = 128;
    final static double PER_FRAME_DISTANCE = TRAVEL_SPEED*DT;
    int lane;

    public LaneTravellingObject() {
        super(new Vector2D(), new Vector2D());
    }

    LaneTravellingObject revive(Vector2D p, double angle, int laneNumber){
        super.revive(p,Vector2D.polar(angle,TRAVEL_SPEED));
        this.lane = laneNumber;
        this.distanceLeft = TOTAL_DISTANCE_TO_TRAVEL;
        return this;
    }

    void updateDistanceLeft(double distanceTravelledThisFrame){
        distanceLeft -= distanceTravelledThisFrame;
    }

    double getDistanceLeft(){
        return distanceLeft;
    }

    int getLane(){
        return lane;
    }

    boolean checkIfInSameLane(LaneTravellingObject other){
        return (this.lane == other.getLane());
    }

    boolean hasReachedDestination(){
        if (distanceLeft <= 0){
            this.alive = false;
            return true;
        }
        return false;
    }

    void individualUpdate(){
        position.addScaled(velocity, DT);
        distanceLeft -= PER_FRAME_DISTANCE;
        //updateDistanceLeft();
    }
}
