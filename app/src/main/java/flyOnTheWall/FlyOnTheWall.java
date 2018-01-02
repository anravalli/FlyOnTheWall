/*
 *	Fly On The Wall: a Fly survival game :-)
 *
 *	Copyright 2012 - 2018 Andrea Ravalli <anravalli @ gmail.com>
 *
 *	This file is part of Fly On The Wall.
 *
 *	Fly On The Wall is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	Fly On The Wall is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.

 *	You should have received a copy of the GNU General Public License
 *	along with Fly On The Wall.  If not, see <http://www.gnu.org/licenses/>.
*/

package flyOnTheWall;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;

import flyOnTheWall.pkg.R;
import flyOnTheWall.base.msg.GameMessage;
import flyOnTheWall.base.msg.GameMessagesType;
import flyOnTheWall.base.msg.OnNewGameMessage;

/**
 * @author Andrea Ravalli <anravalli @ gmail.com>
 * @version 0.6
 * @since 2012
 * <p/>
 * This class is the application entry point.
 * Here the android Activity base class is extended to offer
 */
public class FlyOnTheWall extends Activity {

    private final String TAG = FlyOnTheWall.class.getSimpleName();
    public MediaPlayer mpPlayer;
    private GameController mGameCtrl = null;
    private GameMsgDispatcher msgDispatcher = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "Creating Activity");
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //create the game controller
        mGameCtrl = new GameController();

        //TODO: consider moving message registration to startGame()
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
                //handled in game thread?
                if (mGameCtrl != null) {
                    int id = android.os.Process.getThreadPriority(android.os.Process.myTid());
                    Log.d(TAG, "Stopping game thread (" + id + ")");
                    mGameCtrl.setRunning(false);
                }
                cleanUpReference();
                Log.d(TAG, "Game ended");
                break;
            default:
                //nope
                break;
        }
    }

    public void cleanUpReference() {
        //forcing stop
        //forcing de-registration

        Log.w(TAG, "invalidate controller");
        mGameCtrl = null;
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
                Toast.makeText(FlyOnTheWall.this, "Game is Starting!!", Toast.LENGTH_SHORT).show();
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

        //if the controller has been invalidated restart it
        if (mGameCtrl == null) {
            Log.d(TAG, "Game controller was invalidated: create a new one!");
            mGameCtrl = new GameController();
        }

        //setup the game view
        //     controller needs to be notified to the view in order to allow a clean shutdown
        //     the context needs to be notified to the view in order to setup the SurfaceView
        GameView gameView = new GameView(this, mGameCtrl.getModel());
        ViewManager.getViewManager().setGameView(gameView);

        mGameCtrl.initGame();

        mGameCtrl.setRunning(true);
        mGameCtrl.start();

        return;
    }

    private void resetSplashView() {
        this.runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(FlyOnTheWall.this, "Ending Game!", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.main);
                mpPlayer.start();
                setMainCallback();
            }
        });
    }
}