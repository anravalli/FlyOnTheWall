package flyonthewall.pkg;

import FlyOnTheWall.pkg.R;
import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;
//import android.widget.ProgressBar;
import android.widget.Toast;
//import android.widget.ToggleButton;


public class FlyOnTeWall extends Activity {
	
	public MediaPlayer mpPlayer;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.main);

        setMainCallback();
        mpPlayer = MediaPlayer.create(this, R.raw.title_score_init);
        mpPlayer.start();
        
    }
    
    private void setMainCallback() {
    	final View reset = findViewById(R.id.imageView1);
        reset.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // Perform action on clicks, depending on whether it's now checked
            	Toast.makeText(FlyOnTeWall.this, "Game is Starting!!", Toast.LENGTH_SHORT).show();
            	setGameLayout();
            	mpPlayer.start();
            }
        });
		
	}

	void setGameLayout()
    {
    	final String TAG = GameView.class.getSimpleName();
    	
    	setContentView(R.layout.game_layout);
    	
    	final GameController gameCtrl = new GameController();
    	final GameView m_gview = setupGameSurface(gameCtrl);
    	gameCtrl.setGameView(m_gview); //TODO riferimento incrociato!!!!
    	
    	final Button reset = (Button) findViewById(R.id.button1);
    	reset.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			// Perform action on clicks, depending on whether it's now checked
    			Toast.makeText(FlyOnTeWall.this, "RESET", Toast.LENGTH_SHORT).show();
    			m_gview.reset();
    		}
    	});
    	
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
    	
    	final Button switchState = (Button) findViewById(R.id.button3);
    	switchState.setOnClickListener(new OnClickListener() {
    		public void onClick(View v) {
    			
    			String status = m_gview.switchState();
    			//String status = gameCtrl.switchState();
    			Toast.makeText(FlyOnTeWall.this, status, Toast.LENGTH_SHORT).show();
    		}
    	});

    	gameCtrl.setRunning(true);
    	gameCtrl.start();

    	return;
    }

	private GameView setupGameSurface(GameController ctrl) {
		
    	final View panel = (View) findViewById(R.id.view1);
    	ViewGroup parent = (ViewGroup) panel.getParent();
    	int index = parent.indexOfChild(panel);
    	final GameView mypanel = new GameView(this, ctrl);//ctrl.getView(); //

    	LayoutParams myparams = panel.getLayoutParams();
    	mypanel.setLayoutParams(myparams);
    	parent.removeView(panel);
    	parent.addView(mypanel, index);
    	
		return mypanel;
	}
}