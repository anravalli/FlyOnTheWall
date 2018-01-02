/*
 *	Fly On The Wall: a Fly survival game :-)
 *
 *	Copyright 2012 - 2018 Andrea Ravalli <anravalli @ gmail.com>
 *
 *	This file is part of Fly On The Wall.
 *
 *	Fly On The Wall is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	Fly On The Wall is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.

 *	You should have received a copy of the GNU General Public License
 *	along with Fly On The Wall.  If not, see <http://www.gnu.org/licenses/>.
*/

package flyOnTheWall.fly;

//import android.graphics.Rect;

import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import flyOnTheWall.GameModel;
import flyOnTheWall.base.Entity;
import flyOnTheWall.base.EntityType;
import flyOnTheWall.dbg.SensibleAreaMark;
import flyOnTheWall.fly.sm.Eating;
import flyOnTheWall.fly.sm.FlyBaseState;
import flyOnTheWall.fly.sm.Flying;
import flyOnTheWall.fly.sm.Landed;
import flyOnTheWall.fly.sm.Walking;

public class Fly extends Entity {

    private static final String TAG = Fly.class.getSimpleName();
    private final FlyView m_flyView;
    private final FlySugarView m_flySugarLevel;

    private int m_cmd_area_width = 40;
    private int mTolerance = 20;

	public Fly()
	{
        super("fly", EntityType.Fly);

		Log.d(TAG, "Get a new Fly!");

        register();

        model = new FlyStatus(name, 400, 400, 0, 0, 0, 500, new Point(0, 0));
        ((FlyStatus) model).updateCmdArea(m_cmd_area_width);

        currentState = Landed.getInstance();
        ((FlyBaseState) currentState).enterState(model);

        this.m_flyView = new FlyView((FlyStatus) model);

        this.m_flySugarLevel = new FlySugarView();
        this.m_flySugarLevel.setFlyModel(this.get_mFlyStatus());

        SensibleAreaMark marker = new SensibleAreaMark((FlyStatus) model);
        marker.setSensitivity(m_cmd_area_width);
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
        ((FlyStatus) model).updateCmdArea(m_cmd_area_width);

        Log.d(TAG, "Get a new Fly! (" + model + ")");

        currentState = Landed.getInstance();
        currentState.enterState(model);

        this.m_flyView = new FlyView((FlyStatus) model);
        this.m_flySugarLevel = new FlySugarView();
        this.m_flySugarLevel.setFlyModel(this.get_mFlyStatus());

        SensibleAreaMark marker = new SensibleAreaMark((FlyStatus) model);
        marker.setSensitivity(m_cmd_area_width);
        marker.setFlyView(m_flyView);

        register();

        registerToEvent();
        registerToMessages();
    }

    @Override
    public synchronized void update(GameModel gm) {
        //update strategy implemented by the state machine
        currentState = currentState.updateAndGoToNext();
        //update origin
        Point o = model.get_origin();
        Point new_origin = gm.getMapOrigin();
        if (!o.equals(new_origin)) {
            model.set_origin(new_origin);
        }
        Path p = m_flyView.getBoundingPath(mTolerance);
        model.set_bounds(p);
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

        Rect commandArea = ((FlyStatus) model).get_cmd_area();
        //Log.d(TAG, "allowedArea X " + sensibleArea.left + " (w: " + sensibleArea.width() + " )");
        //Log.d(TAG, "allowedArea y " + sensibleArea.top + " (w: " + sensibleArea.height() + " )");

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (commandArea.contains((int) event.getX(), (int) event.getY())) {
                Log.d(TAG, "...switch state!");
                switchState();
            } else {
                //((FlyStatus) model).set_dest_x((int) event.getX());
                //((FlyStatus) model).set_dest_y((int) event.getY());
                ((FlyStatus) model).updateDestinationPoint((int) event.getX(),(int) event.getY());
                ((FlyStatus) model).updateCmdArea(m_cmd_area_width,(int) event.getX(), (int) event.getY());
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (commandArea.contains((int) event.getX(), (int) event.getY())) {
                Log.d(TAG, "ACTION_MOVE...don't switch state!");
                return;
            }
            ((FlyStatus) model).set_dest_x((int) event.getX());
            ((FlyStatus) model).set_dest_y((int) event.getY());
            ((FlyStatus) model).updateCmdArea(m_cmd_area_width, (int) event.getX(),(int) event.getY());
        }

    }

    public synchronized Rect getSensitiveArea(int sensitivity) {
        //Rect r = m_flyView.getSensitiveArea(sensitivity);
        //Point o = model.get_origin();
        Point o = ((FlyStatus) model).get_dest_p();
        Rect r = new Rect(sensitivity + o.x, sensitivity + o.y, o.x - sensitivity, o.y - sensitivity);
        //return new Rect(r.left + o.x, r.top + o.y, r.right + o.x, r.bottom + o.y);
        return r;
    }

    public FlyStatus get_mFlyStatus() {
        return (FlyStatus) model;
    }
}
