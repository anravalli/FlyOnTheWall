package flyonthewall.sugar;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
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

    private int mTolerance = 20;

    //x and y are the coordinates referred to the map (map upper left corner = 0,0))
    public Sugar(String name, int x, int y, int sugar, Point origin) {
        super(name, EntityType.Sugar);
        Log.d(TAG, "Get a new Sugar!");

        //the position stored in status are relative to the map?
        //   --> the view must apply the view port translation (offset to view origin)
        //the position stored in model is relative to the view?
        //   --> offset to the view must be calculated here
        //selected: relative to map
        model = new SugarEntityModel(name, x, y, 0, 0, sugar, origin);
        m_SugarView = new SugarView((SugarEntityModel) model);
        currentState = SugarIdleState.getInstance();

        register();

        registerToMessages();
    }

    @Override
    public synchronized void update(Point new_origin) {
        Point o = model.get_origin();
        if (!o.equals(new_origin)) {
            model.set_origin(new_origin);
        }
        Rect r = m_SugarView.getBoundingBox(mTolerance);
        Path p = m_SugarView.getBoundingPath(mTolerance);
        model.set_bounds(p);
        bounding_box = new Rect(r.left + o.x, r.top + o.y, r.right + o.x, r.bottom + o.y);
    }

    /**
     * returns the amount of sugar consumed (defined by {@code SugarEntityModel.m_consume_speed})
     *
     * @return amount of sugar consumed
     */
    public int consumeSugar() {
        int sugar = ((SugarEntityModel) model).get_sugar() - ((SugarEntityModel) model).get_consume_speed();
        if (sugar > 0) {
            ((SugarEntityModel) model).set_sugar(sugar);
            return ((SugarEntityModel) model).get_consume_speed();
        }
        GameMsgDispatcher.getMessageDispatcher().unregisterToGameMessages(name);
        InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
        unregister();
        return 0;
    }
}