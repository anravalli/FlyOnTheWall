package flyonthewall.sugar;

import android.util.Log;

import flyonthewall.GameMsgDispatcher;
import flyonthewall.InputDispatcher;
import flyonthewall.base.Entity;
import flyonthewall.base.EntityType;
import flyonthewall.base.msg.GameMessage;

/**
 * Created by andrea on 27/09/15.
 */
public class Sugar extends Entity {
    private static final String TAG = Sugar.class.getSimpleName();
    private final SugarView m_SugarView;
    private SugarModel mSugarStatus;
    private SugarSM mSugarState;


    public Sugar() {
        super("sugar", EntityType.Sugar);
        Log.d(TAG, "Get a new Sugar!");

        mSugarStatus = new SugarModel();
        m_SugarView = new SugarView(mSugarStatus);
        mSugarState = SugarSM.getInstance();

        register();

        //registerToEvent();
        registerToMessages();
    }

    @Override
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
                    mSugarState.manageCollision(msg.details);
                }
                break;
            default:
                /* no-op */
                break;
        }
    }


    public synchronized void update() {
        bounding_box = m_SugarView.getBoundingBox(-5);
    }

    public int consumeSugar() {
        int sugar = mSugarStatus.get_sugar() - mSugarStatus.get_consume_speed();
        if (sugar > 0) {
            return sugar;
        }
        GameMsgDispatcher.getMessageDispatcher().unregisterToGameMessages(name);
        InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
        unregister();
        return 0;
    }
}