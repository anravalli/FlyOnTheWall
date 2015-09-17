package flyonthewall.fly.sm;

import flyonthewall.fly.FlyStatus;
import flyonthewall.fly.Physic;

/**
 * This Class provides a standard interface for Fly behaviours based on its state.
 * Each concrete state must reimplement behaviours based on the associated "strategy"
 */

public class FlySM {

	//this should be part of the "strategy"
	Physic mPhysic = null;

	//TODO the following data must stay on the model!
	int mDrawableId = 0;
	int m_time_to_change = 0;
	int m_speed = 0;
	int m_speed_z = 0;
	int m_rot_speed = 0;
	public String m_name = "";
	protected int m_sugarConsumeSpeed = 0;

	protected FlyStatus mFlyStatus;
	protected FlySM nextState;

	private static FlySM mState = null;

	static public FlySM getInstance() {
		if (mState == null) {
			new FlySM();
		}
		return mState;
	}

	public void enterState(FlyStatus status) {
		return;
	}

	public void exitState() {
		return;
	}

	public void consumeSugar() {
		mFlyStatus.set_mSugar(mFlyStatus.get_mSugar()-m_sugarConsumeSpeed);
	}

	public FlySM nextState() {
		
		return nextState;
	}

	int getNextFrame() {
		return 0;
	}

	public void updatePosition(int dest_x, int dest_y) {
		return;
	}
}
