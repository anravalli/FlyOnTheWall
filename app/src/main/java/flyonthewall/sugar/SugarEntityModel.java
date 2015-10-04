package flyonthewall.sugar;

import flyonthewall.base.EntityModel;

/**
 * Created by andrea on 27/09/15.
 */
public class SugarEntityModel extends EntityModel {
    private int m_sugar;
    private int m_max_sugar;


    private int m_consume_speed = 2;
    //private int _mSugar;

    SugarEntityModel() {
        m_ename = "sugar";
        m_x = 600;
        m_y = 600;
        m_z = 0;
        m_sprite_id = 0;
        m_max_sugar = 100;
        m_sugar = m_max_sugar;
    }

    SugarEntityModel(String name, int x, int y, int z, int drawableId, int max_sugarsugar) {
        m_ename = name;
        m_x = x;
        m_y = y;
        m_z = z;
        m_sprite_id = drawableId;
        m_max_sugar = max_sugarsugar;
        m_sugar = m_max_sugar;
    }

    public int get_sugar() {
        return m_sugar;
    }

    public void set_sugar(int m_sugar) {
        this.m_sugar = m_sugar;
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
