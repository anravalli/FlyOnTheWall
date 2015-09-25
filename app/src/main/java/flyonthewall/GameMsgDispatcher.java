package flyonthewall;

import android.util.Log;

import java.util.HashMap;
import java.util.Vector;

import flyonthewall.base.msg.GameMessage;
import flyonthewall.base.msg.OnNewGameMessage;

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
        Log.d(TAG, "Registration request of: " + client + "(" + cbk + ")");
        if (mRegisteredClients.get(client) == null) {
            mRegisteredClients.put(client, cbk);
            Log.d(TAG, "registration OK");
        }

    }

    public synchronized void unregisterToGameMessages(String client) {
        Log.d(TAG, "De-registration request from: " + client);
        if (mRegisteredClients.get(client) != null) {
            mRegisteredClients.remove(client);
            Log.d(TAG, "de-registration OK");
        }
    }

    public void dispatchMessage(GameMessage msg) {
        Log.d(TAG, "dispatchMessage: " + msg.type);
        //TODO extend this implementation to other dispatcher
        Vector<OnNewGameMessage> callbacks = new Vector<OnNewGameMessage>(mRegisteredClients.values());
        int i = callbacks == null ? 0 : callbacks.size();
        while (--i >= 0) {
            callbacks.get(i).receiveMessage(msg);
        }
    }
}
