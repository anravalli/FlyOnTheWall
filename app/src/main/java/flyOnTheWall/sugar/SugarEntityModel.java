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

import android.graphics.Point;

import flyOnTheWall.base.EntityModel;

/**
 * Created by andrea on 27/09/15.
 */
public class SugarEntityModel extends EntityModel {
    private int m_sugar;
    private int m_max_sugar;
    private int m_consume_speed = 1;

    private int m_v_width;
    private int m_v_height;

    SugarEntityModel() {
        m_ename = "sugar";
        m_x = 0;
        m_y = 0;
        m_z = 0;
        m_sprite_id = 0;
        m_max_sugar = 100;
        m_sugar = m_max_sugar;
    }

    SugarEntityModel(String name, int x, int y, int z, int drawableId, int max_sugarsugar, Point o) {
        m_ename = name;
        m_x = x;
        m_y = y;
        m_z = z;
        m_sprite_id = drawableId;
        m_max_sugar = max_sugarsugar;
        m_sugar = m_max_sugar;
        m_origin = o;
    }

    public int get_sugar() {
        return m_sugar;
    }

    public void set_sugar(int m_sugar) {
        this.m_sugar = m_sugar;
    }

    public int get_v_width() {
        return m_v_width;
    }

    public void set_v_width(int m_v_width) {
        this.m_v_width = m_v_width;
    }

    public int get_v_height() {
        return m_v_height;
    }

    public void set_v_height(int m_v_height) {
        this.m_v_height = m_v_height;
    }

    public int get_max_sugar() {
        return m_max_sugar;
    }

    public void set_max_sugar(int m_max_sugar) {
        this.m_max_sugar = m_max_sugar;
    }

    public int get_consume_speed() {
        return m_consume_speed;
    }

    public void set_consume_speed(int m_consume_speed) {
        this.m_consume_speed = m_consume_speed;
    }

}
