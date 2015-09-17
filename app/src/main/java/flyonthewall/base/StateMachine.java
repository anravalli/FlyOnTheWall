package flyonthewall.base;

/**
 * Created by andrea on 16/09/15.
 * <p/>
 * This class define a generic state machine to be re-implemented by entities
 */
public class StateMachine {

    private static StateMachine mState = null;

    static public StateMachine getInstance() {
        if (mState == null) {
            new StateMachine();
        }
        return mState;
    }

}
