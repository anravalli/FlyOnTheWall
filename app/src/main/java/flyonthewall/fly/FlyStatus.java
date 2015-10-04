package flyonthewall.fly;

//import FlyOnTheWall.pkg.R;
//import android.graphics.BitmapFactory;
import android.util.Log;

import flyonthewall.base.EntityModel;

public class FlyStatus extends EntityModel {
	private static final String TAG = FlyStatus.class.getSimpleName();

	private int m_heading;
	private int m_dir;

	//the bitmap;
	private int m_currentFrame;
	
	private int m_sugar;
	private int m_max_sugar;

    private int m_dest_x = 0;
    private int m_dest_y = 0;

	FlyStatus(){
		m_ename = "fly";
		this.m_x = 0;
		this.m_y = 0;
		this.m_z = 0;
		this.m_heading = 0;
		this.m_dir = 0;
		this.m_sugar = 500;
        this.m_max_sugar = 500;
		//set_mState(new Landed());
	}

	//costruttore chiamato in Fly
	FlyStatus(String name, int x, int y, int z, int h, int dir) {
		m_ename = name;
		this.m_x = x;
		this.m_y = y;
		this.m_z = z;
		this.m_heading = h;
		this.m_dir = dir;
		this.m_sugar = 500;
        this.m_max_sugar = 500;

		//this.mState = new Landed();
	}
	FlyStatus(FlyStatus that){
		this.m_ename = that.m_ename;
		this.m_x = that.m_x;
		this.m_y = that.m_y;
		this.m_z = that.m_z;
		this.m_heading = that.m_heading;
		this.m_dir =  that.m_dir;
		this.m_sugar = that.m_sugar;
        this.m_max_sugar = that.m_max_sugar;
		//this.mState = that.mState;
	}

	void print(){
		//Log.d(TAG, "status: "+this.m_sname+", x: "+this.m_x+", y: "+this.m_y+", z: "+this.m_z+", heading: "+this.m_heading+", dir: "+this.m_dir);
		Log.d(TAG, "status: "+this);
	}

	public int get_heading() {
		return m_heading;
	}

	//protected
	public void set_heading(int m_heading) {
		this.m_heading = m_heading;
	}

	public int get_dir() {
		return m_dir;
	}

	protected void set_dir(int m_dir) {
		this.m_dir = m_dir;
	}

	public int getM_currentFrame() {
		return m_currentFrame;
	}

	public void setM_currentFrame(int m_currentFrame) {
		this.m_currentFrame = m_currentFrame;
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

	public int get_dest_x() {
		return m_dest_x;
    }

	public void set_dest_x(int m_dest_x) {
		this.m_dest_x = m_dest_x;
    }

	public int get_dest_y() {
		return m_dest_y;
    }

	public void set_dest_y(int m_dest_y) {
		this.m_dest_y = m_dest_y;
    }
}
