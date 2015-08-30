package flyonthewall.pkg.fly;

import android.util.Log;
import FlyOnTheWall.pkg.R;

public class Walking extends StateMachine {

	private static final String TAG = Walking.class.getSimpleName();
	
	private static Walking mInstance = null;
	//private FlyView theFly;
	
	private Walking()
	{
		mDrawableId = R.drawable.fly;
		m_speed = 5;
		m_rot_speed = 1;
		nextState = this;
		m_name = "walking";
		m_sugarConsumeSpeed=1;
	}
	

	public int getNextFrame() {
		// TODO Auto-generated method stub
		return mDrawableId;
	}

	public void enterState(FlyStatus status) {
		mFlyStatus = status;
		mFlyStatus.setFrameDrwableId(mDrawableId);
		mFlyStatus.setM_currentFrame(0);
		mFlyStatus.set_mCurrStatusName("walking");
		mFlyStatus.set_z(0);
		nextState = this;
		m_speed = 5;
		m_rot_speed = 1;
		m_sugarConsumeSpeed=1;

		Log.d(TAG, "Entering state:" + mFlyStatus.get_mCurrStatusName());
	}

	public void exitState() {
		
	}

	@Override
	public void updatePosition(int dest_x, int dest_y) {
		
		int delta_x = (int) (dest_x - mFlyStatus.get_x());
		int delta_y = (int) (dest_y - mFlyStatus.get_y());
		//int speed = 5;
		
		int new_a = 0;
		
		if(delta_x!=0 || delta_y!=0){
			double rad_a = Math.atan2(delta_y, delta_x);
			new_a = (int) Math.toDegrees(rad_a) + 90;

			//int delta_a = new_a - mFlyStatus.get_heading();
			mFlyStatus.set_heading(new_a);
		}
		
		int speed_x = (int) (m_speed*Math.cos(new_a));
		int speed_y = (int) (m_speed*Math.sin(new_a));
		
		if (Math.abs(delta_y)<speed_y)
			speed_y = Math.abs(delta_y);
		if (Math.abs(delta_x)<speed_x)
			speed_x = Math.abs(delta_x);
		
		int new_pos_x = mFlyStatus.get_x()+speed_x;
		int new_pos_y = mFlyStatus.get_y()+speed_y;
		
		
		mFlyStatus.set_x(new_pos_x);
		mFlyStatus.set_y(new_pos_y);
		//mFlyStatus.set_x(dest_x);
		//mFlyStatus.set_y(dest_y);
		
		//da rivedere: bisogna aggiornare solo la Z non la scala 
		if(mFlyStatus.get_z()>0)
			mFlyStatus.set_z(mFlyStatus.get_z()-1);
		
		if (delta_x==0 && delta_y==0){
			nextState= Landed.getInstance();
			nextState.enterState(mFlyStatus);
		}

	}

	static public Walking getInstance() {
		//synchronized (mInstance) {
			if (mInstance == null){
				mInstance = new Walking();
			}
			Log.d(TAG, "instance state: " + mInstance + "(name: "+ mInstance.m_name + ")");
		//}
		return mInstance;
	}


//	@Override
//	void consumeSugar() {
//		mFlyStatus.set_mSugar(mFlyStatus.get_mSugar()-m_sugarConsumeSpeed);
//		
//	}

}
