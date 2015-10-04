package flyonthewall.fly;

//import android.graphics.Rect;

import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import flyonthewall.GameMsgDispatcher;
import flyonthewall.InputDispatcher;
import flyonthewall.base.Entity;
import flyonthewall.base.EntityStateMachine;
import flyonthewall.base.EntityType;
import flyonthewall.base.msg.GameMessage;
import flyonthewall.dbg.SensibleAreaMark;
import flyonthewall.fly.sm.Flight;
import flyonthewall.fly.sm.Landed;
import flyonthewall.fly.sm.Walking;

public class Fly extends Entity {

    private static final String TAG = Fly.class.getSimpleName();
    private final FlyView m_flyView;
    private final FlySugarView m_flySugarLevel;

	private FlyStatus mFlyStatus;
    private EntityStateMachine mFlyState;

	//private long sleepTime;


    private int mSensitivity = 20;
    //private float dest_a;

	public Fly()
	{
        super("fly", EntityType.Fly);

		Log.d(TAG, "Get a new Fly!");

        register();

        mFlyStatus = new FlyStatus(name, 200, 200, 50, 0, 0);
        mFlyStatus.set_z(0);

		mFlyState = Landed.getInstance();
		mFlyState.enterState(mFlyStatus);

        this.m_flyView = new FlyView(mFlyStatus);
        //this.m_flyView.setFlyModel(this.get_mFlyStatus());

        this.m_flySugarLevel = new FlySugarView();
        this.m_flySugarLevel.setFlyModel(this.get_mFlyStatus());

        SensibleAreaMark.getMarker().setSensitivity(mSensitivity);
        SensibleAreaMark.getMarker().setFlyView(m_flyView);

        registerToEvent();
        registerToMessages();

	}

    @Override
    protected void fetchMessage(GameMessage msg) {
        switch (msg.type) {
            case GameExiting:
                GameMsgDispatcher.getMessageDispatcher().unregisterToGameMessages(name);
                InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
                unregister();
                break;
            case CollisionDetected:
                if (msg.details == null) {
                    Log.w(TAG, "Collision detected but entity list is NULL!!!");
                    break;
                }
                if (msg.details.get(name) != null) {
                    mFlyState.manageCollision(msg.details);
                }
                break;
            default:
                /* no-op */
                break;
        }
    }

    @Override
    public synchronized void update() {
        //update strategy implemented by the state machine
        mFlyState = mFlyState.updateAndGoToNext();
        bounding_box = m_flyView.getBoundingBox(-5);
    }

    public void forcePosition(int x, int y){
		mFlyStatus.set_x(x);
		mFlyStatus.set_y(y);
        mFlyStatus.set_dest_x(x);
        mFlyStatus.set_dest_y(y);
    }

    public String switchState(){
        Log.d(TAG, "--- switchState ---");
        synchronized (mFlyState) {
			if(mFlyStatus.get_mCurrStatusName().equals("walking"))
                mFlyState = Flight.getInstance();
            else if(mFlyStatus.get_mCurrStatusName().equals("landed"))
				mFlyState = Walking.getInstance();
			else if(mFlyStatus.get_mCurrStatusName().equals("flying"))
				mFlyState = Landed.getInstance();

            mFlyState.enterState(mFlyStatus);
		}
        Log.d(TAG, "--- new status: " + mFlyStatus.get_mCurrStatusName());
        return mFlyStatus.get_mCurrStatusName();
	}


    @Override
    protected void onTouchEvent(MotionEvent event) {
        //Log.d(TAG, "down event x: " + event.getX() + ", Y:" + event.getY());
        //Log.d(TAG, "move event x: " + event.getX() + ", Y:" + event.getY());

        Rect sensibleArea = m_flyView.getSensitiveArea(mSensitivity);
        //Log.d(TAG, "allowedArea X " + sensibleArea.left + " (w: " + sensibleArea.width() + " )");
        //Log.d(TAG, "allowedArea y " + sensibleArea.top + " (w: " + sensibleArea.height() + " )");

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (sensibleArea.contains((int) event.getX(), (int) event.getY())) {
                Log.d(TAG, "...switch state!");
                switchState();
            } else {
                mFlyStatus.set_dest_x((int) event.getX());
                mFlyStatus.set_dest_y((int) event.getY());
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            mFlyStatus.set_dest_x((int) event.getX());
            mFlyStatus.set_dest_y((int) event.getY());
        }

    }

    public FlyStatus get_mFlyStatus() {
        return mFlyStatus;
    }
}
