package flyonthewall;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Collection;
import java.util.HashMap;

import flyonthewall.base.EntityView;
import flyonthewall.dbg.TouchMark;

/**
 * Created by andrea on 16/09/15.
 */
public class ViewManager {
    private static ViewManager theViewManager = null;
    final String TAG = ViewManager.class.getSimpleName();
    private GameView mGameView;
    private SurfaceHolder surfaceHolder = null;
    private HashMap<String, EntityView> ViewCollection = new HashMap<String, EntityView>();
    private Canvas mCanvas;
    private Collection<EntityView> views = null;
    //private FlyView m_flyView;
    //private FlySugarView m_flySugarLevel;

    private Resources mViewRes;

    public static ViewManager getViewManager() {
        if (theViewManager == null) {
            theViewManager = new ViewManager();
        }
        return theViewManager;
    }

    public void register(String name, EntityView view) {
        Log.d(TAG, "registering: " + name);
        if (ViewCollection.get(view) == null) {
            ViewCollection.put(name, view);
        }
    }

    public void cleanUp() {
        ViewCollection.clear();
        views = null;
        //fucking bug!!!
        // crash in unlockCanvasAndPost!
        mGameView = null;
    }

    public void setGameView(GameView view) {
        Log.d(TAG, "current - mGameView = " + mGameView);
        this.mGameView = view;
        Log.d(TAG, "  new  - mGameView = " + mGameView);
        if (surfaceHolder != null) {
            synchronized (surfaceHolder) {
                Log.d(TAG, "surfaceHolder sync update");
                this.surfaceHolder = mGameView.getHolder();
            }
        } else {
            Log.d(TAG, "surfaceHolder update");
            this.surfaceHolder = mGameView.getHolder();
        }
        this.mViewRes = mGameView.getRes();

    }

    public void updateViews() {
        try {
            if (!checkGameView()) {
                return;
            }
            //draw the canvas (view) in a synchronized way

            Canvas old = mCanvas;
            mCanvas = surfaceHolder.lockCanvas();

            if (old != null && !old.equals(mCanvas)) {
                Log.w(TAG, "canvas objects are different!");
                Log.w(TAG, "old mCanvas is " + old);
                Log.w(TAG, "new mCanvas is " + mCanvas);

            }
            if (mCanvas == null) {
                Log.e(TAG, "no canvas returned by lockCanvas!!!!");
                return;
            }
            //lock mutex
            synchronized (surfaceHolder) {
                //the order of "draw" calls defines the Z order
                mGameView.setCanva(mCanvas);
                try {
                    mGameView.update();
                    views = ViewCollection.values();
                    for (EntityView view : views) {
                        view.draw(mCanvas);
                    }
                    TouchMark.getMarker().draw(mCanvas);
                } catch (Exception e) {
                    Log.e(TAG, "exception!!" + e);
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Unexpected exception!!" + e);
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                //is crashing here:
                // IllegalArgumentException: canvas object must be the same instance that was previously returned by lockCanvas
                surfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }    // end finally

    }

    boolean checkGameView() {
        if (mGameView == null) {
            return false;
        }
        if (!mGameView.isCreated()) {
            return false;
        }
        return true;
    }

    public Resources getViewRes() {
        return mViewRes;
    }
}
