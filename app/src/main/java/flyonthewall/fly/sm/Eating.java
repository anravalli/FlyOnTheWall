package flyonthewall.fly.sm;

import android.util.Log;

import FlyOnTheWall.pkg.R;
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
        //m_flyModel.set_mCurrStatusName(m_name);
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
    public void enterState(FlyStatus status) {
        super.enterState(status);
        m_flyModel = status;
        m_flyModel.set_mCurrStatusName(m_name);
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
        if (m_food == null) {
            Log.w(TAG, "no food set!!!");
            return;
        }
        int sugar_tx = ((Sugar) m_food).consumeSugar();
        if (sugar_tx != 0 && m_flyModel.get_sugar() <= m_flyModel.get_max_sugar()) {
            m_flyModel.set_sugar(m_flyModel.get_sugar() + sugar_tx);
        } else {
            nextState = Landed.getInstance();
            nextState.enterState(m_flyModel);
            m_food = null;
        }
        return;
    }

}
