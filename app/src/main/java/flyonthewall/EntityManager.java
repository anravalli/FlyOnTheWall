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

    private HashMap<String, Entity> mRegisteredEntities = new HashMap<String, Entity>();
    private boolean fw = false;

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
                //Point o = squareScrollingAnimation(gameModel);
                if (!o.equals(gameModel.getMapOrigin())) {
                    gameModel.setMapOrigin(o);
                }
            }
            entities.get(i).update(gameModel.getMapOrigin());
        }
        checkCollisions();
    }

    private Point squareScrollingAnimation(GameModel gm) {
        // scrolling animation
        Point o = new Point(gm.getMapOrigin());
        int half_map_w = gm.getMapWidth() / 2;
        int half_map_h = gm.getMapHeight() / 2;

        if (!fw && o.x >= -half_map_w) {
            o.x--;
            return o;
        } else {
            //scroll_l = false;
            if (!fw && o.y >= -half_map_h) {
                o.y--;
                return o;
            } else
                fw = true;
        }

        if (fw && (o.x + half_map_w <= half_map_w)) {
            o.x++;
            return o;
        } else {
            if (fw && o.y + half_map_h <= half_map_h) {
                o.y++;
                return o;
            } else
                fw = false;
        }
        return o;
    }

    private Point checkScreenBorders(Fly e, GameModel gameModel) {
        FlyStatus flyStatus = e.get_mFlyStatus();
        Point o = gameModel.getMapOrigin();
        int left_x = (int) (gameModel.getViewWidth() * 0.3);
        int top_y = (int) (gameModel.getViewHeight() * 0.3);
        int right_x = (int) (gameModel.getViewWidth() * 0.7);
        int bottom_y = (int) (gameModel.getViewHeight() * 0.7);
        int half_map_w = gameModel.getMapWidth() / 2;
        int half_map_h = gameModel.getMapHeight() / 2;
        int rel_x = flyStatus.get_x() + o.x;
        int rel_y = flyStatus.get_y() + o.y;

        if (rel_x >= right_x && o.x >= -half_map_w) {
            //move to the left
            o.x = o.x - e.getCurrentState().getM_speed();
        } else if (rel_x <= left_x && (o.x + gameModel.getViewHeight() <= half_map_w)) {
            //move to the right
            o.x = o.x + e.getCurrentState().getM_speed();
        }

        if (rel_y >= bottom_y && (o.y >= -half_map_h)) {
            //move to the down
            o.y = o.y - e.getCurrentState().getM_speed();
        } else if (rel_y <= top_y && (o.y + gameModel.getViewHeight() <= half_map_h)) {
            //move to the up
            o.y = o.y + e.getCurrentState().getM_speed();
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
