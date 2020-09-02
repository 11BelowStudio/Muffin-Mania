package GamePackage;

import GamePackage.GameObjects.BackgroundMuffinObject;
import GamePackage.GameObjects.GameObject;
import GamePackage.GameObjects.StringObject;
import utilities.TextAssetReader;
import utilities.HighScoreHandler;
import utilities.Vector2D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static GamePackage.Constants.*;

public class TitleScreen extends Model {

    private int titleScreenState;
    private static final int SETTING_UP_SCROLLING_TEXT_STATE = 0;
    private static final int SHOWING_SCROLLING_TEXT_STATE = 1;
    private static final int SHOWING_MENU_STATE = 2;
    private static final int START_GAME_STATE = 3;


    private static final int MUFFIN_CHANCES = 1024;
    private int muffinTimer;

    private final List<BackgroundMuffinObject> bgMuffins;
    private final List<BackgroundMuffinObject> bgAliveMuffins;

    private final Stack<BackgroundMuffinObject> bgMuffinStack;

    private final List<StringObject> hudObjects;

    private final List<StringObject> aliveHUD;

    private final List<StringObject> menuScreenStringObjects;
    private final StringObject titleText;
    private final StringObject subtitleText;
    private final StringObject play;
    private final StringObject showScores;
    private final StringObject showCredits;
    private final StringObject quitText;
    private final StringObject byMeText;

    private final ArrayList<StringObject> scrollingTextToAdd;

    private final static ArrayList<String> OPENING_TEXT = TextAssetReader.getOpeningText();

    private final static ArrayList<String> CREDITS_TEXT = TextAssetReader.getCreditsText();


    public TitleScreen(Controller ctrl, HighScoreHandler hs) {
        super(ctrl, hs);

        hudObjects = new ArrayList<>();
        aliveHUD = new ArrayList<>();

        //collection to hold menu screen stringobjects
        menuScreenStringObjects = new ArrayList<>();

        //declaring the stringobjects for the menu screen
        titleText = new StringObject(
                new Vector2D(HALF_WIDTH,QUARTER_HEIGHT),
                new Vector2D(),
                "Muffin Mania",
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_60
        );
        subtitleText = new StringObject(
                new Vector2D(HALF_WIDTH,QUARTER_HEIGHT + SIXTEENTH_HEIGHT),
                new Vector2D(),
                "Tactical Muffin Action",
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_40
        );
        play = new StringObject(
                new Vector2D(HALF_WIDTH,HALF_HEIGHT),
                new Vector2D(),
                "*Play*",
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_40
        );
        showScores = new StringObject(
                new Vector2D(HALF_WIDTH,HALF_HEIGHT + EIGHTH_HEIGHT),
                new Vector2D(),
                "*Show Scores*",
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_40
        );
        showCredits = new StringObject(
                new Vector2D(HALF_WIDTH,HALF_HEIGHT + QUARTER_HEIGHT),
                new Vector2D(),
                "*Show Credits*",
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_40
        );
        quitText = new StringObject(
                new Vector2D(HALF_WIDTH, 15*SIXTEENTH_HEIGHT),
                new Vector2D(),
                "Press escape to quit",
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_30
        );
        byMeText = new StringObject(
                new Vector2D(HALF_WIDTH, 15.5*SIXTEENTH_HEIGHT),
                new Vector2D(),
                "by 11BelowStudio (2020)",
                StringObject.MIDDLE_ALIGN,
                StringObject.SANS_20
        );
        //adding these to the collection of them
        menuScreenStringObjects.add(titleText);
        menuScreenStringObjects.add(subtitleText);
        menuScreenStringObjects.add(play);
        menuScreenStringObjects.add(showScores);
        menuScreenStringObjects.add(showCredits);
        menuScreenStringObjects.add(quitText);
        menuScreenStringObjects.add(byMeText);

        scrollingTextToAdd = new ArrayList<>();

        bgMuffins = new ArrayList<>();
        bgAliveMuffins = new ArrayList<>();
        bgMuffinStack = new Stack<>();
    }

    @Override
    void drawModel(Graphics2D g) {
        //draw the muffins
        for (BackgroundMuffinObject m: bgMuffins){
            m.draw(g);
        }
        for (StringObject o : hudObjects) {
            o.draw(g);
            //g.draw(o.getAreaRectangle());
            //and then the HUD (so its displayed above the game objects)
        }
    }

    @Override
    void refreshModelLists() {
        hudObjects.clear();
        hudObjects.addAll(aliveHUD);
        aliveHUD.clear();

        bgMuffins.clear();
        bgMuffins.addAll(bgAliveMuffins);
        bgAliveMuffins.clear();
    }

    @Override
    public TitleScreen revive() {
        super.revive();
        return this;
    }

    void startModelMusic(){
        //SoundManager.startMenu();
    }

    void stopModelMusic(){
        //SoundManager.stopMenu();
    }

    @Override
    void setupModel() {
        muffinTimer = 0;
        for (int i = 0; i < 50; i++) {
            bgMuffinStack.push(new BackgroundMuffinObject());
        }


        //titleScreenStateHasChanged = false;

        createScrollingText(OPENING_TEXT, 30, 25);
        titleScreenState = SETTING_UP_SCROLLING_TEXT_STATE;
        titleScreenStateChangeHandler();


        //aliveHUD.add(titleText);
        //aliveHUD.add(subtitleText);
        //aliveHUD.add(play);
        //aliveHUD.add(showScores);

    }


    @Override
    void clearCollections(){
        super.clearCollections();
        scrollingTextToAdd.clear();
    }

    @Override
    void clearModelCollections() {

    }

    @Override
    void updateLoop() {

        boolean titleScreenStateHasChanged = false;

        for (BackgroundMuffinObject o: bgMuffins) {
            o.update();
            if (o.stillAlive()){
                bgAliveMuffins.add(o);
            } else{
                bgMuffinStack.push(o);
            }
        }

        for (StringObject o: hudObjects) {
            o.update();
            if (o.stillAlive()){
                aliveHUD.add(o);
            }
        }


        if (Math.random()* MUFFIN_CHANCES < muffinTimer && canWeSpawnAMuffin()){
            bgAliveMuffins.add(bgMuffinStack.pop().revive());
            muffinTimer = 0;
        } else{
            muffinTimer++;
        }

        Action currentAction = ctrl.getAction();
        switch (titleScreenState) {
            case SHOWING_SCROLLING_TEXT_STATE:
                if (currentAction.checkForSpacePress() || aliveHUD.isEmpty()) {
                    //move to menu state if space pressed or aliveHUD empties whilst showing scrolling text
                    titleScreenState = SHOWING_MENU_STATE;
                    titleScreenStateHasChanged = true;
                }
                break;
            case SHOWING_MENU_STATE:
                if(currentAction.checkForClick()){
                    Point clickPoint = currentAction.getClickLocation();
                    System.out.println(clickPoint);
                    if (titleText.isClicked(clickPoint)){
                        //SoundManager.whoIsJoe();
                    } else if (subtitleText.isClicked(clickPoint)){
                        //SoundManager.discussion();
                    } else if (play.isClicked(clickPoint)){
                        titleScreenState = START_GAME_STATE;
                        titleScreenStateHasChanged = true;
                    } else if (showScores.isClicked(clickPoint)){
                        createScrollingText(hs.StringArrayListLeaderboard(), 30, 50);
                        titleScreenStateHasChanged = true;
                    } else if (showCredits.isClicked(clickPoint)){
                        createScrollingText(CREDITS_TEXT, 30, 50);
                        titleScreenStateHasChanged = true;
                    } else if (quitText.isClicked(clickPoint)){
                        quitText.cycleColours();
                    } else if (byMeText.isClicked(clickPoint)){
                        byMeText.cycleColours();
                    }
                } else if (currentAction.checkForSpacePress()){
                    titleScreenState = START_GAME_STATE;
                    titleScreenStateHasChanged = true;
                }
                break;
            case SETTING_UP_SCROLLING_TEXT_STATE:
            case START_GAME_STATE:
                //shouldn't be at these values tbh
                break;
        }
        if (titleScreenStateHasChanged){
            //handle the state changes (if the states have changed)
            titleScreenStateChangeHandler();
        }
    }



    private void createScrollingText(ArrayList<String> theText, int distFromBottom, double scrollSpeed){
        scrollingTextToAdd.clear();
        titleScreenState = SETTING_UP_SCROLLING_TEXT_STATE;
        for (String s: theText){
            if (!s.isEmpty()) {
                scrollingTextToAdd.add(new StringObject(new Vector2D(HALF_WIDTH, GAME_HEIGHT + distFromBottom), scrollSpeed, s, StringObject.MIDDLE_ALIGN));
            }
            //distFromBottom += distBetweenLines;
            distFromBottom += 22;
        }
    }

    private void titleScreenStateChangeHandler(){
        switch (titleScreenState){
            case SETTING_UP_SCROLLING_TEXT_STATE:
                //removes existing contents from aliveHUD
                aliveHUD.clear();
                //puts the scrolling text that needs adding to the aliveHUD
                aliveHUD.addAll(scrollingTextToAdd);

                //now showing the scrolling text;
                titleScreenState = SHOWING_SCROLLING_TEXT_STATE;
                break;
            case SHOWING_SCROLLING_TEXT_STATE:
                //if state changes whilst showing scrolling text, go to menu
                titleScreenState = SHOWING_MENU_STATE;
                //NO BREAK HERE, AUTOMATICALLY SHOWS THE MENU NOW
            case SHOWING_MENU_STATE:
                //wipes contents (the scrolling text) of aliveHUD
                aliveHUD.clear();
                //revives and adds the menu StringObjects to aliveHUD
                for (StringObject s: menuScreenStringObjects) {
                    aliveHUD.add(s.revive());
                }
                break;
            case START_GAME_STATE:
                //just stop the title screen entirely when game needs to start
                endThis();
                break;
        }
    }

    private boolean canWeSpawnAMuffin(){
        return (!bgMuffinStack.isEmpty());
    }

}
