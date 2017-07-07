package com.alex.wigglebird.sprites;

import com.alex.wigglebird.FlappyDemo;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


/**
 * Created by elexx on 01.06.2017.
 */

public class Bird {

    private static final int GRAVITY = -15;
    private static final int MOVEMENT = 100;
    private Vector3 position;
    private Vector3 velocity;
    private Rectangle bounds;
    private Animation birdAnimation;
    private Texture texture;
    public Sound flap;
    private float rotation;

    public Bird(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        texture = new Texture("birdanimation.png");
        birdAnimation = new Animation(new TextureRegion(texture), 3, 0.5f);
        //bounds = new Rectangle(x, y, texture.getWidth() / 3, texture.getHeight());
        bounds = new Rectangle(x, y, texture.getWidth() / 3.75f, texture.getHeight() / 1.25f);
        flap = Gdx.audio.newSound(Gdx.files.internal("sfx_wing.ogg"));
    }

    public void update(float dt) {
        if (!shouldntFlap()) birdAnimation.update(1.5f * dt);
        if(position.y > 0) velocity.add(0, GRAVITY, 0);
        velocity.scl(dt);
        position.add(MOVEMENT * dt, velocity.y, 0);
        if(position.y < 0) position.y = 0;
        if(position.y + texture.getHeight() / 1.25f > FlappyDemo.HEIGHT / 2) position.y = FlappyDemo.HEIGHT / 2 - texture.getHeight() / 1.25f;
        velocity.scl(1 / dt);
        // Rotate counterclockwise
        if (velocity.y < 0) {
            rotation -= 600 * (dt * 0.25);

            if (rotation < -50) {
                rotation = -50;
            }
        }

        // Rotate clockwise
        if (isFalling()) {
            rotation += 480 * (dt);
            if (rotation > 40) {
                rotation = 40;
            }

        }
        bounds.setPosition(position.x, position.y);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(int angle) {
        rotation = angle;
    }

    public boolean isFalling() {
        return velocity.y > 110;
    }

    public boolean shouldntFlap() {
        return velocity.y < 0;
    }

    public Vector3 getPosition() {
        return position;
    }

    public Animation getBirdAnimation() {
        return birdAnimation;
    }

    public TextureRegion getTexture() {
        return birdAnimation.getFrame();
    }

    public void jump() {
        velocity.y = 270;
        flap.play(0.25f);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void dispose() {
        texture.dispose();
        flap.dispose();
    }

    public void setPosition(int x, int y) {
        position.x = x;
        position.y = y;
    }
}
