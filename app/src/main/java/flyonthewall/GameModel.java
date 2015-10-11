package flyonthewall;

import android.graphics.Point;
import android.util.Log;

/**
 * Created by andrea on 08/10/15.
 */
public class GameModel {
    private static final String TAG = GameModel.class.getSimpleName();
    ;
    /**
     * game run status
     */
    private GameStatus mStatus = GameStatus.stopped;

    /**
     * game map size
     * map_width, map_height
     */
    private int map_width = 1500;
    private int map_height = 1500;

    /**
     * game view port origin:
     * a point between (-map_width/2,-map_height/2) and (map_width/2,map_height/2)
     */
    private Point vp_origin = new Point(0, 0);

    /**
     * current view port size:
     * view_width, view_height
     * IMPORTANT: write access only from GameView
     */
    private int view_width;
    private int view_height;

    public synchronized GameStatus getStatus() {
        return mStatus;
    }

    public synchronized void setStatus(GameStatus mStatus) {
        this.mStatus = mStatus;
    }

    public synchronized int getMapWidth() {
        return map_width;
    }

    public synchronized void setMapWidth(int map_width) {
        this.map_width = map_width;
    }

    public synchronized int getMapHeight() {
        return map_height;
    }

    public synchronized void setMapHeight(int map_height) {
        this.map_height = map_height;
    }

    public synchronized Point getVpOrigin() {
        //temporary scrolling animation
        /*if (vp_origin.x >= -map_width/2)
            vp_origin.x--;
        if (vp_origin.y >= -map_height/2)
            vp_origin.y--;*/
        return vp_origin;
    }

    public synchronized void setVpOrigin(Point vp_origin) {
        this.vp_origin = vp_origin;
    }

    public synchronized int getViewWidth() {
        return view_width;
    }

    public synchronized void setViewWidth(int view_width) {
        Log.d(TAG, "setViewWidth, new width: " + view_width);
        this.view_width = view_width;
    }

    public synchronized int getViewHeight() {
        return view_height;
    }

    public synchronized void setViewHeight(int view_height) {
        Log.d(TAG, "setViewHeight, new height: " + view_height);
        this.view_height = view_height;
    }
}
