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
