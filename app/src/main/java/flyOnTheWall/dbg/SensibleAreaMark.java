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

package flyOnTheWall.dbg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import flyOnTheWall.InputDispatcher;
import flyOnTheWall.ViewManager;
import flyOnTheWall.base.EntityView;
import flyOnTheWall.base.OnTouchCallback;
import flyOnTheWall.fly.FlyStatus;
import flyOnTheWall.fly.FlyView;

public class SensibleAreaMark extends EntityView {
    private static final String TAG = SensibleAreaMark.class.getSimpleName();
    //private static SensibleAreaMark m_tmark = null;
    private float m_x;
    private float m_y;
    private boolean enable = false;
    private FlyView flyView = null;
    private int sensitivity = 0;
    private int fade_t = 100;
    private int fade_t_max = 100;
    private String name = "sens_area";
    //private Point m_origin;
    //private Entity dummy_e;

    public SensibleAreaMark(FlyStatus mFlyStatus) {
        m_x = 300;
        m_y = 300;
        mEntityModel = mFlyStatus;

        /*m_origin = new Point(0,0);
        Entity dummy_e = new Entity("sensmark", EntityType.None) {
            @Override
            public synchronized void update(Point new_origin) {
                Point origin = m_origin;
                if (!origin.equals(new_origin)) {
                    m_origin = new_origin;
                }
            }
        };
        EntityManager.getEntityManager().registerEntity(dummy_e);*/

        registerToEvent();
        ViewManager.getViewManager().register(name, this);
    }

    /*public static SensibleAreaMark getMarker() {
        if (m_tmark == null)
            m_tmark = new SensibleAreaMark();
        return m_tmark;
    }*/

    public void setSensitivity(int s) {
        sensitivity = s;
    }

    public void setFlyView(FlyView flyView) {
        this.flyView = flyView;
    }

    private void setEnable(boolean e) {
        Log.d(TAG, "set to: " + e);
        if (flyView != null) {
            enable = e;
        }
        if (enable) {
            fade_t = fade_t_max;
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!enable)
            return;

        canvas.save();
        Paint paint = new Paint();
        Style style = Style.STROKE;
        int alpha = 255 / fade_t_max * fade_t;
        paint.setColor(Color.GREEN);
        paint.setAlpha(alpha);
        paint.setStyle(style);

        fade_t--;
        if (alpha == 0) {
            setEnable(false);
        }
        //Point o = mEntityModel.get_origin();
        //Rect r = flyView.getSensitiveArea(sensitivity);
        //canvas.drawRect(r.left + o.x, r.top + o.y, r.right + o.x, r.bottom + o.y, paint);
        Rect r = ((FlyStatus)mEntityModel).get_cmd_area();
        if (r != null)
            canvas.drawRect(r, paint);
        canvas.restore();
    }

    @Override
    public int getOpacity() {
        // TODO Auto-generated method stub
        return PixelFormat.UNKNOWN;
    }

    @Override
    public void setAlpha(int alpha) {
        // TODO Auto-generated method stub
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        // TODO Auto-generated method stub

    }

    void registerToEvent() {
        InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
        InputDispatcher.getInputDispatcher().registerToTouchEvent(name, new OnTouchCallback() {
            public void onTouch(MotionEvent event) {
                setEnable(true);
            }
        });
    }
}
