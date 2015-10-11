package flyonthewall;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import flyonthewall.base.Entity;
import flyonthewall.base.EntityType;
import flyonthewall.base.msg.GameMessage;
import flyonthewall.base.msg.GameMessagesType;
import flyonthewall.fly.Fly;
import flyonthewall.fly.FlyStatus;

/**
 * Created by andrea on 16/09/15.
 */
public class EntityManager {
    private static EntityManager mEntityManager = null;

    //private GameModel mGameModel = null;
    //Point mOrigin = new Point(0, 0);
    //Collection<Entity> entities = null;
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

    public void updateEntities(GameModel gameModel) {
        //when updating entities also fly position must be checked in order to activate scrolling
        //activating scrolling means updating the origin point for all entities and GameView
        Vector<Entity> entities = new Vector<Entity>(mRegisteredEntities.values());
        int i = entities == null ? 0 : entities.size();
        while (--i >= 0) {
            Entity e = entities.get(i);
            if (e.getType() == EntityType.Fly) {
                Point o = checkScreenBorders((Fly) e, gameModel);
                if (!o.equals(gameModel.getVpOrigin())) {
                    gameModel.setVpOrigin(o);
                }
            }
            entities.get(i).update(gameModel.getVpOrigin());
        }
        checkCollisions();
    }

    private Point checkScreenBorders(Fly e, GameModel gameModel) {
        int left_x = (int) (gameModel.getVpOrigin().x * 1.2);
        int top_y = (int) (gameModel.getVpOrigin().y * 1.2);
        int right_x = (int) (left_x + gameModel.getViewWidth() * 0.8);
        int bottom_y = (int) (top_y + gameModel.getViewHeight() * 0.8);

        FlyStatus flyStatus = e.get_mFlyStatus();
        Point o = gameModel.getVpOrigin();

        if (flyStatus.get_x() > right_x) {
            //move to the right
            o.x = +flyStatus.get_x() - right_x;
        } else if (flyStatus.get_x() < left_x) {
            //move to the left
            o.x = +flyStatus.get_x() - left_x;
        }

        if (flyStatus.get_y() > bottom_y) {
            //move to the down
            o.y = +flyStatus.get_y() - bottom_y;
        } else if (flyStatus.get_y() < top_y) {
            //move to the up
            o.y = +flyStatus.get_y() - top_y;
        }

        return o;
    }

    private void checkCollisions() {
        HashMap<String, Entity> targets = new HashMap<String, Entity>(mRegisteredEntities);
        Iterator<Entity> entity_iter = targets.values().iterator();
        ArrayList<GameMessage> collisionsList = new ArrayList<GameMessage>();
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
                collisionsList.add(msg);
            }
        }
        //now all collisions are detected: prepare for cancelling redundant messages
        //1.sort based on number of entities
        Collections.sort(collisionsList, new Comparator<GameMessage>() {
            public int compare(GameMessage lhs, GameMessage rhs) {
                return lhs.details.size() < rhs.details.size() ? -1 : lhs.details.size() == rhs.details.size() ? 0 : 1;
            }
        });
        //2. iterate over sorted messages
        /*Iterator<GameMessage> msgIterator = collisionsList.iterator();
        while (msgIterator.hasNext()){
            if()
        }*/
        for (GameMessage msg : collisionsList) {
            //dispatch collision messages
            GameMsgDispatcher.getMessageDispatcher().dispatchMessage(msg);
        }
    }
}
