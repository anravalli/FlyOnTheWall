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
import flyOnTheWall.base.EntityStateMachine;
import flyOnTheWall.fly.FlyStatus;

public class Flying extends FlyBaseState {

    private static final String TAG = Flying.class.getSimpleName();

    private static Flying mInstance = null;

    private Flying() {
		mDrawableId = R.drawable.fly;
		m_speed = 10;
        m_speed_z = 10;
        m_rot_speed = 1;
        m_name = "flying";
        m_sugarConsumeSpeed=2;
		
	}

    static public Flying getInstance() {
        if (mInstance == null) {
            mInstance = new Flying();
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
        m_speed = 10;
        m_speed_z = 10;
        m_rot_speed = 1;
		m_sugarConsumeSpeed=2;
		nextState = this;
        Log.d(TAG, "Entering state:" + m_model.get_mCurrStatusName());

        return;
    }

    public void exitState() {
        //stub
    }

	@Override
    public void updatePosition() {
        int dest_x = ((FlyStatus) m_model).get_dest_x();
        int dest_y = ((FlyStatus) m_model).get_dest_y();
        ;
        int delta_x = (int) (dest_x - m_model.get_x());
        int delta_y = (int) (dest_y - m_model.get_y());
        int speed_x = m_speed;
		int speed_y = m_speed;

		double dist = Math.hypot(delta_x, delta_y);

		//assicura che l'ultimo passo porti a zero la distanza
		if (Math.abs(delta_x)<speed_x)
			speed_x = Math.abs(delta_x);
		if (Math.abs(delta_y)<speed_y)
			speed_y = Math.abs(delta_y);

        int new_pos_x = (int) (m_model.get_x() + (Math.signum(delta_x) * speed_x));
        int new_pos_y = (int) (m_model.get_y() + (Math.signum(delta_y) * speed_y));

		//calcola la direzione
		int new_a = 0;
		if(delta_x!=0 || delta_y!=0){
			double rad_a = Math.atan2(delta_y, delta_x);
			new_a = (int) Math.toDegrees(rad_a) + 90;

            m_model.set_heading(new_a);
        }
        m_model.set_x(new_pos_x);
        m_model.set_y(new_pos_y);

        int new_z = m_model.get_z();
        if(dist>50){
            new_z = m_model.get_z() + m_speed_z;
            if (new_z>50)
				new_z = 50;
		}
		else if (dist<50){
            new_z = m_model.get_z() - m_speed_z;
            if (m_model.get_z() <= 0)
                new_z = 0;
		}
        m_model.set_z(new_z);

        //if distance reach zero set the next state to Landed
        if (dist==0){
            nextState = Landed.getInstance();
            ((FlyBaseState) nextState).enterState(m_model);
        }
        //Log.d(TAG, "head="+m_flyModel.get_heading());
    }


    @Override
    public synchronized EntityStateMachine nextState() {
        if (((FlyStatus) m_model).get_sugar() <= 0) {
            nextState = Dead.getInstance();
            ((FlyBaseState) nextState).enterState(m_model);
        }
        return nextState;
    }
}
