package flyonthewall;

import java.util.Collection;
import java.util.HashMap;

import flyonthewall.base.msg.GameMessage;
import flyonthewall.base.msg.OnNewGameMessage;

/**
 * Created by andrea on 17/09/15.
 */
public class GameMsgDispatcher {

    private HashMap<String, OnNewGameMessage> mRegisteredClients = new HashMap<String, OnNewGameMessage>();

    private static GameMsgDispatcher mMessageDispatcher = null;

    public static GameMsgDispatcher getMessageDispatcher() {
        if (mMessageDispatcher == null) {
            mMessageDispatcher = new GameMsgDispatcher();
        }
        return mMessageDispatcher;
    }

    public void registerToGameMessages(String client, OnNewGameMessage cbk) {
        if (mRegisteredClients.get(client) == null) {
            mRegisteredClients.put(client, cbk);
        }
    }

    public void unregisterToGameMessages(String client) {
        if (mRegisteredClients.get(client) == null) {
            mRegisteredClients.remove(client);
        }
    }

    public void dispatchMessage(GameMessage msg) {
        //TODO review this algorithm to make it more efficient
        Collection<OnNewGameMessage> callbacks = mRegisteredClients.values();
        for (OnNewGameMessage cbk : callbacks) {
            cbk.receiveMessage(msg);
        }

    }
}
