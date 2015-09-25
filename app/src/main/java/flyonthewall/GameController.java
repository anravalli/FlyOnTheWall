package flyonthewall;

import android.util.Log;
import android.view.MotionEvent;

import flyonthewall.base.OnTouchCallback;
import flyonthewall.base.msg.GameMessage;
import flyonthewall.base.msg.GameMessagesType;
import flyonthewall.base.msg.OnNewGameMessage;
import flyonthewall.fly.Fly;

public class GameController extends Thread {
    //controller class for the whole game
    //it holds also the game main loop

	private static final String TAG = GameController.class.getSimpleName();

    private final String name = "game_ctrl";

    // keep game state
    private GameStatus mStatus = GameStatus.stopped;
    private GameMsgDispatcher gameMsgDispatcher = null;
    private Fly m_fly;

    //frame rate regulation
    private long mTimeReference = 0;
    private long mFrameRate = 1000/30;

	public GameController() {
		super();
        Log.d(TAG, "Creating the game controller (" + this + ")");
        mTimeReference = System.currentTimeMillis();
        //register to inputs and game bus
        InputDispatcher.getInputDispatcher().registerToTouchEvent(name, new OnTouchCallback() {
            public void onTouch(MotionEvent event) {
                detectLongTouch(event);
            }
        });
        gameMsgDispatcher = GameMsgDispatcher.getMessageDispatcher();
        gameMsgDispatcher.registerToGameMessages(name, new OnNewGameMessage() {
            public void receiveMessage(GameMessage msg) {
                fetchMessage(msg);
            }
        });
    }

    public GameStatus getStatus() {
        return mStatus;
    }

    public void setRunning(boolean running) {
        synchronized (mStatus) {
            if (running)
                this.mStatus = GameStatus.running;
            else
                this.mStatus = GameStatus.stopped;
            Log.d(TAG, "Game status is changed to: " + this.mStatus);
        }
    }

    private void fetchMessage(GameMessage msg) {
        Log.d(TAG, "message received (type: " + msg.type + ")");
        switch (msg.type) {
            case BackPressed:
                onBackPressed();
                break;
            case GameResume:
                mStatus = GameStatus.running;
                break;
            case GameExiting:
                mStatus = GameStatus.exiting;
                break;
            default:
                //do nothing
                break;
        }
    }

    void initGame() {
        m_fly = new Fly();
        reset();
	}

    @Override
    public void run() {

		Log.d(TAG, "Starting game loop");
		
		//game main loop
        synchronized (mStatus) {
            while (mStatus != GameStatus.stopped) {

                if (m_fly == null) {
                    Log.e(TAG, "fly is null!!");
                    System.exit(-100);
                }
                //
                try {
                    int frameDelay = (int) calculateInterFrameDelay();
                    Thread.sleep(frameDelay);
                } catch (InterruptedException e) {
                }
                try {
                    switch (mStatus) {
                        case running:
                            EntityManager.getEntityManager().updateEntities();
                            ViewManager.getViewManager().updateViews();
                            break;
                        case paused:
                            //enter pause
                            //draw pause screen
                            ViewManager.getViewManager().updateViews();
                            break;
                        case exiting:
                            //do house cleanings
                            InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
                            ViewManager.getViewManager().cleanUp();
                            gameMsgDispatcher.dispatchMessage(new GameMessage(
                                    GameMessagesType.GameEnded, null));
                            gameMsgDispatcher.unregisterToGameMessages(name);
                            break;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "exception!!" + e);
                    e.printStackTrace();
                }
            }
            cleanUp();
            Log.i(TAG, "Game status changed to STOP");
            Log.d(TAG, "Thread status: " + this.getStatus());
        }

    }

    private void cleanUp() {
        //InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
        gameMsgDispatcher.unregisterToGameMessages(name);

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
            String msg = "setting the inter-frame delay to " + frameDelay + " (fr: " + mFrameRate + ", delta: " + delta + ")";
            //Log.println(Log.DEBUG, TAG, msg);
        } else {
            frameDelay = 0;
            String msg = "delta is lower than 0: inter-frame delay " + frameDelay + " (fr: " + mFrameRate + ", delta: " + delta + ")";
            //Log.println(Log.DEBUG, TAG, msg);
        }
        mTimeReference = cur_time;
        return frameDelay;
    }

    private void detectLongTouch(MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            long event_time = e.getDownTime();
        }
        //mStatus = GameStatus.paused;
    }

    private void onBackPressed() {
        Log.d(TAG, "(" + this + ")");
        switch (mStatus) {
            case running:
                mStatus = GameStatus.paused;
                break;
            case paused:
                mStatus = GameStatus.running;
                break;
            default:
                Log.d(TAG, "(" + this + ") un managed status: " + mStatus);
                break;
        }
    }

	public void reset() {
		m_fly.forcePosition(200, 200);
	}

}
