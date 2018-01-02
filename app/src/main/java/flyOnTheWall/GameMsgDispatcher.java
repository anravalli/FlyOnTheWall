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

import android.util.Log;

import java.util.HashMap;
import java.util.Vector;

import flyOnTheWall.base.msg.GameMessage;
import flyOnTheWall.base.msg.OnNewGameMessage;

/**
 * Created by andrea on 17/09/15.
 */
public class GameMsgDispatcher {

    private static GameMsgDispatcher mMessageDispatcher = null;
    private final String TAG = GameMsgDispatcher.class.getSimpleName();
    private HashMap<String, OnNewGameMessage> mRegisteredClients = new HashMap<String, OnNewGameMessage>();

    public static GameMsgDispatcher getMessageDispatcher() {
        if (mMessageDispatcher == null) {
            mMessageDispatcher = new GameMsgDispatcher();
        }
        return mMessageDispatcher;
    }

    public synchronized void registerToGameMessages(String client, OnNewGameMessage cbk) {
        Log.i(TAG, "Registration request of: " + client + "(" + cbk + ")");
        if (mRegisteredClients.get(client) == null) {
            mRegisteredClients.put(client, cbk);
            Log.d(TAG, "registration OK");
        } else if (mRegisteredClients.get(client) != cbk) {
            Log.w(TAG, "Changing the callback for already registered client: " + client);
            Log.d(TAG, "old callback " + mRegisteredClients.get(client));
            Log.d(TAG, "new callback " + cbk);
            mRegisteredClients.put(client, cbk);
        } else {
            Log.w(TAG, "Duplicated registration request for " + client);
        }

    }

    public synchronized void unregisterToGameMessages(String client) {
        Log.i(TAG, "De-registration request from: " + client);
        if (mRegisteredClients.get(client) != null) {
            mRegisteredClients.remove(client);
            Log.d(TAG, "de-registration OK");
        }
    }

    public void dispatchMessage(GameMessage msg) {
        Log.d(TAG, "dispatchMessage: " + msg.type);
        //TODO extend this implementation to other dispatcher
        Vector<OnNewGameMessage> callbacks = new Vector<OnNewGameMessage>(mRegisteredClients.values());
        int i = callbacks.size();
        while (--i >= 0) {
            callbacks.get(i).receiveMessage(msg);
        }
    }
}
