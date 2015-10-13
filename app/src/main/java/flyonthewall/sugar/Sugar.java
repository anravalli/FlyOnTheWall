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

    //x and y are the coordinates referred to the map (map upper left corner = 0,0))
    public Sugar(String name, int x, int y, int sugar, Point origin) {
        super(name, EntityType.Sugar);
        Log.d(TAG, "Get a new Sugar!");

        //the position stored in status are relative to the map?
        //   --> the view must apply the view port translation (offset to view origin)
        //the position stored in model is relative to the view?
        //   --> offset to the view must be calculated here
        //selected: relative to map
        mSugarStatus = new SugarEntityModel(name, x, y, 0, 0, sugar, origin);
        m_SugarView = new SugarView(mSugarStatus);
        currentState = SugarIdleState.getInstance();

        register();

        //registerToEvent();
        registerToMessages();
    }

    @Override
    public synchronized void update(Point new_origin) {
        Point origin = mSugarStatus.get_origin();
        if (!origin.equals(new_origin)) {
            mSugarStatus.set_origin(new_origin);
        }
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