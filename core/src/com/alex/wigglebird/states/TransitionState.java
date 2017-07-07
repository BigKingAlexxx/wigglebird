package com.alex.wigglebird.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by elexx on 30.06.2017.
 */

public class TransitionState extends State {

    private ShapeRenderer rect;


    public TransitionState(GameStateManager gsm) {
        super(gsm);
        rect = new ShapeRenderer();

    }

    @Override
    protected void handleInput() {

    }

    @Override
    public void update(float dt) {

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        rect.begin(ShapeRenderer.ShapeType.Filled);
        rect.rect(cam.viewportWidth, cam.viewportHeight, cam.position.x, cam.position.y);
        rect.setColor(0, 0, 0, 0.5f);
        rect.end();
    }

    @Override
    public void dispose() {
        rect.dispose();
    }

    @Override
    public void pause() {

    }
}
