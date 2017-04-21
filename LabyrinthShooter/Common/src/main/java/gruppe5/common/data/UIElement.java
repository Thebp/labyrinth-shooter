/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gruppe5.common.data;

import java.awt.image.BufferedImage;
import java.util.UUID;

/**
 *
 * @author Nick
 */
public class UIElement {
    private final UUID ID = UUID.randomUUID();
    private BufferedImage image;
    private float x;
    private float y;
    
    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
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
    
    public String getID() {
        return ID.toString();
    }
}
