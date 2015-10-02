package flyonthewall.fly.sm;

import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import flyonthewall.base.Entity;
import flyonthewall.fly.FlyStatus;
import flyonthewall.fly.Physic;

/**
 * This Class provides a standard interface for Fly behaviours based on its state.
 * Each concrete state must reimplement behaviours based on the associated "strategy"
 */
//TODO: move to base package
public abstract class FlySM {
    //only for traces
    public String m_name = "";
    protected int m_sugarConsumeSpeed = 0;
    protected FlyStatus mFlyStatus = null;
    //retains the next status
    protected FlySM nextState = null;
    protected Entity m_food = null;
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

    public abstract void enterState(FlyStatus status);

    public abstract void exitState();

    public abstract void updatePosition();

    /**
     * Specific action sequence to be performed during an update cycle is state dependant
     *
     */
    public abstract void update();

    public FlySM updateAndGoToNext() {
        synchronized (mFlyStatus) {
            update();
        }
        return nextState();
    }

    public void manageCollision(HashMap<String, Entity> details) {
        Collection<Entity> entities = details.values();
        Iterator<Entity> entity_it = entities.iterator();
        Entity first = entity_it.next();

        //this entity name must be get from the model
        if (first.getName() == mFlyStatus.get_ename()) {
            while (entity_it.hasNext()) {
                Entity e = entity_it.next();
                Log.d("Fly StateMachine", "collision with: " + e.getName() + " (" + e.getType() + ")");
            }
        }
    }

	public void consumeSugar() {
        mFlyStatus.set_sugar(mFlyStatus.get_sugar() - m_sugarConsumeSpeed);
    }

    public synchronized FlySM nextState() {
        return nextState;
	}


    public Entity get_food() {
        return m_food;
    }

    public void set_food(Entity m_food) {
        this.m_food = m_food;
    }
}
