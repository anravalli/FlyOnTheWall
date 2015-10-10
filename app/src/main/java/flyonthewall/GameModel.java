package flyonthewall;

import android.graphics.Point;

/**
 * Created by andrea on 08/10/15.
 */
public class GameModel {
    /**
     * game run status
     */
    private GameStatus mStatus = GameStatus.stopped;

    /**
     * game map size
     * map_width, map_height
     */
    private int map_width;
    private int map_height;

    /**
     * game view port origin:
     * a point between (-map_width/2,-map_height/2) and (map_width/2,map_height/2)
     */
    private Point vp_origin;

    /**
     * current view port size:
     * view_width, view_height
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
        return vp_origin;
    }

    public synchronized void setVpOrigin(Point vp_origin) {
        this.vp_origin = vp_origin;
    }

    public synchronized int getViewWidth() {
        return view_width;
    }

    public synchronized void setViewWidth(int view_width) {
        this.view_width = view_width;
    }

    public synchronized int getViewHeight() {
        return view_height;
    }

    public synchronized void setViewHeight(int view_height) {
        this.view_height = view_height;
    }
}
