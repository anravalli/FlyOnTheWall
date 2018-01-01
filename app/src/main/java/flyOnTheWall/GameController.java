package flyOnTheWall;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;

import flyOnTheWall.base.Entity;
import flyOnTheWall.base.OnTouchCallback;
import flyOnTheWall.base.msg.GameMessage;
import flyOnTheWall.base.msg.GameMessagesType;
import flyOnTheWall.base.msg.OnNewGameMessage;
import flyOnTheWall.fly.Fly;
import flyOnTheWall.fly.sm.Flying;
import flyOnTheWall.fly.sm.Walking;
import flyOnTheWall.sugar.Sugar;

public class GameController extends Thread {
    //controller class for the whole game
    //it holds also the game main loop

	private static final String TAG = GameController.class.getSimpleName();

    private final String name = "game_ctrl";

    // keep game state
    //private GameStatus mStatus = GameStatus.stopped;
    private GameModel mModel = new GameModel();
    private GameMsgDispatcher gameMsgDispatcher = null;
    private Fly m_fly;

    //frame rate regulation
    private long mTimeReference = 0;
    private long mFrameRate = 1000/30;
    private int x_pan_factor = 0;
    private int y_pan_factor = 0;

	public GameController() {
		super();
        Log.d(TAG, "Creating the game controller (" + this + ")");
        mTimeReference = System.currentTimeMillis();
        //register to inputs and game bus
        InputDispatcher.getInputDispatcher().registerToTouchEvent(name, new OnTouchCallback() {
            public void onTouch(MotionEvent event) {
                panMap(event);
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
        return mModel.getStatus();
    }

    public void setRunning(boolean running) {
        if (running) {

            this.mModel.setStatus(GameStatus.running);
        } else {
            this.mModel.setStatus(GameStatus.stopped);
        }
        Log.i(TAG, "Game status is changed to: " + this.mModel.getStatus());

    }

    private void fetchMessage(GameMessage msg) {
        Log.d(TAG, "message received (type: " + msg.type + ")");
        switch (msg.type) {
            case BackPressed:
                onBackPressed();
                break;
            case GameResume:
                mModel.setStatus(GameStatus.running);
                break;
            case GameExiting:
                mModel.setStatus(GameStatus.exiting);
                break;
            default:
                //do nothing
                break;
        }
    }

    void initGame() {
        Log.d(TAG, "Initializing Game");
        Entity sugar = new Sugar("sugar", mModel.getMapWidth() / 3, mModel.getMapHeight() / 4, 200, mModel.getMapOrigin());
        Entity sugar1 = new Sugar("sugar_1", 2 * mModel.getMapWidth() / 3, 3 * mModel.getMapHeight() / 5, 200, mModel.getMapOrigin());
        Entity sugar2 = new Sugar("sugar_2", mModel.getMapWidth() / 5, 8 * mModel.getMapHeight() / 10, 200, mModel.getMapOrigin());
        m_fly = new Fly("fly", mModel.getMapWidth() / 2, mModel.getMapHeight() / 2, 1000, mModel.getMapOrigin());
    }

    @Override
    public void run() {

        Log.d(TAG, "Starting game loop");

        //game main loop

        while (mModel.getStatus() != GameStatus.stopped) {
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
                switch (mModel.getStatus()) {
                    case running:
                        EntityManager.getEntityManager().updateEntities(mModel);
                        ViewManager.getViewManager().updateViews();
                        break;
                    case paused:
                        //enter pause
                        //TODO: during pause no event shoould be dispached to entity
                        ViewManager.getViewManager().updateViews();
                        break;
                    /*
                    TODO: on exit request each entity must save its status
                          after status save move the game to exiting
                    */
                    //temporary exitreq do the same of exiting
                    case exitreq:
                        Log.d(TAG, "Managing exit request (" + getId() + ")");
                        //mModel.setStatus(GameStatus.exiting);
                        GameMessage msg = new GameMessage(GameMessagesType.GameExiting, null);
                        GameMsgDispatcher.getMessageDispatcher().dispatchMessage(msg);
                        break;
                    case exiting:
                        Log.d(TAG, "Exiting (" + getId() + ")");
                        //do house cleanings
                        InputDispatcher.getInputDispatcher().unregisterToTouchEvent(name);
                        ViewManager.getViewManager().cleanUp();
                        gameMsgDispatcher.dispatchMessage(new GameMessage(
                                GameMessagesType.GameEnded, null));
                        gameMsgDispatcher.unregisterToGameMessages(name);
                        break;
                    default:
                        Log.w(TAG, "Un-managed game status! (" + mModel.getStatus() + ")");
                        break;
                }
            } catch (Exception e) {
                Log.e(TAG, "exception!!" + e);
                e.printStackTrace();
            }
        }
        //cleanUp();
        Log.i(TAG, "Game status changed to stopped");
        Log.d(TAG, "Thread status: " + this.getStatus());
    }

    private void cleanUp() {
        Log.d(TAG, "cleanUp:  perform de-registration");
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
            //String msg = "setting the inter-frame delay to " + frameDelay + " (fr: " + mFrameRate + ", delta: " + delta + ")";
            //Log.println(Log.DEBUG, TAG, msg);
        } else {
            frameDelay = 0;
            //String msg = "Delta is lower than 0: inter-frame delay " + frameDelay + " (fr: " + mFrameRate + ", delta: " + delta + ")";
            //Log.println(Log.DEBUG, TAG, msg);
        }
        mTimeReference = cur_time;
        return frameDelay;
    }

    private void panMap(MotionEvent e) {
        if (m_fly.getCurrentState() == Walking.getInstance() || m_fly.getCurrentState() == Flying.getInstance()) {
            return;
        }

        if (e.getAction() == MotionEvent.ACTION_MOVE) {
            Point o = new Point(mModel.getMapOrigin());
            //let's pan the map!

            int old_x_pan_factor = x_pan_factor;
            int old_y_pan_factor = y_pan_factor;
            int nx = o.x;
            int ny = o.y;
            int half_map_w = mModel.getMapWidth() / 2;
            int half_map_h = mModel.getMapHeight() / 2;
            Log.d(TAG, "Old origin X: " + o.x);
            if (e.getHistorySize() > 0) {
                x_pan_factor = (int) (e.getX() - e.getHistoricalX(0));
                y_pan_factor = (int) (e.getY() - e.getHistoricalY(0));
            }

            //increase X
            if (x_pan_factor == 0) {
                nx += old_x_pan_factor;
            } else {
                //keep constant scroll
                nx += x_pan_factor;
            }
            //increase Y
            if (y_pan_factor == 0) {
                ny += old_y_pan_factor;
                Log.d(TAG, "keep y_pan_factor: " + old_y_pan_factor);
            } else {
                //keep constant scroll
                Log.d(TAG, "Update y_pan_factor: " + y_pan_factor);
                ny += y_pan_factor;
            }

            //check map limits
            if (nx > -half_map_w && nx + mModel.getViewWidth() <= half_map_w) {
                o.x = nx;
                Log.d(TAG, "New X: " + o.x);
            }
            if (ny > -half_map_h && ny + mModel.getViewHeight() <= half_map_h) {
                o.y = ny;
                Log.d(TAG, "New Y: " + o.y);
            }

            mModel.setMapOrigin(o);
        }
        //mStatus = GameStatus.paused;
    }

    private void onBackPressed() {
        Log.d(TAG, "(" + this + ")");
        switch (mModel.getStatus()) {
            case running:
                mModel.setStatus(GameStatus.paused);
                break;
            case paused:
                mModel.setStatus(GameStatus.running);
                break;
            default:
                Log.d(TAG, "(" + this + ") un managed status: " + mModel.getStatus());
                break;
        }
    }

    public GameModel getModel() {
        return mModel;
    }
}
