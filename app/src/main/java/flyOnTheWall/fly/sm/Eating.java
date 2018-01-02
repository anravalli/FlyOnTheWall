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

package flyOnTheWall.fly.sm;

import android.util.Log;

import flyOnTheWall.pkg.R;
import flyOnTheWall.base.EntityModel;
import flyOnTheWall.fly.FlyStatus;
import flyOnTheWall.sugar.Sugar;

/**
 * Created by andrea on 02/10/15.
 */
public class Eating extends FlyBaseState {

    private static final String TAG = Eating.class.getSimpleName();

    private static Eating mInstance = null;

    private Eating() {
        mDrawableId = R.drawable.fly;
        nextState = this;
        m_name = "eating";
        m_sugarConsumeSpeed = 0;
    }

    static public Eating getInstance() {
        if (mInstance == null) {
            mInstance = new Eating();
        }
        Log.d(TAG, "instance state: " + mInstance + "(name: " + mInstance.m_name + ")");
        return mInstance;
    }

    @Override
    public void enterState(EntityModel status) {
        super.enterState(status);
        m_model = status;
        m_model.set_mCurrStatusName(m_name);
        nextState = this;
        m_sugarConsumeSpeed = 0;
    }

    @Override
    public void exitState() {
        return;
    }

    @Override
    public void updatePosition() {
        return;
    }

    @Override
    public void update() {
        updatePosition();
        FlyStatus m = (FlyStatus) m_model;
        if (m_food == null) {
            Log.w(TAG, "no food set!!!");
            return;
        }
        int sugar_tx = ((Sugar) m_food).consumeSugar();
        if (sugar_tx != 0 && m.get_sugar() <= m.get_max_sugar()) {
            m.set_sugar(m.get_sugar() + sugar_tx);
        } else {
            nextState = Landed.getInstance();
            nextState.enterState(m_model);
            m_food = null;
        }
        return;
    }

}
