package flyonthewall.base;

import android.graphics.Rect;
import android.view.MotionEvent;

import flyonthewall.EntityManager;
import flyonthewall.GameMsgDispatcher;
import flyonthewall.InputDispatcher;
import flyonthewall.base.msg.GameMessage;
import flyonthewall.base.msg.OnNewGameMessage;

/**
 * Created by andrea on 17/09/15.
 *
 * This class isn't declared abstract
 */
public class Entity {

    protected String name = "";
    protected EntityType type = EntityType.None;


    protected Rect bounding_box = null;
    //protected EntityManager manager = null;

    public Entity(String n_name, EntityType n_type) {
        this.type = n_type;
        this.name = n_name;
    }

    protected void register() {
        EntityManager.getEntityManager().registerEntity(this);
    }

    protected void unregister() {
        EntityManager.getEntityManager().unregisterEntity(name);
    }

    public synchronized void update() {
        //TODO check for better construct (abstract, etc)
        //by default do nothing
    }

    public Rect getBounding_box() {
        return bounding_box;
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }

    public boolean checkCollision(Entity target) {
        if (bounding_box.intersect(target.bounding_box)) {
            return true;
        }
        return false;
    }

    protected void registerToEvent() {
        InputDispatcher.getInputDispatcher().registerToTouchEvent(this.name, new OnTouchCallback() {
            public void onTouch(MotionEvent event) {
                onTouchEvent(event);
            }
        });
    }


    protected void registerToMessages() {
        GameMsgDispatcher.getMessageDispatcher().registerToGameMessages(name, new OnNewGameMessage() {
            public void receiveMessage(GameMessage msg) {
                fetchMessage(msg);
            }
        });
    }

    protected void fetchMessage(GameMessage msg) {

    }

    protected void onTouchEvent(MotionEvent event) {

    }
}
