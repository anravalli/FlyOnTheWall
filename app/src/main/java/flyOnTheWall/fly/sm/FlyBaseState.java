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

package flyOnTheWall.fly.sm;

import flyOnTheWall.base.EntityModel;
import flyOnTheWall.base.EntityStateMachine;
import flyOnTheWall.fly.FlyStatus;

/**
 * Created by andrea on 04/10/15.
 */
public abstract class FlyBaseState extends EntityStateMachine {


    public void consumeSugar() {
        ((FlyStatus) m_model).set_sugar(((FlyStatus) m_model).get_sugar() - m_sugarConsumeSpeed);
    }

    @Override
    public void enterState(EntityModel status) {
        m_model = status;
    }
}
