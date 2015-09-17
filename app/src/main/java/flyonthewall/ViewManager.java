package flyonthewall;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import flyonthewall.base.EntityView;
import flyonthewall.dbg.TouchMark;
import flyonthewall.fly.FlySugarView;
import flyonthewall.fly.FlyView;

/**
 * Created by andrea on 16/09/15.
 */
public class ViewManager {
    final String TAG = ViewManager.class.getSimpleName();

    private GameView mGameView;
    private SurfaceHolder surfaceHolder = null;

    private HashMap<String, EntityView> ViewCollection = new HashMap<String, EntityView>();

    private static ViewManager theViewManager = null;

    private Canvas mCanvas;
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

        ViewCollection.put(name, view);

    }

    public void setGameView(GameView view) {
        this.mGameView = view;
        this.surfaceHolder = mGameView.getHolder();
        this.mViewRes = mGameView.getRes();

    }

    public void updateViews() {
        try {
            if (!checkGameView()) {
                return;
            }
            //draw the canvas (view) in a synchronized way
            mCanvas = surfaceHolder.lockCanvas();
            //lock mutex
            synchronized (surfaceHolder) {
                mGameView.setCanva(mCanvas);
                try {
                    mGameView.update();
                    Collection<EntityView> views = ViewCollection.values();
                    for (EntityView view : ViewCollection.values()) {
                        view.draw(mCanvas);
                    }

                    //m_flySugarLevel.draw(mCanvas);
                    TouchMark.getMarker().draw(mCanvas);
                } catch (Exception e) {
                    Log.d(TAG, "exception!!" + e);
                }
            }
        } finally {
            if (mCanvas != null) {
                //is crashing here:
                // IllegalArgumentException: canvas object must be the same instance that was previously returned by lockCanvas
                this.mGameView.getHolder().unlockCanvasAndPost(mCanvas);
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
