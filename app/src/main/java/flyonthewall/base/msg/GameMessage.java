package flyonthewall.base.msg;

import java.util.ArrayList;

import flyonthewall.base.Entity;

/**
 * Created by andrea on 18/09/15.
 */
public class GameMessage {
    public GameMessagesType type = GameMessagesType.None;
    public ArrayList<Entity> details = null; //new ArrayList<Entity>();

    /*public GameMessage(){
        //nop
    }*/

    public GameMessage(GameMessagesType n_type, ArrayList<Entity> n_details) {
        this.type = n_type;
        //TODO memory management must be reviewed
        this.details = n_details;
    }

    public GameMessage(GameMessage msg) {
        this.type = msg.type;
        //TODO memory management must be reviewed
        this.details = msg.details;
    }


}
