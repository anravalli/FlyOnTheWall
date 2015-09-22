package flyonthewall;


import FlyOnTheWall.pkg.R;
import flyonthewall.base.EntityView;
import flyonthewall.base.msg.GameMessage;
import flyonthewall.base.msg.GameMessagesType;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
//import android.graphics.Paint;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import android.graphics.Bitmap;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = GameView.class.getSimpleName();
	
    private Canvas mCanva;
    private GameController m_controller;
    private Resources m_res;
    private final Handler m_main_h = new Handler(Looper.getMainLooper());
    
    public Resources getRes() {
		return m_res;
	}

	public void setRes(Resources m_res) {
		this.m_res = m_res;
	}
    public static int mWidth;
    public static int mHeight;
    
    Bitmap mBackgroundImage;
    private Boolean m_created;
    private TextView logView;
    private Activity theActivity = null;

    /**
     * Use {@link #setupGameSurface(GameController)}
     *
     * @param context
     * @param ctrl
     */
    public GameView(Activity context, GameController ctrl) {
        super(context);

        theActivity = context;

        getHolder().addCallback(this);
        setFocusable(true);
        m_res = context.getResources();

        //cross reference: needed to destroy app when surface is destroyed and for event signaling
        m_controller=ctrl;
        //
        setupGameSurface(ctrl);

        setupButtons();

        m_created =false;
        mBackgroundImage = BitmapFactory.decodeResource(m_res, R.drawable.wall);
    }

    private void setupButtons() {
        final Button resume = (Button) ((View) getParent()).findViewById(R.id.resume_btn);
        resume.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GameMessage msg = new GameMessage(GameMessagesType.GameResume, null);
                GameMsgDispatcher.getMessageDispatcher().dispatchMessage(msg);
            }
        });
        final Button exit = (Button) ((View) getParent()).findViewById(R.id.exit_btn);
        exit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                GameMessage msg = new GameMessage(GameMessagesType.GameExiting, null);
                GameMsgDispatcher.getMessageDispatcher().dispatchMessage(msg);
            }
        });
    }

    /**
     * This method take care to remove the view defined in the xml and exchange it with the game view,
     * preserving all layout information.
     * <p/>
     *
     * @param ctrl
     * @return
     */
    private void setupGameSurface(GameController ctrl) {

        final View panel = (View) theActivity.findViewById(R.id.game_view);
        if (panel != null) {
            ViewGroup parent = (ViewGroup) panel.getParent();
            int index = parent.indexOfChild(panel);

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

        m_created=true;

        return;
	}

    //@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed!!!");

    }

	public void update(){
		//Log.d(TAG, "m_created:"+m_created);
		if(!m_created)
			return;
        if (m_controller.getStatus() == GameStatus.paused) {
            drawPauseScreen(true);
        } else {
            drawPauseScreen(false);
        }

		mCanva.drawColor(Color.LTGRAY);
        mCanva.drawBitmap(mBackgroundImage, 0, 0, null);

	}

    private void drawPauseScreen(final boolean set) {
        final View layout = (View) theActivity.findViewById(R.id.pause_screen);

        m_main_h.post(new Runnable() {
            public void run() {
                if (layout.getVisibility() == GONE && set) {
                    layout.setVisibility(View.VISIBLE);
                } else if (layout.getVisibility() == VISIBLE && !set) {
                    layout.setVisibility(View.GONE);
                } else {
                    return;
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputDispatcher.getInputDispatcher().dispatchMotionEvent(event);
		return true;
    }

    public void setCanva(Canvas mCanva) {
		this.mCanva = mCanva;
	}

	public Boolean isCreated() {
		return m_created;
	}

    public TextView getLogView() {
        return logView;
    }

    public void setLogView(TextView logView) {
        this.logView = logView;
    }
}
