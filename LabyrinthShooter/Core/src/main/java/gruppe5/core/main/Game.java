package gruppe5.core.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.GameKeys;
import gruppe5.common.data.World;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.services.IRenderService;
import gruppe5.common.player.PlayerSPI;
import gruppe5.core.managers.GameInputProcessor;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

public class Game implements ApplicationListener {

    private ShapeRenderer sr;
    private BitmapFont bitmapfont;
    private SpriteBatch spriteBatch;
    private static OrthographicCamera cam;
    private final GameData gameData = new GameData();
    private World world = new World();
    private final Lookup lookup = Lookup.getDefault();
    private List<IGamePluginService> gamePlugins = new CopyOnWriteArrayList<>();
    private Lookup.Result<IGamePluginService> result;
    private final float displayWidth = 400;
    private final float displayHeight = 400;
    private final int worldWidth = 2000;
    private final int worldHeight = 2000;
    

    @Override
    public void create() {
        
        gameData.setDisplayWidth(worldWidth);
        gameData.setDisplayHeight(worldHeight);

        cam = new OrthographicCamera(displayWidth, displayHeight);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();
        
        sr = new ShapeRenderer();
        bitmapfont = new BitmapFont();
        bitmapfont.setScale(.50f, .50f);
        spriteBatch = new SpriteBatch();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));
        
        result = lookup.lookupResult(IGamePluginService.class);
        result.addLookupListener(lookupListener);
        result.allItems();

        for (IGamePluginService plugin : result.allInstances()) {
            plugin.start(gameData, world);
            gamePlugins.add(plugin);
        }
//        for (IRenderService renderService : getRenderServices()) {
//            renderService.create(gameData, world);
//        }
    }

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());
        
        update();
        
        updateCam();
        
        draw();
        
        gameData.getKeys().update();
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        if(gameData.getKeys().isPressed(GameKeys.SHIFT))
        {
            if(cam.viewportWidth == displayWidth){
                cam.viewportWidth = worldWidth;
                cam.viewportHeight = worldHeight;
            }
            else{
                cam.viewportWidth = displayWidth;
                cam.viewportHeight = displayHeight;
            }
        }
    }
    
    private Entity getPlayer(){
        PlayerSPI playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        return playerSPI.getPlayer(world);
    }
    
    private void updateCam(){
        
        cam.position.set(getPlayer().getX(), getPlayer().getY(), 0);
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);
    }

    private void draw() {
//        for (IRenderService renderService : getRenderServices()) {
//            renderService.render(gameData, world);
//        }
        for (Entity entity : world.getEntities()) {
            sr.setColor(1, 1, 1, 1);

            sr.begin(ShapeRenderer.ShapeType.Line);

            float[] shapex = entity.getShapeX();
            float[] shapey = entity.getShapeY();

            for (int i = 0, j = shapex.length - 1;
                    i < shapex.length;
                    j = i++) {

                sr.line(shapex[i], shapey[i], shapex[j], shapey[j]);
            }

            sr.end();
        }
        drawFont();
    }
    
    public void drawFont(){
        spriteBatch.begin();
        bitmapfont.setColor(Color.GREEN);
        bitmapfont.drawMultiLine(spriteBatch, "LABYRINTH SHOOTER" + "\n" + "FPS: "
        + Gdx.graphics.getFramesPerSecond() + "\n" + "Entites: " + world.getEntities().size(),
        getPlayer().getX() - 180, getPlayer().getY() + 180);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
    }
    
    private Collection<? extends IEntityProcessingService> getEntityProcessingServices() {
        return lookup.lookupAll(IEntityProcessingService.class);
    }

    private Collection<? extends IRenderService> getRenderServices() {
        return lookup.lookupAll(IRenderService.class);
    }
    
    private final LookupListener lookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {

            Collection<? extends IGamePluginService> updated = result.allInstances();

            for (IGamePluginService us : updated) {
                // Newly installed modules
                if (!gamePlugins.contains(us)) {
                    us.start(gameData, world);
                    gamePlugins.add(us);
                }
            }

            // Stop and remove module
            for (IGamePluginService gs : gamePlugins) {
                if (!updated.contains(gs)) {
                    gs.stop(gameData, world);
                    gamePlugins.remove(gs);
                }
            }
        }

    };
}
