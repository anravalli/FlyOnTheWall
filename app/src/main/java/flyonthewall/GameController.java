package flyonthewall;

import flyonthewall.fly.Fly;
import flyonthewall.fly.FlyStatus;
import flyonthewall.fly.FlySugarView;
import flyonthewall.fly.FlyView;
import flyonthewall.dbg.TouchMark;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameController extends Thread {
	//controller class for the whole game
    //it holds also the game main loop

	private static final String TAG = GameController.class.getSimpleName();

    /**
     * delay time for surface setup
     */
    private final long mSetupDelay = 50;

    //Canvas mCanvas=null;

	private GameView m_gameview=null;

	// flag to hold game state 
	private boolean running;

	private Fly m_fly;
	private FlyView m_flyView;
	private FlySugarView m_flySugarLevel;

    //frame rate regulation
    private long mTimeReference = 0;
    private long mFrameRate = 1000/30;
	
	public void setRunning(boolean running) {
		this.running = running;
	}


	public GameController() {
		super();
        Log.d(TAG, "Creating the game controller");
        mTimeReference = System.currentTimeMillis();

    }

    void initGame() {
        m_fly = new Fly();
        reset();
	}

	@Override
	public void run(){

		Log.d(TAG, "Starting game loop");
		
		//game main loop
		while (running) {

            if (m_fly == null) {
                Log.d(TAG, "fly is null!!");
                System.exit(-100);
            }
            //
            try {
                int frameDelay = (int) calculateInterFrameDelay();
                Thread.sleep(frameDelay);
            } catch (InterruptedException e) {
            }

            m_fly.update();

            ViewManager.getViewManager().updateViews();
        }
    }

    private long calculateInterFrameDelay() {

        long frameDelay = 0;
        //get current time
        long cur_time = System.currentTimeMillis();
        //compare current time with stored time (previous cycle)
        long delta = cur_time - mTimeReference;

        //if the delta is less than 1/30 secs then sets sleeptime to
        if (delta <= mFrameRate) {
            // ---> sleeptime = 1/30 - delta(secs)
            frameDelay = mFrameRate - delta;
            String msg = "setting the sleeptime to " + frameDelay + " (fr: " + mFrameRate + ", delta: " + delta + ")";
            Log.println(Log.DEBUG, TAG, msg);
        } else {
            frameDelay = 0;
            String msg = "delta is lower than 0: sleeptime " + frameDelay + " (fr: " + mFrameRate + ", delta: " + delta + ")";
            Log.println(Log.DEBUG, TAG, msg);
        }
        mTimeReference = cur_time;
        return frameDelay;
    }

	public GameView getView() {
		return m_gameview;
	}

	public Fly getFly() {
		return m_fly;
	}

	public FlyStatus getFlyStatus() {
		return m_fly.get_mFlyStatus();
	}

	public void reset() {
		m_fly.forcePosition(200, 200);
	}

	public String switchState() {
		return m_fly.switchState();
	}

}
