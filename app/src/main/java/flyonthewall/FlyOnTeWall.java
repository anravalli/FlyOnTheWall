package flyonthewall;

import FlyOnTheWall.pkg.R;
import flyonthewall.base.msg.GameMessage;
import flyonthewall.base.msg.GameMessagesType;
import flyonthewall.base.msg.OnNewGameMessage;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
//import android.widget.ProgressBar;
import android.widget.Toast;
//import android.widget.ToggleButton;

/**
 * @author Andrea Ravalli <anravlli @ gmail.com>
 * @version 0.6
 * @since 2012
 * <p/>
 * This class is the application entry point.
 * Here the android Activity base class is extended to offer
 */
public class FlyOnTeWall extends Activity {

    private final String TAG = FlyOnTeWall.class.getSimpleName();
    public MediaPlayer mpPlayer;
    private GameController mGameCtrl = null;
    private GameMsgDispatcher msgDispatcher = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        msgDispatcher = GameMsgDispatcher.getMessageDispatcher();
        msgDispatcher.registerToGameMessages("main", new OnNewGameMessage() {
            public void receiveMessage(GameMessage msg) {
                fetchMessage(msg);
            }
        });

        setContentView(R.layout.main);

        setMainCallback();
        mpPlayer = MediaPlayer.create(this, R.raw.title_score_init);
        mpPlayer.start();

    }

    private void fetchMessage(GameMessage msg) {
        switch (msg.type) {
            case GameEnded:
                resetSplashView();
                mGameCtrl.setRunning(false);
                Log.d(TAG, "Game ended -- clean up");
                break;
            default:
                //nope
                break;
        }
    }

    @Override
    public void onBackPressed() {
        //TODO check pointer consistency
        if (mGameCtrl != null && mGameCtrl.getStatus() != GameStatus.stopped) {
            GameMessage msg = new GameMessage(GameMessagesType.BackPressed, null);
            GameMsgDispatcher.getMessageDispatcher().dispatchMessage(msg);
        } else {
            Log.i(TAG, "clean exit");
            super.onBackPressed();
        }
    }

    /**
     * This method defines a general callback for the main view.
     * Touching this view the main view is changed to the game view and the game it self is started.
     */
    private void setMainCallback() {
        final View splash = findViewById(R.id.imageView1);
        splash.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(FlyOnTeWall.this, "Game is Starting!!", Toast.LENGTH_SHORT).show();
                startGame();
                //TODO this is just test sounds availability
                mpPlayer.start();
            }
        });
    }

    /**
     * Set up the game layout and start the game.
     * The layout is defined in a standard Android layout. Here only the callback are defined
     */
    void startGame()
    {
        //change view layout
        setContentView(R.layout.game_layout);

        //create the game controller
        mGameCtrl = new GameController();

        //setup the game view
        //     controller needs to be notified to the view in order to allow a clean shutdown
        //     the context needs to be notified to the view in order to setup the SurfaceView
        final GameView gameView = new GameView(this, mGameCtrl);
        ViewManager.getViewManager().setGameView(gameView);

        mGameCtrl.initGame();

        mGameCtrl.setRunning(true);
        mGameCtrl.start();

        return;
    }

    private void resetSplashView() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(FlyOnTeWall.this, "Ending Game!", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.main);
                mpPlayer.start();
                setMainCallback();
            }
        });
    }
}