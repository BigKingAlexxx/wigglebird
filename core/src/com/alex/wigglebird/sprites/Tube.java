package com.alex.wigglebird.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by elexx on 01.06.2017.
 */

public class Tube {

    public static final int TUBE_WIDTH = 52;

    private static final int FLUCTUATION = 150;
    public static final int TUBE_GAP = 80;
    private static final int LOWEST_OPENING = 100;

    private Texture topTube, bottomTube;
    private Vector2 posTopTube, posBotTube;
    private Rectangle boundsTop, boundsBot;
    private Random rand;
    private Rectangle rectScore;

    public boolean test = false;

    public Tube(float x) {
        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        rand = new Random();

        posTopTube = new Vector2(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube = new Vector2(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight() / 1.25f);

        boundsTop = new Rectangle(posTopTube.x, posTopTube.y, topTube.getWidth() / 1.25f, topTube.getHeight() / 1.25f);
        boundsBot = new Rectangle(posBotTube.x, posBotTube.y, bottomTube.getWidth() / 1.25f, bottomTube.getHeight() / 1.25f);

        rectScore = new Rectangle(posTopTube.x + (topTube.getWidth() / 1.25f / 2) - 10,
                posTopTube.y - (Tube.TUBE_GAP / 2) - 30, 20, 60);
    }

    public Texture getTopTube() {
        return topTube;
    }

    public Texture getBottomTube() {
        return bottomTube;
    }

    public Vector2 getPosTopTube() {
        return posTopTube;
    }

    public Vector2 getPosBotTube() {
        return posBotTube;
    }

    public float getRectScoreX() {
        return rectScore.getX();
    }

    public void reposition(float x) {
        posTopTube.set(x, rand.nextInt(FLUCTUATION) + TUBE_GAP + LOWEST_OPENING);
        posBotTube.set(x, posTopTube.y - TUBE_GAP - bottomTube.getHeight() / 1.25f);
        boundsTop.setPosition(posTopTube.x, posTopTube.y);
        boundsBot.setPosition(posBotTube.x, posBotTube.y);
        rectScore.set(posTopTube.x + (topTube.getWidth() / 1.25f / 2) - 10,
                posTopTube.y - (Tube.TUBE_GAP / 2) - 30, 20, 60);
    }

    public boolean collides(Rectangle player) {
        return player.overlaps(boundsTop) || player.overlaps(boundsBot);
    }

    public void dispose() {
        topTube.dispose();
        bottomTube.dispose();
    }
}


