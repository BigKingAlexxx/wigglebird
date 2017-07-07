package com.alex.wigglebird.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by elexx on 12.06.2017.
 */

public class Ground {

    private static final int Y_OFFSET = -50;

    private Texture texture, textureNight;
    private Vector2 position;

    public Ground(float x) {
        texture = new Texture("ground.png");
        textureNight = new Texture("ground_night_0.1.png");
        position = new Vector2(x, Y_OFFSET);
    }

    public Texture getTexture() {
        return com.alex.wigglebird.FlappyDemo.IS_NIGHT ? textureNight : texture;
    }

    public Vector2 getPosGround() {
        return position;
    }

    public void setPosGround(float x) {
        position.x = x;
    }

    public void update() {
        position.x -= 0.75f;
    }

    public void dispose() {
        if (!com.alex.wigglebird.FlappyDemo.IS_NIGHT) texture.dispose();
        else textureNight.dispose();
    }
}
