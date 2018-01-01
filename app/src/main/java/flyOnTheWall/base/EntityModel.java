package flyOnTheWall.base;

import android.graphics.Path;
import android.graphics.Point;

/**
 * Created by andrea on 02/10/15.
 */
public class EntityModel {
    protected String m_ename = "";

    /**
     * coordinates are referred to the map space
     * this coordinates represent the center of the entity
     */
    protected int m_x = 0;
    protected int m_y = 0;
    protected int m_z = 0;
    protected int m_heading;
    protected Path m_bounds;
    protected Point m_origin = new Point(0, 0);
    protected boolean m_offscreen = false;
    //public updateBounds(Po)
    protected int m_sprite_id = 0;
    protected String m_currStatusName = "none";

    public boolean is_offscreen() {
        return m_offscreen;
    }

    public void set_offscreen(boolean offscreen) {
        this.m_offscreen = offscreen;
    }

    public Path get_bounds() {
        return m_bounds;
    }

    public void set_bounds(Path m_bounds) {
        this.m_bounds = m_bounds;
    }

    public Point get_origin() {
        return m_origin;
    }

    public void set_origin(Point m_origin) {
        this.m_origin = m_origin;
    }

    public int get_x() {
        return m_x;
    }

    public void set_x(int m_x) {
        this.m_x = m_x;
    }

    public int get_y() {
        return m_y;
    }

    public void set_y(int m_y) {
        this.m_y = m_y;
    }

    public int get_z() {
        return m_z;
    }

    //protected
    public void set_z(int m_z) {
        this.m_z = m_z;
    }

    public String get_ename() {
        return m_ename;
    }

    public void set_ename(String m_ename) {
        this.m_ename = m_ename;
    }

    public int get_spriteId() {
        return m_sprite_id;
    }

    public void set_spriteId(int Id) {
        this.m_sprite_id = Id;
    }

    public String get_mCurrStatusName() {
        return m_currStatusName;
    }

    public void set_mCurrStatusName(String m_currStatusName) {
        this.m_currStatusName = m_currStatusName;
    }

    public int get_heading() {
        return m_heading;
    }

    public void set_heading(int m_heading) {
        this.m_heading = m_heading;
    }
}