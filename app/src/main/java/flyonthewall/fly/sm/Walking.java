package flyonthewall.fly.sm;

import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import FlyOnTheWall.pkg.R;
import flyonthewall.base.Entity;
import flyonthewall.base.EntityModel;
import flyonthewall.base.EntityStateMachine;
import flyonthewall.fly.FlyStatus;

public class Walking extends FlyBaseState {

	private static final String TAG = Walking.class.getSimpleName();
	
	private static Walking mInstance = null;
	
	private Walking()
	{
		mDrawableId = R.drawable.fly;
		m_speed = 5;
		m_rot_speed = 1;
        m_name = "walking";
		m_sugarConsumeSpeed=1;
	}

    static public Walking getInstance() {
        if (mInstance == null) {
            mInstance = new Walking();
        }
        Log.d(TAG, "instance state: " + mInstance + "(name: " + mInstance.m_name + ")");
        return mInstance;
    }

    @Override
    public void update() {
        updatePosition();
        consumeSugar();
        return;
    }

    @Override
    public void enterState(EntityModel status) {
        super.enterState(status);
        m_model = status;
        //copy state configuration to model
        m_model.set_spriteId(mDrawableId);
        m_model.set_mCurrStatusName(m_name);
        m_model.set_z(0);
        nextState = this;
		m_speed = 5;
		m_rot_speed = 1;
		m_sugarConsumeSpeed=1;

        Log.d(TAG, "Entering state:" + m_model.get_mCurrStatusName());
        return;
    }

    @Override
    public void exitState() {

	}

	@Override
    public void updatePosition() {
        int dest_x = ((FlyStatus) m_model).get_dest_x();
        int dest_y = ((FlyStatus) m_model).get_dest_y();
        int delta_x = (int) (dest_x - m_model.get_x());
        int delta_y = (int) (dest_y - m_model.get_y());

		int new_a = 0;

		if(delta_x!=0 || delta_y!=0){
			double rad_a = Math.atan2(delta_y, delta_x);
			new_a = (int) Math.toDegrees(rad_a) + 90;

            m_model.set_heading(new_a);
        }

		int speed_x = (int) (m_speed*Math.cos(new_a));
		int speed_y = (int) (m_speed*Math.sin(new_a));

        if (Math.abs(delta_y) < Math.abs(speed_y))
            speed_y = delta_y;
        if (Math.abs(delta_x) < Math.abs(speed_x))
            speed_x = delta_x;

        int new_pos_x = m_model.get_x() + speed_x;
        int new_pos_y = m_model.get_y() + speed_y;


        m_model.set_x(new_pos_x);
        m_model.set_y(new_pos_y);

        //da rivedere: bisogna aggiornare solo la Z non la scala
        if (m_model.get_z() > 0)
            m_model.set_z(m_model.get_z() - 1);


        if (delta_x==0 && delta_y==0){
			nextState= Landed.getInstance();
            ((FlyBaseState) nextState).enterState(m_model);
        }

	}

    @Override
    public EntityStateMachine nextState() {
        nextState = super.nextState();
        if (((FlyStatus) m_model).get_sugar() <= 0) {
            nextState = Dead.getInstance();
            ((FlyBaseState) nextState).enterState(m_model);
        }
        return nextState;
    }

    @Override
    public void manageCollision(HashMap<String, Entity> details) {
        Collection<Entity> entities = details.values();
        Iterator<Entity> entity_it = entities.iterator();

        boolean flyInvolved = false;
        boolean sugarInvolved = false;
        boolean shoeInvolved = false;
        Entity food = null;
        while (entity_it.hasNext()) {
            Entity e = entity_it.next();
            switch (e.getType()) {
                case Shoe:
                    shoeInvolved = true;
                    break;
                case Fly:
                    flyInvolved = true;
                    break;
                case Sugar:
                    sugarInvolved = true;
                    //last food entity found is eaten first
                    food = e;
                    break;
            }
            Log.d("Fly StateMachine", "collision with: " + e.getName() + " (" + e.getType() + ")");
        }
        if (flyInvolved) {
            if (shoeInvolved) {
                //let's die!
                nextState = Dead.getInstance();
                ((FlyBaseState) nextState).enterState(m_model);
                return;
            }
            if (sugarInvolved && ((FlyStatus) m_model).get_sugar() >= ((FlyStatus) m_model).get_max_sugar()) {
                nextState = Landed.getInstance();
                ((FlyBaseState) nextState).enterState(m_model);
            } else if (sugarInvolved) {
                //let's eat!
                nextState = Eating.getInstance();
                ((FlyBaseState) nextState).enterState(m_model);
                nextState.set_food(food);
            }
        }
    }

}
