package flyOnTheWall.base;

import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import flyOnTheWall.fly.Physic;

/**
 * This Class provides a standard interface for Fly behaviours based on its state.
 * Each concrete state must reimplement behaviours based on the associated "strategy"
 */
//TODO: move to base package
public abstract class EntityStateMachine {

    //only for traces
    protected String m_name = "";
    protected int m_sugarConsumeSpeed = 0;
    protected EntityModel m_model = null;
    //retains the next status
    protected EntityStateMachine nextState = null;
    protected Entity m_food = null;
    //state configuration they can stay here (and maybe updated in the model)
    protected int mDrawableId = 0;
    protected int m_time_to_change = 0;
    protected int m_speed = 0;
    protected int m_speed_z = 0;
    protected int m_rot_speed = 0;
    //this should be part of the "strategy"
	Physic mPhysic = null;

    public int getM_speed() {
        return m_speed;
    }

    /*protected EntityStateMachine(){
        nextState = this;
    }*/

    public abstract void enterState(EntityModel status);

    public abstract void exitState();

    public abstract void updatePosition();

    /**
     * Specific action sequence to be performed during an update cycle is state dependant
     *
     */
    public abstract void update();

    public EntityStateMachine updateAndGoToNext() {
        synchronized (this.m_model) {
            update();
        }
        return nextState();
    }

    public void manageCollision(HashMap<String, Entity> details) {
        Collection<Entity> entities = details.values();
        Iterator<Entity> entity_it = entities.iterator();
        Entity first = entity_it.next();

        //this entity name must be get from the model
        //TODO: only the first entity is responsible of the collision? it can't work
        if (first.getName() == m_model.get_ename()) {
            while (entity_it.hasNext()) {
                Entity e = entity_it.next();
                Log.d("Fly StateMachine", "collision with: " + e.getName() + " (" + e.getType() + ")");
            }
        }
    }

    public synchronized EntityStateMachine nextState() {
        return nextState;
	}


    public Entity get_food() {
        return m_food;
    }

    public void set_food(Entity m_food) {
        this.m_food = m_food;
    }

    public String get_name() {
        return m_name;
    }
}
