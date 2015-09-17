package flyonthewall.fly;

//import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

import flyonthewall.InputDispatcher;
import flyonthewall.base.OnTouchCallback;
import flyonthewall.fly.sm.Dead;
import flyonthewall.fly.sm.Flight;
import flyonthewall.fly.sm.FlySM;
import flyonthewall.fly.sm.Landed;
import flyonthewall.fly.sm.Walking;

public class Fly {
	
	private static final String TAG = Fly.class.getSimpleName();
    private final FlyView m_flyView;
    private final FlySugarView m_flySugarLevel;


	private FlyStatus mFlyStatus;
    private FlySM mFlyState;

    private boolean running;

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	//private long sleepTime;
	
	private int m_dest_x;
	private int m_dest_y;
	//private float dest_a;
	
	//spostare la macchina a stati sul model
	
	public Fly()
	{
		Log.d(TAG, "Get a new Fly!");
		
		mFlyStatus = new FlyStatus(200,200,50,0,0);
		m_dest_x = 200;
		m_dest_y = 200;
		mFlyStatus.set_z(0);
		//mFlyState = new WalkingStatus();
		mFlyState = Landed.getInstance();
		mFlyState.enterState(mFlyStatus);

        this.m_flyView = new FlyView();
        this.m_flyView.setFlyModel(this.get_mFlyStatus());

        this.m_flySugarLevel = new FlySugarView();
        this.m_flySugarLevel.setFlyModel(this.get_mFlyStatus());

        registerToEvent();

        Log.d(TAG, "Get a new Fly!");
	}

	public void update() {
		synchronized (mFlyState) {
			mFlyState = mFlyState.nextState();
			mFlyState.updatePosition(m_dest_x, m_dest_y);
			mFlyState.consumeSugar();
			if(mFlyStatus.get_mSugar() <= 0 && !mFlyStatus.get_mCurrStatusName().equals("walking")){
				mFlyState = Dead.getInstance();
				mFlyState.enterState(mFlyStatus);
			}

		}
	}

	public void setDestinationPoint(int x, int y){
		m_dest_x = x;
		m_dest_y = y;
	}

    public void forcePosition(int x, int y){
		mFlyStatus.set_x(x);
		mFlyStatus.set_y(y);
		m_dest_x = x;
		m_dest_y = y;
	}

    public String switchState(){
		synchronized (mFlyState) {
			if(mFlyStatus.get_mCurrStatusName().equals("walking"))
                mFlyState = Flight.getInstance();
            else if(mFlyStatus.get_mCurrStatusName().equals("landed"))
				mFlyState = Walking.getInstance();
			else if(mFlyStatus.get_mCurrStatusName().equals("flying"))
				mFlyState = Landed.getInstance();
			//else if(mFlyStatus.get_mCurrStatusName().equals("dead"))
			//	mFlyState = Dead.getInstance();

            mFlyState.enterState(mFlyStatus);
		}
		return mFlyStatus.get_mCurrStatusName();
	}

    void registerToEvent() {
        InputDispatcher.getInputDispatcher().registerToTouchEvent("fly", new OnTouchCallback() {
            public void onTouch(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.d(TAG, "down event x: " + event.getX() + ", Y:" + event.getY());
                    setDestinationPoint((int) event.getX(), (int) event.getY());
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    Log.d(TAG, "move event x: " + event.getX() + ", Y:" + event.getY());
                    setDestinationPoint((int) event.getX(), (int) event.getY());
                }
            }
        });
    }

    public FlyStatus get_mFlyStatus() {
        return mFlyStatus;
    }

    public void set_mFlyStatus(FlyStatus mFlyStatus) {
        this.mFlyStatus = mFlyStatus;
    }

}
