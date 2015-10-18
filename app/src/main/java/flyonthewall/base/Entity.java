package flyonthewall.base;

import android.annotation.TargetApi;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
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
    private static final String TAG = Entity.class.getSimpleName();
    protected String name = "";
    protected EntityType type = EntityType.None;
    protected Rect bounding_box = null;
    protected EntityModel model = null;


    protected EntityStateMachine currentState = null;
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

    public synchronized void update(Point mOrigin) {
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

    public EntityStateMachine getCurrentState() {
        return currentState;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public boolean checkCollision(Entity target) {
        if (bounding_box.intersect(target.bounding_box)) {
            return true;
        }
        /*Path p = new Path();
        if (p.op(model.get_bounds(), target.model.get_bounds(), Path.Op.INTERSECT)) {
            return true;
        }*/
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
        switch (msg.type) {
            case GameExiting:
                GameMsgDispatcher.getMessageDispatcher().unregisterToGameMessages(name);
                InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
                unregister();
                break;
            case CollisionDetected:
                if (msg.details == null) {
                    Log.w(TAG, "Collision detected but entity list is NULL!!!");
                    break;
                }
                if (msg.details.get(name) != null) {
                    currentState.manageCollision(msg.details);
                }
                break;
            default:
                /* no-op */
                break;
        }
    }

    protected void onTouchEvent(MotionEvent event) {

    }
}
