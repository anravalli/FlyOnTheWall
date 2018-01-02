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

package flyOnTheWall.sugar;

import android.graphics.Path;
import android.graphics.Point;
import android.util.Log;

import flyOnTheWall.GameModel;
import flyOnTheWall.GameMsgDispatcher;
import flyOnTheWall.InputDispatcher;
import flyOnTheWall.base.Entity;
import flyOnTheWall.base.EntityType;

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
    public synchronized void update(GameModel gm) {
        Point o = model.get_origin();
        Point new_origin = gm.getMapOrigin();
        if (!o.equals(new_origin)) {
            model.set_origin(new_origin);
        }
        model.set_offscreen(isOffScreen(gm));
        if (model.is_offscreen()) {
            ((SugarEntityModel) model).set_v_width(gm.getViewWidth());
            ((SugarEntityModel) model).set_v_height(gm.getViewHeight());
        }
        Path p = m_SugarView.getBoundingPath(mTolerance);
        model.set_bounds(p);
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