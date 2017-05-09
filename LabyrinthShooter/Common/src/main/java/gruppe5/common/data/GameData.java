package gruppe5.common.data;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameData {

    private float delta;
    private int displayWidth;
    private int displayHeight;
    private int worldWidth;
    private int worldHeight;
    private int mouseX;
    private int mouseY;
    private boolean noclip;

    private final GameKeys keys = new GameKeys();
    private List<UIElement> uiElements = new CopyOnWriteArrayList<>();
    public static final float UNIT_SIZE = 25;

    public boolean isNoclip() {
        return noclip;
    }

    public void setNoclip(boolean noclip) {
        this.noclip = noclip;
    }
    
    public int getMouseX() {
        return mouseX;
    }

    public void setMouseX(int mouseX) {
        this.mouseX = mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public void setMouseY(int mouseY) {
        this.mouseY = mouseY;
    }
    
    public void addUIElement(UIElement element) {
        uiElements.add(element);
    }
    
    public void removeUIElement(UIElement element) {
        uiElements.remove(element);
    }
    
    public List<UIElement> getUIElements() {
        return uiElements;
    }

    public GameKeys getKeys() {
        return keys;
    }

    public void setDelta(float delta) {
        this.delta = delta;
    }

    public float getDelta() {
        return delta;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }
}
