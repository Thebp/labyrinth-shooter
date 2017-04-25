package gruppe5.common.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Entity implements Serializable {
    private final UUID ID = UUID.randomUUID();
    private Map<String, Entity> subEntities = new ConcurrentHashMap<>();
    private boolean collidable;
    private boolean dynamic;
    private String imagePath;
    private boolean isBackground = false;
    private String soundPath;
    private float x;
    private float y;
    private float dx;
    private float dy;
    private float radians;
    private float maxSpeed;
    private float acceleration;
    private float deacceleration;
    private float[] shapeX = new float[4];
    private float[] shapeY = new float[4];
    private int rotationSpeed;
    private int life;
    private int damage;
    private float radius;
    private boolean isHit = false;
    private float expiration;

    public boolean isDynamic() {
        return dynamic;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    
    public boolean isBackground() {
        return isBackground;
    }
    
    public void setIsBackground(boolean isBackground) {
        this.isBackground = isBackground;
    }

    public String addSubEntity(Entity entity) {
        subEntities.put(entity.getID(), entity);
        return entity.getID();
    }

    public void removeSubEntity(String entityID) {
        subEntities.remove(entityID);
    }

    public void removeSubEntity(Entity entity) {
        subEntities.remove(entity.getID());
    }
    
    public Collection<Entity> getEntities() {
        return subEntities.values();
    }

    public <E extends Entity> List<Entity> getEntities(Class<E>... entityTypes) {
        List<Entity> r = new ArrayList<>();
        for (Entity e : getEntities()) {
            for (Class<E> entityType : entityTypes) {
                if (entityType.equals(e.getClass())) {
                    r.add(e);
                }
            }
        }
        return r;
    }

    public Entity getEntity(String ID) {
        return subEntities.get(ID);
    }

    public boolean isCollidable() {
        return collidable;
    }

    public void setCollidable(boolean collidable) {
        this.collidable = collidable;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }
    
    public void reduceExpiration(float delta){
        this.expiration -= delta;
    }
    
    public float getExpiration(){
        return expiration;
    }
    
    public void setExpiration(float value){
        this.expiration = value;
    }
    
    public boolean isHit(){
        return isHit;
    }
    
    public void setIsHit(boolean hit){
        this.isHit = hit;
    }
    
    public void setRadius(float r){
        this.radius = r;
    }
    
    public float getRadius(){
        return radius;
    }
    
    public int getLife(){
        return life;
    }
    
    public void setLife(int life){
        this.life = life;
    }

    public String getID() {
        return ID.toString();
    }

    public float getDx() {
        return dx;
    }

    public void setDx(float dx) {
        this.dx = dx;
    }

    public float getDy() {
        return dy;
    }

    public void setDy(float dy) {
        this.dy = dy;
    }

    public float getX() {
        return x;
    }
    
    public void setX(float x){
        this.x = x;
    }

    public float getY() {
        return y;
    }
    
    public void setY(float y){
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getRadians() {
        return radians;
    }

    public void setRadians(float radians) {
        this.radians = radians;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(float acceleration) {
        this.acceleration = acceleration;
    }

    public float getDeacceleration() {
        return deacceleration;
    }

    public void setDeacceleration(float deacceleration) {
        this.deacceleration = deacceleration;
    }

    public float[] getShapeX() {
        return shapeX;
    }

    public void setShapeX(float[] shapeX) {
        this.shapeX = shapeX;
    }

    public float[] getShapeY() {
        return shapeY;
    }

    public void setShapeY(float[] shapeY) {
        this.shapeY = shapeY;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }
    

}
