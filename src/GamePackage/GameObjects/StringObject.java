package GamePackage.GameObjects;

import utilities.Vector2D;

import java.awt.*;

import static GamePackage.Constants.DT;

public class StringObject extends GameObject {

    private String thisString;

    private int alignment;

    private Font theFont;

    public static final int LEFT_ALIGN = 0;
    public static final int RIGHT_ALIGN = 1;
    public static final int MIDDLE_ALIGN = 2;

    boolean scrolling;

    private int currentPredefinedColour;
    public static final int WHITE_NUM = 0;
    public static final int RED_NUM = 1;
    public static final int ORANGE_NUM = 2;
    public static final int YELLOW_NUM = 3;
    public static final int GREEN_NUM = 4;


    private Rectangle areaRectangle;

    //le ebic font has arrived no bamboozle
    public static final Font SANS_12 = new Font("Comic Sans MS", Font.PLAIN, 12);
    public static final Font SANS_20 = new Font("Comic Sans MS", Font.PLAIN, 20);
    public static final Font SANS_25 = new Font("Comic Sans MS", Font.PLAIN, 25);
    public static final Font SANS_30 = new Font("Comic Sans MS", Font.PLAIN, 30);
    public static final Font SANS_40 = new Font("Comic sans MS", Font.PLAIN,40);
    public static final Font SANS_50 = new Font("Comic sans MS", Font.PLAIN,50);
    public static final Font SANS_60 = new Font("Comic sans MS", Font.PLAIN,60);
    public static final Font SANS_70 = new Font("Comic sans MS", Font.PLAIN,70);
    public static final Font SANS_80 = new Font("Comic sans MS", Font.PLAIN,80);

    public StringObject(Vector2D p, Vector2D v, String s, int a, Font f){
        this(p,v,s,a);
        theFont = f;
    }


    public StringObject(Vector2D p, Vector2D v, String s, int a){
        this(p,v,a);
        setText(s);
    }



    public StringObject(Vector2D p, Vector2D v, String s){
        this(p,v);
        setText(s);
    }

    public StringObject(Vector2D p, Vector2D v, int a){
        this(p,v);
        alignment = a;
    }

    public StringObject(Vector2D p, Vector2D v){
        super(p,v);
        width = 0;
        height = 0;
        alignment = 0;
        thisString = "";

        objectColour = Color.WHITE;
        setPredefinedColour(WHITE_NUM);
        theFont = SANS_20;
        areaRectangle = new Rectangle();
        scrolling = false;
    }

    //scrolling constructor with defined font
    public StringObject(Vector2D p, double speed, String s, int a, Font f){
        this(p,Vector2D.polar(UP_RADIANS,speed),s,a,f);
        scrolling = true;
    }

    //scrolling constructor with default font
    public StringObject(Vector2D p, double speed, String s, int a){
        this(p,Vector2D.polar(UP_RADIANS,speed),s,a);
        scrolling = true;
    }

    @Override
    public StringObject revive(Vector2D p, Vector2D v) {
        super.revive(p,v);
        setPredefinedColour(WHITE_NUM);
        return this;
    }

    public StringObject revive(){ return revive(position,velocity); }

    public StringObject kill(){ alive = false; return this; }

    public String getString(){ return thisString; }

    public StringObject revive(String s){
        revive();
        return setText(s);
    }

    public boolean isClicked(Point p){
        return (areaRectangle.contains(p));
    }

    void setTheFont(Font f){
        theFont = f;
    }


    //version of revive() encapsulating setTextAndPredefinedColour()
    public StringObject revive(String newText, int definedColourValue){
        revive();
        setTextAndPredefinedColour(newText, definedColourValue);
        return this;
    }




    public void setPredefinedColour(int definedColourValue){
        currentPredefinedColour = definedColourValue;
        setColourToPredefinedColour();
    }

    public void cycleColours(){
        //used for cycling through the predefined colours
        currentPredefinedColour++;
        currentPredefinedColour = currentPredefinedColour % 5;
        //ensures it's in range 0-4 (within valid predefined colours)
        setColourToPredefinedColour();
    }

    private void setColourToPredefinedColour(){
        switch (currentPredefinedColour){
            case RED_NUM:
                objectColour = TRAFFIC_RED;
                break;
            case ORANGE_NUM:
                objectColour = TRAFFIC_ORANGE;
                break;
            case YELLOW_NUM:
                objectColour = TRAFFIC_YELLOW;
                break;
            case GREEN_NUM:
                objectColour = TRAFFIC_GREEN;
                break;
            default:
                //if predefined value is not recognized, it's set to the white value
                currentPredefinedColour = WHITE_NUM;
                //no break here, so it will then set the object colour to white
            case WHITE_NUM:
                objectColour = Color.WHITE;
                break;
        }
    }

    public void setTextAndPredefinedColour(String newText, int definedColourValue){
        setText(newText);
        setPredefinedColour(definedColourValue);
    }

    public void setColour(Color c){ objectColour = c;}


    @Override
    public void renderObject(Graphics2D g) {
        if (alive) {
            Font tempFont = g.getFont();
            g.setFont(theFont);
            g.setColor(Color.black);
            FontMetrics metrics = g.getFontMetrics(g.getFont());
            int w = metrics.stringWidth(thisString);
            int h = metrics.getHeight();
            int heightOffset = (h/4);
            int widthOffset;
            switch (alignment){
                default:
                    widthOffset = alignment;
                    break;
                case 0:
                    widthOffset = 0;
                    break;
                case 1:
                    widthOffset = -w;
                    break;
                case 2:
                    widthOffset = -(w/2);
                    break;
            }
            g.drawString(thisString,widthOffset+1,heightOffset+1);
            g.drawString(thisString,widthOffset-1,heightOffset+1);
            g.drawString(thisString,widthOffset-1,heightOffset-1);
            g.drawString(thisString,widthOffset+1,heightOffset-1);
            g.setColor(objectColour);
            g.drawString(thisString,widthOffset,heightOffset);
            g.setFont(tempFont);
            areaRectangle = new Rectangle((int)position.x - (w/2), (int)position.y - heightOffset,w,h);
        }
    }

    public StringObject setText(String s){ thisString = s; return this;}

    public Rectangle getAreaRectangle(){ return areaRectangle;}



    @Override
    void amIAlive(){
        if (scrolling) {
            //only dies if off-screen if it's scrolling text
            if (position.y < 0) {
                this.alive = false;
            }
        }
    }

    @Override
    void individualUpdate() {
        position.addScaled(velocity, DT);
    }


}