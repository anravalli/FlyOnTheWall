package flyonthewall;


import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
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
    private GameController m_controller;
    private Resources m_res;
    private int mWidth;
    private int mHeight;
    private int mMaxWidth;
    private int mMaxHeight;
    private int mOriginX = 0;
    private int mOriginY = 0;
    private Boolean m_created;
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
        mMaxWidth = 2 * mWidth;
        mMaxHeight = 2 * mHeight;
        mBackgroundImage = mBackgroundImage.createScaledBitmap(mBackgroundImage, mMaxWidth, mMaxHeight, true);
    }

    /* Callback invoked when the surface dimensions change. */
    public void setSurfaceSize(int width, int height) {
        // synchronized to make sure these all change atomically
        synchronized (this.getHolder()) {
        	mWidth = width;
        	mHeight = height;
            mMaxWidth = 2 * mWidth;
            mMaxHeight = 2 * mHeight;
            mBackgroundImage = mBackgroundImage.createScaledBitmap(mBackgroundImage, mMaxWidth, mMaxHeight, true);
        }
    }

    //@Override
	public void surfaceCreated(SurfaceHolder holder) {

		mWidth = holder.getSurfaceFrame().width();
		mHeight = holder.getSurfaceFrame().height();
        mMaxWidth = 2 * mWidth;
        mMaxHeight = 2 * mHeight;

		Log.d(TAG, "mWidth: "+mWidth);
		Log.d(TAG, "mHeight: "+mHeight);

        mBackgroundImage = mBackgroundImage.createScaledBitmap(mBackgroundImage, mMaxWidth, mMaxHeight, true);

        m_created=true;

        return;
	}

    //@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		Log.d(TAG, "Surface is being destroyed!!!");
        // tell the thread to shut down and wait for it to finish
        // this is a clean shutdown
        boolean retry = true;
        while (retry) {
            try {
                m_controller.join();
                retry = false;
            } catch (InterruptedException e) {
                Log.d(TAG, "Waiting for shut down --- " + e);
            } catch (Exception e) {
                Log.d(TAG, "Waiting for shut down --- " + e);
            }
        }
        Log.d(TAG, "Thread was shut down cleanly");

    }

	public void update(){
		//Log.d(TAG, "m_created:"+m_created);
        Paint p = null;
        if(!m_created)
			return;
        if (m_controller.getStatus() == GameStatus.paused) {
            drawPauseScreen(true);
            p = desaturate(mBackgroundImageSaturation);
        } else {
            drawPauseScreen(false);
            p = desaturate(1);
        }
        if (mOriginX >= -mWidth)
            mOriginX--;
        if (mOriginY >= -mHeight)
            mOriginY--;
        //mCanva.drawColor(Color.LTGRAY);
        mCanva.drawBitmap(mBackgroundImage, mOriginX, mOriginY, p);

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
