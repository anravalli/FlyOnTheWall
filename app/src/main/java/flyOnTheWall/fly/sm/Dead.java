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

public class Dead extends FlyBaseState {

	private static final String TAG = Dead.class.getSimpleName();

	private static Dead mInstance = null;
	
	private Dead(){
		mDrawableId = R.drawable.fly_dead;
		m_speed = 0;
		m_speed_z = 0;
		nextState = this;
		m_sugarConsumeSpeed=0;
		m_name = "dead";
	}

    public static Dead getInstance() {
        if (mInstance == null) {
            mInstance = new Dead();
        }
        Log.d(TAG, "instance state: " + mInstance + " (name: " + mInstance.m_name + ")");
        return mInstance;
    }

    @Override
    public void update() {
        //no action in dead state
        return;
    }

    @Override
    public void enterState(EntityModel status) {
        super.enterState(status);
        m_model = status;
        //copy state configuration to model
        m_model.set_spriteId(mDrawableId);
        m_model.set_mCurrStatusName(m_name);
        m_model.set_z(0);
        m_model.set_heading(0);

		m_speed = 0;
		m_speed_z = 0;
		m_sugarConsumeSpeed=0;
        nextState = this;
        Log.d(TAG, "Entering state:" + m_model.get_mCurrStatusName());
    }

	@Override
	public void exitState() {
		// TODO Auto-generated method stub

	}

	@Override
    public void updatePosition() {
    }

}
