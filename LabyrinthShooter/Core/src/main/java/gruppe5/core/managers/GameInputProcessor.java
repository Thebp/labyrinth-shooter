package gruppe5.core.managers;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import gruppe5.common.data.GameData;
import gruppe5.common.data.GameKeys;

public class GameInputProcessor extends InputAdapter implements InputProcessor {

    private final GameData gameData;
    private int count = 0;
    
    public GameInputProcessor(GameData gameData) {
        this.gameData = gameData;
    }
    
    @Override
    public boolean keyDown(int k) {
        if (k == Keys.UP) {
            gameData.getKeys().setKey(GameKeys.UP, true);
        }
        if (k == Keys.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT, true);
        }
        if (k == Keys.DOWN) {
            gameData.getKeys().setKey(GameKeys.DOWN, true);
        }
        if (k == Keys.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT, true);
        }
        if (k == Keys.ENTER) {
            gameData.getKeys().setKey(GameKeys.ENTER, true);
        }
        if (k == Keys.ESCAPE) {
            gameData.getKeys().setKey(GameKeys.ESCAPE, true);
        }
        if (k == Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, true);
        }
        if (k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, true);
        }
        if (k == Keys.W) {
            gameData.getKeys().setKey(GameKeys.UP, true);
        }
        if (k == Keys.A) {
            gameData.getKeys().setKey(GameKeys.LEFT, true);
        }
        if (k == Keys.S) {
            gameData.getKeys().setKey(GameKeys.DOWN, true);
        }
        if (k == Keys.D) {
            gameData.getKeys().setKey(GameKeys.RIGHT, true);
        }
        if (k == Keys.E) {
            gameData.getKeys().setKey(GameKeys.ENTER, true);
        }
        if (k == Keys.COMMA) {
            if (gameData.isNoclip()) {
                System.out.println("Noclip disabled");
                gameData.setNoclip(false);
            } else {
                System.out.println("Noclip enabled");
                gameData.setNoclip(true);
            }
        }
        
        return true;
    }
    
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        gameData.getKeys().setKey(GameKeys.SPACE, true);
        return super.touchDown(screenX, screenY, pointer, button); //To change body of generated methods, choose Tools | Templates.
    }    
    
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        gameData.getKeys().setKey(GameKeys.SPACE, false);
        return super.touchUp(screenX, screenY, pointer, button); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public boolean keyUp(int k) {
        if (k == Keys.UP) {
            gameData.getKeys().setKey(GameKeys.UP, false);
        }
        if (k == Keys.LEFT) {
            gameData.getKeys().setKey(GameKeys.LEFT, false);
        }
        if (k == Keys.DOWN) {
            gameData.getKeys().setKey(GameKeys.DOWN, false);
        }
        if (k == Keys.RIGHT) {
            gameData.getKeys().setKey(GameKeys.RIGHT, false);
        }
        if (k == Keys.ENTER) {
            gameData.getKeys().setKey(GameKeys.ENTER, false);
        }
        if (k == Keys.ESCAPE) {
            gameData.getKeys().setKey(GameKeys.ESCAPE, false);
        }
        if (k == Keys.SPACE) {
            gameData.getKeys().setKey(GameKeys.SPACE, false);
        }
        if (k == Keys.SHIFT_LEFT || k == Keys.SHIFT_RIGHT) {
            gameData.getKeys().setKey(GameKeys.SHIFT, false);
        }
        if (k == Keys.W) {
            gameData.getKeys().setKey(GameKeys.UP, false);
        }
        if (k == Keys.A) {
            gameData.getKeys().setKey(GameKeys.LEFT, false);
        }
        if (k == Keys.S) {
            gameData.getKeys().setKey(GameKeys.DOWN, false);
        }
        if (k == Keys.D) {
            gameData.getKeys().setKey(GameKeys.RIGHT, false);
        }
        if (k == Keys.E) {
            gameData.getKeys().setKey(GameKeys.ENTER, false);
        }
        return true;
    }
}
