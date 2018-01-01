package flyOnTheWall.sugar;

import android.util.Log;

import java.util.HashMap;

import flyOnTheWall.base.Entity;
import flyOnTheWall.base.EntityModel;
import flyOnTheWall.base.EntityStateMachine;

/**
 * Created by andrea on 27/09/15.
 */
public class SugarIdleState extends EntityStateMachine {
    private static final String TAG = SugarIdleState.class.getSimpleName();

    private static SugarIdleState mInstance = null;

    private final String m_name = "SugarIdleState";

    public static SugarIdleState getInstance() {

        if (mInstance == null) {
            mInstance = new SugarIdleState();
        }
        Log.d(TAG, "instance state: " + mInstance + "(name: " + mInstance.m_name + ")");
        return mInstance;
    }

    @Override
    public void enterState(EntityModel status) {
        m_model = status;
    }

    @Override
    public void exitState() {

    }

    @Override
    public void updatePosition() {

    }

    @Override
    public void update() {

    }

    @Override
    public void manageCollision(HashMap<String, Entity> details) {

    }
}
