package GamePackage;

import java.awt.*;

public class Constants {

    public static final int GAME_WIDTH = 784;
    public static final int GAME_HEIGHT = 784;
    public static final Dimension DEFAULT_DIMENSION = new Dimension(GAME_WIDTH,GAME_HEIGHT);

    public static final int HALF_WIDTH = GAME_WIDTH/2;
    public static final int HALF_HEIGHT = GAME_HEIGHT/2;
    public static final int QUARTER_HEIGHT = GAME_HEIGHT/4;
    public final static int EIGHTH_HEIGHT = GAME_HEIGHT/8;
    public final static int SIXTEENTH_HEIGHT = GAME_HEIGHT/16;


    // sleep time between two frames
    static final int DELAY = 20;  // time between frames in milliseconds
    public static final double DT = DELAY / 1000.0;  // DELAY in seconds


    public static final int UP_INT = 0;
    public static final int RIGHT_INT = 1;
    public static final int DOWN_INT = 2;
    public static final int LEFT_INT = 3;
    public static final int MID_INT = 5;
}
