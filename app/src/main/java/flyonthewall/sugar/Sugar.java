package flyonthewall.sugar;

import android.util.Log;

import flyonthewall.GameMsgDispatcher;
import flyonthewall.InputDispatcher;
import flyonthewall.base.Entity;
import flyonthewall.base.EntityType;

/**
 * Created by andrea on 27/09/15.
 */
public class Sugar extends Entity {
    private static final String TAG = Sugar.class.getSimpleName();
    private final SugarView m_SugarView;
    private SugarEntityModel mSugarStatus;

    private int mTolerance = 20;

    public Sugar() {
        super("sugar", EntityType.Sugar);
        Log.d(TAG, "Get a new Sugar!");

        mSugarStatus = new SugarEntityModel();
        m_SugarView = new SugarView(mSugarStatus);
        currentState = SugarIdleState.getInstance();

        register();

        //registerToEvent();
        registerToMessages();
    }

    @Override
    public synchronized void update() {
        bounding_box = m_SugarView.getBoundingBox(mTolerance);
    }

    public int consumeSugar() {
        int sugar = mSugarStatus.get_sugar() - mSugarStatus.get_consume_speed();
        if (sugar > 0) {
            mSugarStatus.set_sugar(sugar);
            return sugar;
        }
        GameMsgDispatcher.getMessageDispatcher().unregisterToGameMessages(name);
        InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
        unregister();
        return 0;
    }
}