package flyonthewall.fly.sm;

import android.util.Log;

import FlyOnTheWall.pkg.R;
import flyonthewall.fly.FlyStatus;

public class Dead extends FlySM {

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
    public void enterState(FlyStatus status) {
		mFlyStatus = status;
        //copy state configuration to model
        mFlyStatus.setFrameDrwableId(mDrawableId);
		mFlyStatus.setM_currentFrame(0);
		mFlyStatus.set_mCurrStatusName("dead");
		mFlyStatus.set_z(0);
        mFlyStatus.set_heading(0);

		m_speed = 0;
		m_speed_z = 0;
		m_sugarConsumeSpeed=0;
        nextState = this;
        Log.d(TAG, "Entering state:" + mFlyStatus.get_mCurrStatusName());

        //dispatch death event
    }

	@Override
	public void exitState() {
		// TODO Auto-generated method stub

	}

	@Override
    public void updatePosition() {
    }

}
