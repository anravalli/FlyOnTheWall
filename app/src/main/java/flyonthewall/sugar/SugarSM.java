package flyonthewall.sugar;

import android.util.Log;

import java.util.HashMap;

import flyonthewall.base.Entity;

/**
 * Created by andrea on 27/09/15.
 */
public class SugarSM {
    private static final String TAG = SugarSM.class.getSimpleName();

    private static SugarSM mInstance = null;

    private final String m_name = "SugarSM";

    public static SugarSM getInstance() {

        if (mInstance == null) {
            mInstance = new SugarSM();
        }
        Log.d(TAG, "instance state: " + mInstance + "(name: " + mInstance.m_name + ")");
        return mInstance;
    }

    public void manageCollision(HashMap<String, Entity> details) {

    }
}
