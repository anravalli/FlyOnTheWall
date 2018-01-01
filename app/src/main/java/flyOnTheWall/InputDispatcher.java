package flyOnTheWall;

import android.view.MotionEvent;

import java.util.Collection;
import java.util.HashMap;

import flyOnTheWall.base.OnTouchCallback;

/**
 * Created by andrea on 17/09/15.
 */
public class InputDispatcher {
    private static InputDispatcher mInputDispatcher = null;

    HashMap<String, OnTouchCallback> mTouchEventRecipient = new HashMap<String, OnTouchCallback>();

    public static InputDispatcher getInputDispatcher() {
        if (mInputDispatcher == null) {
            mInputDispatcher = new InputDispatcher();
        }
        return mInputDispatcher;
    }

    public void registerToTouchEvent(String name, OnTouchCallback callback) {
        if (mTouchEventRecipient.get(name) == null) {
            mTouchEventRecipient.put(name, callback);
        }
    }

    public void unregisterToTouchEvent(String name) {
        if (mTouchEventRecipient.get(name) != null) {
            mTouchEventRecipient.remove(name);
        }
    }

    public void dispatchMotionEvent(MotionEvent e) {
        Collection<OnTouchCallback> touchRecipients = mTouchEventRecipient.values();
        for (OnTouchCallback callback : touchRecipients) {
            callback.onTouch(e);
        }
    }

}
