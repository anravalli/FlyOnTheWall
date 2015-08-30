package flyonthewall.pkg.fly;

import FlyOnTheWall.pkg.R;
import android.util.Log;

public class Landed extends StateMachine {
	private static final String TAG = Landed.class.getSimpleName();
	private static Landed mInstance = null;

	private Landed(){
		mDrawableId = R.drawable.fly;
		m_speed = 0;
		m_speed_z = 0;

		nextState = this;

		m_name = "landed";
	}

	public int getNextFrame() {
		// TODO Auto-generated method stub
		return mDrawableId;
	}

	public void enterState(FlyStatus status) {
		mFlyStatus = status;
		mFlyStatus.setFrameDrwableId(mDrawableId);
		mFlyStatus.setM_currentFrame(0);
		mFlyStatus.set_mCurrStatusName("landed");
		mFlyStatus.set_z(0);
		m_speed = 0;
		m_speed_z = 0;
		//m_sugarConsumeSpeed=0.5;
		m_sugarConsumeSpeed=0;
		Log.d(TAG, "Entering state:" + mFlyStatus.get_mCurrStatusName());
	}

	public void exitState() {
		// TODO Auto-generated method stub
		
	}

	public void updatePosition(int dest_x, int dest_y) {
		
		int delta_x = (int) (dest_x - mFlyStatus.get_x());
		int delta_y = (int) (dest_y - mFlyStatus.get_y());
		double dist = Math.hypot(delta_x, delta_y);
		
		int new_a = 0;
		
		if(delta_x!=0 || delta_y!=0){
			double rad_a = Math.atan2(delta_y, delta_x);
			new_a = (int) Math.toDegrees(rad_a) + 90;

			//int delta_a = new_a - mFlyStatus.get_heading();
			mFlyStatus.set_heading(new_a);
		}
	
		if (mFlyStatus.get_z()>5)
			mFlyStatus.set_z(mFlyStatus.get_z()-5);
		else if (mFlyStatus.get_z()>0)
			mFlyStatus.set_z(0);
		
		if (dist>0 && dist<10){
			nextState=Walking.getInstance();
			nextState.enterState(mFlyStatus);
		}

	}

	public static Landed getInstance() {
		//synchronized (mInstance) {
			if (mInstance == null){
				mInstance = new Landed();
			}
			Log.d(TAG, "instance state: " + mInstance + "(name: "+ mInstance.m_name + ")");	
		//}
		return mInstance;
	}

//	@Override
//	void consumeSugar() {
//		// TODO Auto-generated method stub
//		
//	}

	
}
