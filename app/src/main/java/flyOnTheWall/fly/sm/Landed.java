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

public class Landed extends FlyBaseState {
    private static final String TAG = Landed.class.getSimpleName();
	private static Landed mInstance = null;

	private Landed(){
		mDrawableId = R.drawable.fly;
		m_speed = 0;
		m_speed_z = 0;

		nextState = this;

		m_name = "landed";
	}

    public static Landed getInstance() {
        if (mInstance == null) {
            mInstance = new Landed();
        }
        Log.d(TAG, "instance state: " + mInstance + "(name: " + mInstance.m_name + ")");
        return mInstance;
    }

    @Override
    public void update() {
        updatePosition();
        consumeSugar();
    }

    @Override
    public void enterState(EntityModel status) {
        super.enterState(status);
        m_model = status;
        //copy state configuration to model
        m_model.set_spriteId(mDrawableId);
        m_model.set_mCurrStatusName(m_name);
        m_model.set_z(0);
        m_speed = 0;
		m_speed_z = 0;
		//m_sugarConsumeSpeed=0.5;
		m_sugarConsumeSpeed=0;
        Log.d(TAG, "Entering state:" + m_model.get_mCurrStatusName());
    }

	public void exitState() {
		// TODO Auto-generated method stub
	}

    public void updatePosition() {
        int dest_x = ((FlyStatus) m_model).get_dest_x();
        int dest_y = ((FlyStatus) m_model).get_dest_y();
        int delta_x = (int) (dest_x - m_model.get_x());
        int delta_y = (int) (dest_y - m_model.get_y());
        double dist = Math.hypot(delta_x, delta_y);

		int new_a = 0;

		if(delta_x!=0 || delta_y!=0){
			double rad_a = Math.atan2(delta_y, delta_x);
			new_a = (int) Math.toDegrees(rad_a) + 90;

            m_model.set_heading(new_a);
        }

        if (m_model.get_z() > 5)
            m_model.set_z(m_model.get_z() - 5);
        else if (m_model.get_z() > 0)
            m_model.set_z(0);

		if (dist>0 && dist<10){
			nextState=Walking.getInstance();
            nextState.enterState(m_model);
        }

	}


	
}
