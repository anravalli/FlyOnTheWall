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

package flyOnTheWall.base.msg;

import java.util.HashMap;

import flyOnTheWall.base.Entity;

/**
 * Created by andrea on 18/09/15.
 */
public class GameMessage {
    public GameMessagesType type = GameMessagesType.None;
    public HashMap<String, Entity> details = null; //new ArrayList<Entity>();

    /*public GameMessage(){
        //nop
    }*/

    public GameMessage(GameMessagesType n_type, HashMap<String, Entity> n_details) {
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
