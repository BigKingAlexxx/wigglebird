package com.alex.wigglebird;

import com.alex.wigglebird.states.GameStateManager;
import com.alex.wigglebird.states.MenuState;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyDemo extends ApplicationAdapter {

    public static final int WIDTH = 480;
    public static final int HEIGHT = 800;

    public static final String TITLE = "Flappy Bird";
    private GameStateManager gsm;
    private SpriteBatch batch;
    public boolean isPaused;
    public static boolean MASTER_SOUND_ON = true;
    public static boolean IS_NIGHT = false;
    public static AdHandler handler;
    public static boolean toggle;

    public FlappyDemo(AdHandler handler) {
        this.handler = handler;
    }

    public static void toggleAd() {
        handler.showAds(toggle);
        toggle = !toggle;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        gsm = new GameStateManager();
        Gdx.gl.glClearColor(1, 0, 0, 1);
        gsm.push(new MenuState(gsm));
    }

    @Override
    public void render() {
        /*if (Gdx.input.justTouched()) {
            handler.showAds(toggle);
            toggle = !toggle;
        }*/

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!isPaused) gsm.update(Gdx.graphics.getDeltaTime());
        gsm.render(batch);
    }

    @Override
    public void dispose() {
        super.dispose();
    }

	/*@Override
    public void pause(){
		super.pause();
		isPaused = true;
	}

	@Override
	public void resume() {
		super.resume();
		isPaused = false;
	}*/

    @Override
    public void pause() {
        gsm.getStates().peek().pause();
    }

}
