package flyonthewall.fly.sm;

import java.util.HashMap;

import flyonthewall.base.Entity;
import flyonthewall.fly.FlyStatus;
import flyonthewall.fly.Physic;

/**
 * This Class provides a standard interface for Fly behaviours based on its state.
 * Each concrete state must reimplement behaviours based on the associated "strategy"
 */

public abstract class FlySM {
    //only for traces
    public String m_name = "";
    protected int m_sugarConsumeSpeed = 0;
    protected FlyStatus mFlyStatus = null;
    //retains the next status
    protected FlySM nextState = null;
    //this should be part of the "strategy"
	Physic mPhysic = null;
    //state configuration they can stay here (and maybe updated in the model)
    int mDrawableId = 0;
	int m_time_to_change = 0;
	int m_speed = 0;
	int m_speed_z = 0;
	int m_rot_speed = 0;

    /*protected FlySM(){
        nextState = this;
    }*/

    /**
     * Specific action sequence to be performed during an update cycle is state dependant
     *
     * @return FlySM
     */
    public abstract void update();

    public FlySM updateAndGoToNext() {
        synchronized (mFlyStatus) {
            update();
        }
        return nextState();
    }

    public void manageCollision(HashMap<String, Entity> details) {

    }

    public abstract void enterState(FlyStatus status);

    public abstract void exitState();

	public void consumeSugar() {
		mFlyStatus.set_mSugar(mFlyStatus.get_mSugar()-m_sugarConsumeSpeed);
	}

    public synchronized FlySM nextState() {
        return nextState;
	}

    public abstract void updatePosition();
}
