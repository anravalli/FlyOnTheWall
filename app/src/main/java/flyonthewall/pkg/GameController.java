package flyonthewall.pkg;

import flyonthewall.pkg.fly.Fly;
import flyonthewall.pkg.fly.FlyStatus;
import flyonthewall.pkg.fly.FlySugarView;
import flyonthewall.pkg.fly.FlyView;
import flyonthewall.pkg.fly.TouchMark;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameController extends Thread {
	//controller class for the whole game
	//it holds also the game mail loop

	private static final String TAG = GameController.class.getSimpleName();
	
	long sleepTime=0;
	Canvas mCanvas=null;
	private SurfaceHolder surfaceHolder=null;
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
	
	public void setGameView(GameView view) {
		this.m_gameview = view;
		this.surfaceHolder = m_gameview.getHolder();
		this.sleepTime = 50;//100;
		
		this.m_flyView = new FlyView(m_gameview);
		this.m_flyView.setFlyModel(m_fly.get_mFlyStatus());
		
		this.m_flySugarLevel = new FlySugarView(m_gameview);
		this.m_flySugarLevel.setFlyModel(m_fly.get_mFlyStatus());
	}

	public GameController() {
		super();
		//setGameView(new GameView(this, ctrl));
		m_fly = new Fly();

        mTimeReference = System.currentTimeMillis();

		reset();
		Log.d(TAG, "38");
	}
	
	public void onTouch(float x, float y) {
		//check collision
		//play sound
		m_fly.setDestinationPoint((int)x, (int)y);
		TouchMark.getMarker().setTouch(x,y);
	}

	@Override
	public void run(){

		Log.d(TAG, "Starting game loop");
		
		//game main loop
		while (running) {
			mCanvas = null;

            //TODO frame rate needs to be regulated
            //get current time
            long cur_time = System.currentTimeMillis();
            //compare current time with stored time (previous cycle)
            long delta = cur_time - mTimeReference;
            //if the delta is less than 1/30 secs then sets sleeptime to
            //Log.println(Log.DEBUG, TAG, msg);
            if (delta <= mFrameRate){
                // ---> sleeptime = 1/30 - delta(secs)
                sleepTime = mFrameRate - delta;
                String msg = "setting the sleeptime to " + sleepTime + " (fr: " + mFrameRate + ", delta: " + delta + ")";
                Log.println(Log.DEBUG, TAG, msg);
            }
            else {
                sleepTime = 0;
                String msg = "delta is lower than 0: sleeptime " + sleepTime + " (fr: " + mFrameRate + ", delta: " + delta + ")";
                Log.println(Log.DEBUG, TAG, msg);
            }
            mTimeReference = cur_time;

			if(m_gameview==null)
				continue;
			if(!m_gameview.isCreated()){
				try {
					Log.d(TAG, "View not yet created: skip");
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					
				}
				continue;
			}
			try {
				//draw the canvas (view) in a synchronized way
				mCanvas = this.m_gameview.getHolder().lockCanvas();
				//lock mutex
				synchronized (surfaceHolder) {
					m_gameview.setCanva(mCanvas);
					try{
						if (m_fly==null){
							Log.d(TAG, "fly is null!!");
							System.exit(-100);
						}
						m_gameview.update();
						m_fly.update();
						//chiamate dirette a draw?
						// il game controller dovrebbe solo chiamare gli update
						// sulle entità istanziate nel stato corrente!
						m_flyView.draw(mCanvas);
						m_flySugarLevel.draw(mCanvas);
						TouchMark.getMarker().draw(mCanvas);
					}catch(Exception e){
						Log.d(TAG, "exception!!" + e);
					}
					

					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {}
				}
			}
			finally {
				if (mCanvas != null) {
					this.m_gameview.getHolder().unlockCanvasAndPost(mCanvas);
				}
			}	// end finally
		}
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
	/*public void setfly(Fly m_fly) {
		this.m_fly = m_fly;
	}*/

	public void reset() {
		m_fly.forcePosition(200, 200);
	}

	public String switchState() {
		
		return m_fly.switchState();
	}

}
