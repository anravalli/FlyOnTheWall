package flyonthewall;

import FlyOnTheWall.pkg.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
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

    final String TAG = FlyOnTeWall.class.getSimpleName();
    public MediaPlayer mpPlayer;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.main);

        setMainCallback();
        mpPlayer = MediaPlayer.create(this, R.raw.title_score_init);
        mpPlayer.start();

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
                /**
                 *
                 */
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
        final GameController gameCtrl = new GameController();
        //setup the game surface
        final GameView gameView = new GameView(this, gameCtrl);
        ViewManager.getViewManager().setGameView(gameView);
        /**
         * inform the controller about its view.
         * This is a kind of cross reference!
         * It seem to be needed because of inputs handling
         */
        gameCtrl.initGame();


        /**
         * button callback definition: reset
         * Pressing this button will reset the Fly position through the game view
         */
        final Button reset = (Button) findViewById(R.id.button1);
        reset.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(FlyOnTeWall.this, "RESET", Toast.LENGTH_SHORT).show();
                gameView.reset();
            }
        });
        /**
         * button callback definition: back
         * Pressing this button will exit from the game and return to the main screen.
         */
        final Button back = (Button) findViewById(R.id.button2);
        back.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
                Toast.makeText(FlyOnTeWall.this, "Ending Game!", Toast.LENGTH_SHORT).show();
                setContentView(R.layout.main);
                mpPlayer.start();
                setMainCallback();
            }
        });
        /**
         * button callback definition: state machine toggle switch
         * Pressing this button will trigger Fly status change
         */
        final Button switchState = (Button) findViewById(R.id.button3);
        switchState.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                String status = gameView.switchState();
                //String status = gameCtrl.switchState();
                Toast.makeText(FlyOnTeWall.this, status, Toast.LENGTH_SHORT).show();
            }
        });

        /**
         * let's start the game!
         */
        gameCtrl.setRunning(true);
        gameCtrl.start();

        return;
    }


}