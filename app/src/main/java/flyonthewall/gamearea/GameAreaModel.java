package flyonthewall.gamearea;

import flyonthewall.base.EntityModel;

/**
 * Created by andrea on 05/10/15.
 */
public class GameAreaModel extends EntityModel {

    private int m_width;


    private int m_height;

    public GameAreaModel(int x, int y, int w, int h) {
        m_x = x;
        m_y = y;
        m_width = w;
        m_height = h;

    }

    public int get_height() {
        return m_height;
    }

    public void set_height(int m_height) {
        this.m_height = m_height;
    }

    public int get_width() {
        return m_width;
    }

    public void set_width(int m_width) {
        this.m_width = m_width;
    }
}
