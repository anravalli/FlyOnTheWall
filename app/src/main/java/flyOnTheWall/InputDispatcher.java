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

import android.view.MotionEvent;

import java.util.Collection;
import java.util.HashMap;

import flyOnTheWall.base.OnTouchCallback;

/**
 * Created by andrea on 17/09/15.
 */
public class InputDispatcher {
    private static InputDispatcher mInputDispatcher = null;

    HashMap<String, OnTouchCallback> mTouchEventRecipient = new HashMap<String, OnTouchCallback>();

    public static InputDispatcher getInputDispatcher() {
        if (mInputDispatcher == null) {
            mInputDispatcher = new InputDispatcher();
        }
        return mInputDispatcher;
    }

    public void registerToTouchEvent(String name, OnTouchCallback callback) {
        if (mTouchEventRecipient.get(name) == null) {
            mTouchEventRecipient.put(name, callback);
        }
    }

    public void unregisterToTouchEvent(String name) {
        if (mTouchEventRecipient.get(name) != null) {
            mTouchEventRecipient.remove(name);
        }
    }

    public void dispatchMotionEvent(MotionEvent e) {
        Collection<OnTouchCallback> touchRecipients = mTouchEventRecipient.values();
        for (OnTouchCallback callback : touchRecipients) {
            callback.onTouch(e);
        }
    }

}
