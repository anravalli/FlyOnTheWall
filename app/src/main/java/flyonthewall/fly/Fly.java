package flyonthewall.fly;

//import android.graphics.Rect;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import flyonthewall.base.Entity;
import flyonthewall.base.EntityType;
import flyonthewall.dbg.SensibleAreaMark;
import flyonthewall.fly.sm.Eating;
import flyonthewall.fly.sm.FlyBaseState;
import flyonthewall.fly.sm.Flying;
import flyonthewall.fly.sm.Landed;
import flyonthewall.fly.sm.Walking;

public class Fly extends Entity {

    private static final String TAG = Fly.class.getSimpleName();
    private final FlyView m_flyView;
    private final FlySugarView m_flySugarLevel;

    //private FlyStatus mFlyStatus;

    private int mSensitivity = 20;
    private int mTolerance = 20;

	public Fly()
	{
        super("fly", EntityType.Fly);

		Log.d(TAG, "Get a new Fly!");

        register();

        model = new FlyStatus(name, 400, 400, 0, 0, 0, 500, new Point(0, 0));

        currentState = Landed.getInstance();
        ((FlyBaseState) currentState).enterState(model);

        this.m_flyView = new FlyView((FlyStatus) model);

        this.m_flySugarLevel = new FlySugarView();
        this.m_flySugarLevel.setFlyModel(this.get_mFlyStatus());

        SensibleAreaMark marker = new SensibleAreaMark((FlyStatus) model);
        marker.setSensitivity(mSensitivity);
        marker.setFlyView(m_flyView);

        registerToEvent();
        registerToMessages();

	}

    //x and y are the coordinates referred to the map (map upper left corner = 0,0))
    public Fly(String name, int x, int y, int sugar, Point origin) {
        super(name, EntityType.Fly);

        //the position stored in status are relative to the map
        //   --> the view must apply the view port translation (offset to view origin)
        //mSugarStatus = new SugarEntityModel(name, x, y, 0, 0, sugar, origin);
        model = new FlyStatus(name, x, y, 0, 0, 0, sugar, origin);
        Log.d(TAG, "Get a new Fly! (" + model + ")");

        currentState = Landed.getInstance();
        currentState.enterState(model);

        this.m_flyView = new FlyView((FlyStatus) model);
        this.m_flySugarLevel = new FlySugarView();
        this.m_flySugarLevel.setFlyModel(this.get_mFlyStatus());

        SensibleAreaMark marker = new SensibleAreaMark((FlyStatus) model);
        marker.setSensitivity(mSensitivity);
        marker.setFlyView(m_flyView);

        register();

        registerToEvent();
        registerToMessages();
    }

    @Override
    public synchronized void update(Point new_origin) {
        //update strategy implemented by the state machine
        currentState = currentState.updateAndGoToNext();
        //update origin
        Point o = model.get_origin();
        if (!o.equals(new_origin)) {
            model.set_origin(new_origin);
        }
        Rect r = m_flyView.getBoundingBox(mTolerance);
        Path p = m_flyView.getBoundingPath(mTolerance);
        model.set_bounds(p);
        bounding_box = new Rect(r.left + o.x, r.top + o.y, r.right + o.x, r.bottom + o.y);
    }

    public void forcePosition(int x, int y){
        model.set_x(x);
        model.set_y(y);
        ((FlyStatus) model).set_dest_x(x);
        ((FlyStatus) model).set_dest_y(y);
    }

    public String switchState(){
        Log.d(TAG, "--- switchState ---");
        synchronized (currentState) {
            if (model.get_mCurrStatusName().equals(Walking.getInstance().get_name()))
                currentState = Flying.getInstance();
            else if (model.get_mCurrStatusName().equals(Landed.getInstance().get_name()))
                currentState = Walking.getInstance();
            else if (model.get_mCurrStatusName().equals(Flying.getInstance().get_name()))
                currentState = Landed.getInstance();
            else if (model.get_mCurrStatusName().equals(Eating.getInstance().get_name()))
                currentState = Walking.getInstance();

            ((FlyBaseState) currentState).enterState(model);
        }
        Log.d(TAG, "--- new status: " + model.get_mCurrStatusName());
        return model.get_mCurrStatusName();
    }


    @Override
    protected void onTouchEvent(MotionEvent event) {
        //Log.d(TAG, "down event x: " + event.getX() + ", Y:" + event.getY());
        //Log.d(TAG, "move event x: " + event.getX() + ", Y:" + event.getY());

        Rect sensibleArea = getSensitiveArea(mSensitivity);
        //Log.d(TAG, "allowedArea X " + sensibleArea.left + " (w: " + sensibleArea.width() + " )");
        //Log.d(TAG, "allowedArea y " + sensibleArea.top + " (w: " + sensibleArea.height() + " )");

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (sensibleArea.contains((int) event.getX(), (int) event.getY())) {
                Log.d(TAG, "...switch state!");
                switchState();
            } else {
                ((FlyStatus) model).set_dest_x((int) event.getX());
                ((FlyStatus) model).set_dest_y((int) event.getY());
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            ((FlyStatus) model).set_dest_x((int) event.getX());
            ((FlyStatus) model).set_dest_y((int) event.getY());
        }

    }

    public synchronized Rect getSensitiveArea(int sensitivity) {
        Rect r = m_flyView.getSensitiveArea(sensitivity);
        Point o = model.get_origin();
        return new Rect(r.left + o.x, r.top + o.y, r.right + o.x, r.bottom + o.y);
    }

    public FlyStatus get_mFlyStatus() {
        return (FlyStatus) model;
    }
}
