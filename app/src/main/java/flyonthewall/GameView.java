package flyonthewall;


import FlyOnTheWall.pkg.R;
import flyonthewall.base.EntityView;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
//import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.graphics.Bitmap;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = GameView.class.getSimpleName();
	
    private Canvas mCanva;
    private GameController m_controller;
    private Resources m_res;
    
    public Resources getRes() {
		return m_res;
	}

	public void setRes(Resources m_res) {
		this.m_res = m_res;
	}

	//private FlyView m_flyv; //cross reference with GameController -- not used
    
    public static int mWidth;
    public static int mHeight;
    
    Bitmap mBackgroundImage;
    private Boolean m_created;
    
    private TextView logView;
	
	public TextView getLogView() {
		return logView;
	}

	public void setLogView(TextView logView) {
		this.logView = logView;
	}

	int count = 0;

    /**
     * Use {@link #setupGameSurface(Activity, GameController)}
     *
     * @param context
     * @param ctrl
     */
    public GameView(Activity context, GameController ctrl) {
        super(context);

        getHolder().addCallback(this);
        setFocusable(true);
        m_res = context.getResources();
       
        //cross reference: needed to destroy app when surface is destroyed and for event signaling
        m_controller=ctrl;
        //
        setupGameSurface(context, ctrl);

        m_created =false;
        mBackgroundImage = BitmapFactory.decodeResource(m_res, R.drawable.wall);
     
    }

    /**
     * This method take care to remove the view defined in the xml and exchange it with the game view,
     * preserving all layout information.
     * <p/>
     *
     * @param ctrl
     * @return
     */

    private void setupGameSurface(Activity context, GameController ctrl) {

        final View panel = (View) context.findViewById(R.id.view1);
        if (panel != null) {
            ViewGroup parent = (ViewGroup) panel.getParent();

            int index = parent.indexOfChild(panel);
            /**
             * The view needs to know is controller in order to dispatch input events.
             */
            //final GameView mypanel = new GameView(this, ctrl);//ctrl.getView(); //

            ViewGroup.LayoutParams myparams = panel.getLayoutParams();
            this.setLayoutParams(myparams);
            parent.removeView(panel);
            parent.addView(this, index);
        } else {
            Log.println(Log.ERROR, TAG, "Panel is NULL!!!");
            System.exit(666);
        }

        //return mypanel;
    }
    
    //@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mWidth = width;
        mHeight = height;
    }
    
    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (this.getHolder()) {
        	mWidth = width;
        	mHeight = height;
        }
    }
	
    //@Override
	public void surfaceCreated(SurfaceHolder holder) {
		
		mWidth = holder.getSurfaceFrame().width();
		mHeight = holder.getSurfaceFrame().height();
		Log.d(TAG, "mWidth: "+mWidth);
		Log.d(TAG, "mHeight: "+mHeight);
		
		mBackgroundImage = mBackgroundImage.createScaledBitmap(mBackgroundImage, mWidth, mHeight, true);
		//mCanva.drawBitmap(mBackgroundImage, 0, 0, null);
		
		m_created=true;
		
		return;
	}
	
    //@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed!!!");
		m_controller.setRunning(false); 
		// tell the thread to shut down and wait for it to finish
		// this is a clean shutdown
		boolean retry = true;
		while (retry) {
			try {
				m_controller.join();
				retry = false;
			} catch (InterruptedException e) {
				// try again shutting down the thread
			}
		}
		Log.d(TAG, "Thread was shut down cleanly");
	}

	public void update(){
		//Log.d(TAG, "m_created:"+m_created);
		if(!m_created)
			return;
		
		//Log.d(TAG, "mCanva:"+mCanva);
		mCanva.drawColor(Color.LTGRAY);
		mCanva.drawBitmap(mBackgroundImage, 0, 0, null);
		
		
	}

	public void reset(){
		m_controller.reset();
	
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        InputDispatcher.getInputDispatcher().dispatchMotionEvent(event);

		return true;
    }
	
    public void setCanva(Canvas mCanva) {
		this.mCanva = mCanva;
	}

	public Canvas getCanva() {
		return mCanva;
	}

	public Boolean isCreated() {
		return m_created;
	}

	public GameController getController() {
		return m_controller;
	}

	public String switchState() {
		
		return m_controller.switchState();
    }


    public Bitmap getFrame() {
        return null;
    }
}
