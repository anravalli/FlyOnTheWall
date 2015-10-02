package flyonthewall;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import flyonthewall.base.Entity;
import flyonthewall.base.msg.GameMessage;
import flyonthewall.base.msg.GameMessagesType;

/**
 * Created by andrea on 16/09/15.
 */
public class EntityManager {
    private static EntityManager mEntityManager = null;
    Collection<Entity> entities = null;
    private HashMap<String, Entity> mRegisteredEntities = new HashMap<String, Entity>();

    public static EntityManager getEntityManager() {
        if (mEntityManager == null) {
            mEntityManager = new EntityManager();
        }
        return mEntityManager;
    }

    public void registerEntity(Entity new_entity) {
        Log.d("EntityManager", "registering: " + new_entity.getName());
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
        checkCollisions();
    }

    private void checkCollisions() {
        HashMap<String, Entity> targets = new HashMap<String, Entity>(mRegisteredEntities);
        Iterator<Entity> entity_iter = targets.values().iterator();

        while (entity_iter.hasNext()) {
            Entity entity = entity_iter.next();
            //good intention but bad bug!
            // Entity entity = new Entity(e.getName(), e.getType());
            entity_iter.remove();

            HashMap<String, Entity> details = new HashMap<String, Entity>();

            ArrayList<Entity> target_list = new ArrayList<Entity>(targets.values());
            for (Entity target : target_list) {
                if (entity.checkCollision(target)) {
                    details.put(target.getName(), target);
                }
            }
            if (details.size() > 0) {
                details.put(entity.getName(), entity);
                GameMessage msg = new GameMessage(GameMessagesType.CollisionDetected, details);
                //dispatch collision message
                GameMsgDispatcher.getMessageDispatcher().dispatchMessage(msg);
            }
        }
    }
}
