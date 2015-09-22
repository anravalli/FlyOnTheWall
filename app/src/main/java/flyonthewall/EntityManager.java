package flyonthewall;

import java.util.Collection;
import java.util.HashMap;

import flyonthewall.base.Entity;

/**
 * Created by andrea on 16/09/15.
 */
public class EntityManager {
    private HashMap<String, Entity> mRegisteredEntities = new HashMap<String, Entity>();

    private static EntityManager mEntityManager = null;
    Collection<Entity> entities = null;

    public static EntityManager getEntityManager() {
        if (mEntityManager == null) {
            mEntityManager = new EntityManager();
        }
        return mEntityManager;
    }

    public void registerEntity(Entity new_entity) {
        if (mRegisteredEntities.get(new_entity.getName()) == null) {
            mRegisteredEntities.put(new_entity.getName(),
                    new_entity);
        }
    }

    public void unregisterEntity(String name) {
        if (mRegisteredEntities.get(name) != null) {
            mRegisteredEntities.remove(name);
        }
    }

    public void updateEntities() {
        entities = mRegisteredEntities.values();
        for (Entity e : entities) {
            e.update();
        }

    }

    private void checkCollisions() {

    }
}
