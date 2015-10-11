package flyonthewall.sugar;

import android.graphics.Point;
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
    private final SugarEntityModel mSugarStatus;

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
    public synchronized void update(Point mOrigin) {
        mSugarStatus.set_x(mOrigin.x);
        mSugarStatus.set_y(mOrigin.y);
        bounding_box = m_SugarView.getBoundingBox(mTolerance);
    }

    /**
     * returns the amount of sugar consumed (defined by {@code SugarEntityModel.m_consume_speed})
     *
     * @return amount of sugar consumed
     */
    public int consumeSugar() {
        int sugar = mSugarStatus.get_sugar() - mSugarStatus.get_consume_speed();
        if (sugar > 0) {
            mSugarStatus.set_sugar(sugar);
            return mSugarStatus.get_consume_speed();
        }
        GameMsgDispatcher.getMessageDispatcher().unregisterToGameMessages(name);
        InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
        unregister();
        return 0;
    }
}