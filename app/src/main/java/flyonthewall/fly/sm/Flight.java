package flyonthewall.fly.sm;

import android.util.Log;

import FlyOnTheWall.pkg.R;
import flyonthewall.fly.FlyStatus;

public class Flight extends FlySM {

	private static final String TAG = Flight.class.getSimpleName();

	private static Flight mInstance = null;
	
	private Flight()
	{
		mDrawableId = R.drawable.fly;
		m_speed = 10;
        m_speed_z = 10;
        m_rot_speed = 1;
        //nextState = this;
        m_name = "flight";
		m_sugarConsumeSpeed=2;
		
	}

    static public Flight getInstance() {
        if (mInstance == null) {
            mInstance = new Flight();
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
    public void enterState(FlyStatus status) {
		mFlyStatus = status;
        //copy state configuration to model
        mFlyStatus.set_spriteId(mDrawableId);
        mFlyStatus.setM_currentFrame(0);
		mFlyStatus.set_mCurrStatusName("flying");
		m_speed = 10;
        m_speed_z = 10;
        m_rot_speed = 1;
		m_sugarConsumeSpeed=2;
		nextState = this;
		Log.d(TAG, "Entering state:" + mFlyStatus.get_mCurrStatusName());

        return;
    }

    public void exitState() {
        //stub
    }

	@Override
    public void updatePosition() {
        int dest_x = mFlyStatus.get_dest_x();
        int dest_y = mFlyStatus.get_dest_y();
        ;
        int delta_x = (int) (dest_x - mFlyStatus.get_x());
        int delta_y = (int) (dest_y - mFlyStatus.get_y());
		int speed_x = m_speed;
		int speed_y = m_speed;

		double dist = Math.hypot(delta_x, delta_y);

		//assicura che l'ultimo passo porti a zero la distanza
		if (Math.abs(delta_x)<speed_x)
			speed_x = Math.abs(delta_x);
		if (Math.abs(delta_y)<speed_y)
			speed_y = Math.abs(delta_y);

		int new_pos_x = (int) (mFlyStatus.get_x()+(Math.signum(delta_x)*speed_x));
		int new_pos_y = (int) (mFlyStatus.get_y()+(Math.signum(delta_y)*speed_y));

		//calcola la direzione
		int new_a = 0;
		if(delta_x!=0 || delta_y!=0){
			double rad_a = Math.atan2(delta_y, delta_x);
			new_a = (int) Math.toDegrees(rad_a) + 90;

			mFlyStatus.set_heading(new_a);
		}
		mFlyStatus.set_x(new_pos_x);
		mFlyStatus.set_y(new_pos_y);

        int new_z = mFlyStatus.get_z();
		if(dist>50){
			new_z = mFlyStatus.get_z()+m_speed_z;
			if (new_z>50)
				new_z = 50;
		}
		else if (dist<50){
			new_z = mFlyStatus.get_z()-m_speed_z;
			if (mFlyStatus.get_z()<=0)
				new_z = 0;
		}
		mFlyStatus.set_z(new_z);

        //if distance reach zero set the next state to Landed
        if (dist==0){
            nextState = Landed.getInstance();
            nextState.enterState(mFlyStatus);
        }
        //Log.d(TAG, "head="+mFlyStatus.get_heading());
    }


    @Override
    public synchronized FlySM nextState() {
        //FlySM next_state = mInstance;
        if (mFlyStatus.get_sugar() <= 0) {
            nextState = Dead.getInstance();
            nextState.enterState(mFlyStatus);
        }
        return nextState;
    }
}
