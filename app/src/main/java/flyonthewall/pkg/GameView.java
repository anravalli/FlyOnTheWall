package flyonthewall.pkg;


import FlyOnTheWall.pkg.R;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
//import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.TextView;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import java.lang.Runnable;

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
	
	public GameView(Context context, GameController ctrl) {
        super(context);
        getHolder().addCallback(this);
        setFocusable(true);
        
        m_res = context.getResources();
       
        //cross reference: needed to destroy app when surface is destroyed and for event signaling
        m_controller=ctrl;
        //
        
        m_created =false;
        mBackgroundImage = BitmapFactory.decodeResource(m_res,
                R.drawable.wall);        
     
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
    public boolean onTouchEvent(MotionEvent event){
    	
    	if(event.getAction()==MotionEvent.ACTION_DOWN){
    		Log.d(TAG, "event x: " + event.getX()+", Y:"+event.getY());
    		m_controller.onTouch(event.getX(),event.getY());
        	
    	}
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


}
