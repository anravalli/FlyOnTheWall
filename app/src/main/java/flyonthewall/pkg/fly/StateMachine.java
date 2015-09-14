package flyonthewall.pkg.fly;

import android.util.TimeUtils;

public abstract class StateMachine {
	
	Physic mPhysic = null;
	int mDrawableId = 0;
	int m_time_to_change = 0;
	int m_speed = 0;
	int m_speed_z = 0;
	int m_rot_speed = 0;
	public String m_name = "";
	protected int m_sugarConsumeSpeed = 0;

	protected FlyStatus mFlyStatus;
	protected StateMachine nextState;
	
	abstract void enterState(FlyStatus status);
	abstract void exitState();
	//abstract void consumeSugar();
	//abstract void eatSugar();
	
	void consumeSugar() {
		mFlyStatus.set_mSugar(mFlyStatus.get_mSugar()-m_sugarConsumeSpeed);
	}
	
	/* non si può fareeee!
	protected static StateMachine mInstance = null; 
	abstract public StateMachine getInstance();
	*/
	
	public StateMachine nextState() {
		
		return nextState;
	}
	
	abstract int getNextFrame();
	
	public abstract void updatePosition(int dest_x, int dest_y);
}
