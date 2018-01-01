package flyonthewall.fly.sm;

import android.util.Log;

import FlyOnTheWall.pkg.R;
import flyonthewall.base.EntityModel;

public class Dead extends FlyBaseState {

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
    public void enterState(EntityModel status) {
        super.enterState(status);
        m_model = status;
        //copy state configuration to model
        m_model.set_spriteId(mDrawableId);
        m_model.set_mCurrStatusName(m_name);
        m_model.set_z(0);
        m_model.set_heading(0);

		m_speed = 0;
		m_speed_z = 0;
		m_sugarConsumeSpeed=0;
        nextState = this;
        Log.d(TAG, "Entering state:" + m_model.get_mCurrStatusName());
    }

	@Override
	public void exitState() {
		// TODO Auto-generated method stub

	}

	@Override
    public void updatePosition() {
    }

}
