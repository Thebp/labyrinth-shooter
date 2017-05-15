package gruppe5.common.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class World {

    public World() {
        
    }
    
    private final Map<String, Entity> entityMap = new ConcurrentHashMap<>();
    private int worldWidth;
    private int worldHeight;

    public int getWorldWidth() {
        return worldWidth;
    }

    public void setWorldWidth(int worldWidth) {
        this.worldWidth = worldWidth;
    }

    public int getWorldHeight() {
        return worldHeight;
    }

    public void setWorldHeight(int worldHeight) {
        this.worldHeight = worldHeight;
    }

    public String addEntity(Entity entity) {
        entityMap.put(entity.getID(), entity);
        return entity.getID();
    }

    public void removeEntity(String entityID) {
        entityMap.remove(entityID);
    }

    public void removeEntity(Entity entity) {
        entityMap.remove(entity.getID());
    }
    
    public Collection<Entity> getEntities() {
        return entityMap.values();
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
    
    public List<Entity> getBackgroundEntities() {
        List<Entity> backgroundEntities = new ArrayList();
        for (Entity e : getEntities()) {
            if (e.isBackground()) {
                backgroundEntities.add(e);
            }
        }
        return backgroundEntities;
    }
    
    public List<Entity> getForegroundEntities() {
        List<Entity> foregroundEntities = new ArrayList();
        for (Entity e : getEntities()) {
            if (!e.isBackground()) {
                foregroundEntities.add(e);
            }
        }
        return foregroundEntities;
    }

    public Entity getEntity(String ID) {
        return entityMap.get(ID);
    }

}
