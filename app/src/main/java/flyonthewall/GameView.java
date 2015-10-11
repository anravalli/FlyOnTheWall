package flyonthewall;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import FlyOnTheWall.pkg.R;
import flyonthewall.base.msg.GameMessage;
import flyonthewall.base.msg.GameMessagesType;

//import android.graphics.Paint;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

	private static final String TAG = GameView.class.getSimpleName();
    private final Handler m_main_h = new Handler(Looper.getMainLooper());
    Bitmap mBackgroundImage;
    float mBackgroundImageSaturation = 0.50f;
    private Canvas mCanva;
    //private GameController m_controller;
    private Resources m_res;
    //private int mWidth;
    //private int mHeight;
    //private int mMaxWidth;
    //private int mMaxHeight;
    //private int mOriginX = 0;
    //private int mOriginY = 0;
    private Boolean m_created;
    private Activity theActivity = null;
    private GameModel m_gameStatus = null;

    /**
     * Use {@link #setupGameSurface()}
     *
     * @param context
     * @param g_model
     */
    public GameView(Activity context, GameModel g_model) {
        super(context);

        theActivity = context;

        getHolder().addCallback(this);
        setFocusable(true);
        m_res = context.getResources();

        //game model reference: needed to destroy app when surface is destroyed and for event signaling
        m_gameStatus = g_model;
        //
        setupGameSurface();

        setupButtons();

        m_created =false;
        mBackgroundImage = BitmapFactory.decodeResource(m_res, R.drawable.wall);
    }

    public Resources getRes() {
        return m_res;
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
     * @return
     */
    private void setupGameSurface() {

        final View panel = theActivity.findViewById(R.id.game_view);
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

    /* Callback invoked when the surface dimensions change. */
    //@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged: format " + format + ", width " + width + ", height " + height + ", holder " + holder);
        m_gameStatus.setViewWidth(width);
        m_gameStatus.setViewHeight(height);

        /*
        * no more background rotation
        mMaxWidth = m_gameStatus.getMapWidth();
        mMaxHeight = m_gameStatus.getMapHeight();
        mBackgroundImage = mBackgroundImage.createScaledBitmap(mBackgroundImage, mMaxWidth, mMaxHeight, true);
        */
    }

    //@Override
	public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated: holder " + holder);

        //mWidth = holder.getSurfaceFrame().width();
        //mHeight = holder.getSurfaceFrame().height();
        m_gameStatus.setViewWidth(holder.getSurfaceFrame().width());
        m_gameStatus.setViewHeight(holder.getSurfaceFrame().height());
        //mMaxWidth = 2 * mWidth;
        //mMaxHeight = 2 * mHeight;

        mBackgroundImage = mBackgroundImage.createScaledBitmap(
                mBackgroundImage,
                m_gameStatus.getMapWidth(),
                m_gameStatus.getMapHeight(),
                true);

        m_created=true;

        return;
	}

    //@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Surface is being destroyed!!! (holder " + holder + ")");
        // tell the game thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        while (retry) {
            Log.d(TAG, "Stopping game thread (" + getId() + ")");
            //alternative to m_controller.join();
            if (m_gameStatus.getStatus() == GameStatus.running ||
                    m_gameStatus.getStatus() == GameStatus.paused) {
                m_gameStatus.setStatus(GameStatus.exitreq);
            } else if (m_gameStatus.getStatus() == GameStatus.stopped) {
                Log.d(TAG, "Game thread is already stopped -> exiting (" + getId() + ")");
                retry = false;
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "Game thread was shut down cleanly");

    }

	public void update(){
		//Log.d(TAG, "m_created:"+m_created);
        Paint p = null;
        if(!m_created)
			return;
        if (m_gameStatus.getStatus() == GameStatus.paused) {
            drawPauseScreen(true);
            p = desaturate(mBackgroundImageSaturation);
        } else {
            drawPauseScreen(false);
            p = desaturate(1);
        }
        Point o = m_gameStatus.getVpOrigin();
        mCanva.drawBitmap(mBackgroundImage, o.x, o.y, p);

	}

    private void drawPauseScreen(final boolean set) {
        final View layout = (View) theActivity.findViewById(R.id.pause_screen);

        final int invisible = INVISIBLE;

        m_main_h.post(new Runnable() {
            public void run() {
                if (layout.getVisibility() == invisible && set) {
                    layout.setVisibility(View.VISIBLE);
                    layout.bringToFront();
                } else if (layout.getVisibility() == VISIBLE && !set) {
                    layout.setVisibility(invisible);
                } else {
                    return;
                }
            }
        });
    }

    private Paint desaturate(float sat) {
        Canvas c = new Canvas(mBackgroundImage);
        Paint p = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(sat);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(cm);
        p.setColorFilter(filter);
        return p;
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

}
