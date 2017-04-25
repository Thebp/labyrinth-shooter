package gruppe5.core.main;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.BufferUtils;
import gruppe5.common.data.Entity;
import gruppe5.common.data.GameData;
import gruppe5.common.data.GameKeys;
import gruppe5.common.data.UIElement;
import gruppe5.common.data.World;
import gruppe5.common.services.IEntityProcessingService;
import gruppe5.common.services.IGameInitService;
import gruppe5.common.services.IGamePluginService;
import gruppe5.common.services.IRenderService;
import gruppe5.common.player.PlayerSPI;
import gruppe5.common.resources.ResourceSPI;
import gruppe5.common.services.IUIService;
import gruppe5.core.managers.AssetsJarFileResolver;
import gruppe5.core.managers.GameInputProcessor;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;

public class Game implements ApplicationListener {

    private ShapeRenderer sr;
    private BitmapFont bitmapfont;
    private SpriteBatch spriteBatch;
    private SpriteBatch uiBatch; // For drawing UI elements, no ProjectionMatrix
    private Sprite sprite;
    private static OrthographicCamera cam;
    private final GameData gameData = new GameData();
    Vector3 mousePosition = new Vector3();
    private World world = new World();
    private final Lookup lookup = Lookup.getDefault();
    private List<IGamePluginService> gamePlugins = new CopyOnWriteArrayList<>();
    private List<IGameInitService> gameInits = new CopyOnWriteArrayList<>();
    private Lookup.Result<IGamePluginService> gamePluginResult;
    private Lookup.Result<IGameInitService> gameInitResult;
    private Lookup.Result<IGamePluginService> result;
    private final float displayWidth = 400;
    private final float displayHeight = 400;
    private final int worldWidth = 2000;
    private final int worldHeight = 2000;
    private AssetsJarFileResolver jfhr;
    private AssetManager am;
    private Texture texture;

    @Override
    public void create() {
        gameData.setWorldWidth(worldWidth);
        gameData.setWorldHeight(worldHeight);

        world.setWorldWidth(worldWidth);
        world.setWorldHeight(worldHeight);

        gameData.setDisplayWidth(1000);
        gameData.setDisplayHeight(800);

        cam = new OrthographicCamera(displayWidth, displayHeight);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        sr = new ShapeRenderer();
        bitmapfont = new BitmapFont();
        bitmapfont.setScale(.50f, .50f);
        spriteBatch = new SpriteBatch();
        uiBatch = new SpriteBatch();

        Gdx.input.setInputProcessor(new GameInputProcessor(gameData));

        gameInitResult = lookup.lookupResult(IGameInitService.class);
        gameInitResult.allItems();

        gamePluginResult = lookup.lookupResult(IGamePluginService.class);
        gamePluginResult.addLookupListener(lookupListener);
        gamePluginResult.allItems();

        for (IGameInitService initService : gameInitResult.allInstances()) {
            initService.start(gameData, world);
            gameInits.add(initService);
        }
        for (IGamePluginService plugin : gamePluginResult.allInstances()) {
            plugin.start(gameData, world);
            gamePlugins.add(plugin);
        }
//        for (IRenderService renderService : getRenderServices()) {
//            renderService.create(gameData, world);
//        }

        sprite = new Sprite();

        jfhr = new AssetsJarFileResolver();
        am = new AssetManager(jfhr);
    }

    @Override
    public void render() {
        // clear screen to black
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameData.setDelta(Gdx.graphics.getDeltaTime());

        update();

        updateCam();

        getMouseInput();

        draw();

        gameData.getKeys().update();
    }

    private void getMouseInput() {
        mousePosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mousePosition);
        gameData.setMouseX((int) mousePosition.x);
        gameData.setMouseY((int) mousePosition.y);
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : getEntityProcessingServices()) {
            entityProcessorService.process(gameData, world);
        }
        for (IUIService uiService : getUIServices()) {
            uiService.process(gameData, world);
        }
        zoomCam();
        
    }

    private void zoomCam() {
        if (gameData.getKeys().isPressed(GameKeys.SHIFT)) {
            if (cam.viewportWidth == displayWidth) {
                cam.viewportWidth = worldWidth;
                cam.viewportHeight = worldHeight;
            } else {
                cam.viewportWidth = displayWidth;
                cam.viewportHeight = displayHeight;
            }
        }
    }

    private Entity getPlayer() {
        PlayerSPI playerSPI = Lookup.getDefault().lookup(PlayerSPI.class);
        return playerSPI.getPlayer(world);
    }

    private void updateCam() {
        cam.position.set(getPlayer().getX(), getPlayer().getY(), 0);
        cam.update();
        spriteBatch.setProjectionMatrix(cam.combined);
        sr.setProjectionMatrix(cam.combined);
    }

    private void draw() {
        Entity player = getPlayer();

        for (Entity entity : world.getBackgroundEntities()) {
            drawSprite(entity, player);
        }

        for (Entity entity : world.getForegroundEntities()) {
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

            drawSprite(entity, player);
        }

        for (UIElement element : gameData.getUIElements()) {
            drawUIElement(element);
        }

    }

    private void drawSprite(Entity entity, Entity player) {
        float distance = (float) Math.sqrt(Math.pow(entity.getX() - player.getX(), 2) + Math.pow(entity.getY() - player.getY(), 2));
        if (distance < 400) {
            if (entity.getImagePath() != null) {
                //uses ResourceSPI that takes entity.getImagePath as argument (string url).
                ResourceSPI spriteSPI = Lookup.getDefault().lookup(ResourceSPI.class);
                String url = spriteSPI.getResourceUrl(entity.getImagePath());

                am.load(url, Texture.class);
                am.finishLoading();
                texture = am.get(url, Texture.class);
                sprite = new Sprite(texture);
                spriteBatch.begin();
                sprite.setSize(entity.getRadius(), entity.getRadius());
                sprite.setPosition(entity.getX() - (entity.getRadius() / 2), entity.getY() - (entity.getRadius() / 2));
                sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
                sprite.setRotation((float) Math.toDegrees(entity.getRadians()));
                sprite.draw(spriteBatch);
                spriteBatch.end();
            }
        }
    }
	
    private void drawUIElement(UIElement element) {
        if (element.getImage() != null) {
            BufferedImage image = element.getImage();
            // Create texture that BufferedImage should be drawn onto
            Texture tex = new Texture(image.getWidth(), image.getHeight(), Format.RGBA8888);

            int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
            IntBuffer buffer = BufferUtils.newIntBuffer(image.getWidth() * image.getHeight());

            // Bind texture to the currently active texture unit
            tex.bind();

            // Load pixels into buffer
            buffer.rewind();
            buffer.put(pixels);
            buffer.flip();

            // Upload buffer to texture unit
            Gdx.gl.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0,
                    image.getWidth(), image.getHeight(), GL12.GL_BGRA,
                    GL12.GL_UNSIGNED_INT_8_8_8_8_REV, buffer);

            // Draw texture
            uiBatch.begin();
            // Height is subtracted from Y, so that the position corresponds to the image's top left corner
            uiBatch.draw(tex, element.getX(), element.getY() - image.getHeight(), image.getWidth(), image.getHeight());
            uiBatch.end();

            tex.dispose();
        }
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

    private Collection<? extends IUIService> getUIServices() {
        return lookup.lookupAll(IUIService.class);
    }

    private final LookupListener lookupListener = new LookupListener() {
        @Override
        public void resultChanged(LookupEvent le) {

            Collection<? extends IGamePluginService> updated = gamePluginResult.allInstances();

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
