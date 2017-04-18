/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.data;

import java.awt.image.VolatileImage;
import java.util.UUID;

/**
 *
 * @author Nick
 */
public class UIElement {
    private final UUID ID = UUID.randomUUID();
    private VolatileImage image;
    private float x;

    public VolatileImage getImage() {
        return image;
    }

    public void setImage(VolatileImage image) {
        this.image = image;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
    private float y;
    
    public String getID() {
        return ID.toString();
    }
}
