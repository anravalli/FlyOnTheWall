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

package flyOnTheWall.sugar;

import android.util.Log;

import java.util.HashMap;

import flyOnTheWall.base.Entity;
import flyOnTheWall.base.EntityModel;
import flyOnTheWall.base.EntityStateMachine;

/**
 * Created by andrea on 27/09/15.
 */
public class SugarIdleState extends EntityStateMachine {
    private static final String TAG = SugarIdleState.class.getSimpleName();

    private static SugarIdleState mInstance = null;

    private final String m_name = "SugarIdleState";

    public static SugarIdleState getInstance() {

        if (mInstance == null) {
            mInstance = new SugarIdleState();
        }
        Log.d(TAG, "instance state: " + mInstance + "(name: " + mInstance.m_name + ")");
        return mInstance;
    }

    @Override
    public void enterState(EntityModel status) {
        m_model = status;
    }

    @Override
    public void exitState() {

    }

    @Override
    public void updatePosition() {

    }

    @Override
    public void update() {

    }

    @Override
    public void manageCollision(HashMap<String, Entity> details) {

    }
}
