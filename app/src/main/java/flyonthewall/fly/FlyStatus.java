package flyonthewall.fly;

//import FlyOnTheWall.pkg.R;
//import android.graphics.BitmapFactory;
import android.util.Log;

public class FlyStatus {
	private static final String TAG = FlyStatus.class.getSimpleName();
	
	private int m_x;
	private int m_y;
	private int m_z;
	private int m_heading;
	private int m_dir;
	private String m_currStatusName;
	//the bitmap;
	private int m_frameDrawableId;
	private int m_currentFrame;
	
	private int m_sugar;
	private int m_max_sugar;

    private int m_dest_x = 0;
    private int m_dest_y = 0;

	FlyStatus(){
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
	FlyStatus(int x, int y, int z, int h, int dir){
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

	public int getFrameDrwableId() {
		return m_frameDrawableId;
	}

	public void setFrameDrwableId(int m_frameDrwableId) {
		this.m_frameDrawableId = m_frameDrwableId;
	}

	public int getM_currentFrame() {
		return m_currentFrame;
	}

	public void setM_currentFrame(int m_currentFrame) {
		this.m_currentFrame = m_currentFrame;
	}

	public String get_mCurrStatusName() {
		return m_currStatusName;
	}

	public void set_mCurrStatusName(String m_currStatusName) {
		this.m_currStatusName = m_currStatusName;
	}


	public int get_mSugar() {
		return m_sugar;
	}

	public void set_mSugar(int m_sugar) {
		this.m_sugar = m_sugar;
	}

	public int getM_max_sugar() {
		return m_max_sugar;
	}

    public int getM_dest_x() {
        return m_dest_x;
    }

    public void setM_dest_x(int m_dest_x) {
        this.m_dest_x = m_dest_x;
    }

    public int getM_dest_y() {
        return m_dest_y;
    }

    public void setM_dest_y(int m_dest_y) {
        this.m_dest_y = m_dest_y;
    }
}
