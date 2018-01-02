/*
 *	Fly On The Wall: a Fly survival game :-)
 *
 *	Copyright 2012 - 2018 Andrea Ravalli <anravalli @ gmail.com>
 *
 *	This file is part of Fly On The Wall.
 *
 *	Fly On The Wall is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	Fly On The Wall is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.

 *	You should have received a copy of the GNU General Public License
 *	along with Fly On The Wall.  If not, see <http://www.gnu.org/licenses/>.
*/

package flyOnTheWall;

import android.graphics.Point;
import android.util.Log;

/**
 * Created by andrea on 08/10/15.
 */
public class GameModel {
    private static final String TAG = GameModel.class.getSimpleName();

    /**
     * game run status
     */
    private GameStatus mStatus = GameStatus.stopped;

    /**
     * game map size
     * map_width, map_height
     */
    private int map_width = 1500;
    private int map_height = 2300;

    /**
     * game view port origin:
     * a point between (-map_width/2,-map_height/2) and (map_width/2,map_height/2)
     */
    private Point map_origin = new Point(-map_width / 4, -map_height / 4);

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

    public synchronized Point getMapOrigin() {
        //
        return map_origin;
    }

    public synchronized void setMapOrigin(Point origin) {
        this.map_origin = origin;
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
