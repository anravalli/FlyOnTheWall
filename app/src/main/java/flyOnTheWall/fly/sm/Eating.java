package flyonthewall.fly.sm;

import android.util.Log;

import FlyOnTheWall.pkg.R;
import flyonthewall.base.EntityModel;
import flyonthewall.fly.FlyStatus;
import flyonthewall.sugar.Sugar;

/**
 * Created by andrea on 02/10/15.
 */
public class Eating extends FlyBaseState {

    private static final String TAG = Eating.class.getSimpleName();

    private static Eating mInstance = null;

    private Eating() {
        mDrawableId = R.drawable.fly;
        nextState = this;
        m_name = "eating";
        m_sugarConsumeSpeed = 0;
    }

    static public Eating getInstance() {
        if (mInstance == null) {
            mInstance = new Eating();
        }
        Log.d(TAG, "instance state: " + mInstance + "(name: " + mInstance.m_name + ")");
        return mInstance;
    }

    @Override
    public void enterState(EntityModel status) {
        super.enterState(status);
        m_model = status;
        m_model.set_mCurrStatusName(m_name);
        nextState = this;
        m_sugarConsumeSpeed = 0;
    }

    @Override
    public void exitState() {
        return;
    }

    @Override
    public void updatePosition() {
        return;
    }

    @Override
    public void update() {
        updatePosition();
        FlyStatus m = (FlyStatus) m_model;
        if (m_food == null) {
            Log.w(TAG, "no food set!!!");
            return;
        }
        int sugar_tx = ((Sugar) m_food).consumeSugar();
        if (sugar_tx != 0 && m.get_sugar() <= m.get_max_sugar()) {
            m.set_sugar(m.get_sugar() + sugar_tx);
        } else {
            nextState = Landed.getInstance();
            nextState.enterState(m_model);
            m_food = null;
        }
        return;
    }

}
