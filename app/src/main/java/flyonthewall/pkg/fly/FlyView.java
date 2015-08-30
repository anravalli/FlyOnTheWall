package flyonthewall.pkg.fly;

import flyonthewall.pkg.GameView;
import FlyOnTheWall.pkg.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

public class FlyView extends Drawable {
	private static final String TAG = FlyView.class.getSimpleName();

	private Resources mRes;
	
	private FlyStatus m_flyModel = null; //model
	
	private float mHead = (float) 0.0;
	private float mScaleFactor = (float) 0.5;
	private int m_width = 0;
	private int m_heigth = 0;
	private int lght_z = 100;
	private int lght_cx = 300;
	private int lght_cy = 300;
	
	private float m_pivotx;
	private float m_pivoty;
	Bitmap mFrames = null;
	Bitmap mShadowFrames = null;
	
	public FlyView(GameView parent)
	{
		Log.d(TAG, "");
		set_mRes(parent.getRes());

		mFrames = BitmapFactory.decodeResource(get_mRes(), R.drawable.fly);
		mShadowFrames = BitmapFactory.decodeResource(get_mRes(), R.drawable.fly_shadow);
		
		m_width = mFrames.getWidth();
		m_heigth = mFrames.getHeight();
		
		m_pivotx = m_width/2;
		m_pivoty = m_heigth/2;
		
	}
		
	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		Matrix matrix = new Matrix();
		Matrix sh_matrix = new Matrix();
		float x, y, z, sh_x, sh_y;
		float currScale = (float)0.5;
		float sh_scale = (float)0.5;
		
		synchronized (m_flyModel) {
			mFrames = BitmapFactory.decodeResource(get_mRes(), m_flyModel.getFrameDrwableId());
			x = m_flyModel.get_x();
			y = m_flyModel.get_y();
			z = m_flyModel.get_z();
			currScale = (z + 50)/100;
			sh_scale = (float) (z/50+0.5);
			
			m_pivotx = (float) ((m_width/2)*currScale);
			m_pivoty = (float) ((m_heigth/2)*currScale);
			mHead = m_flyModel.get_heading();
			
			matrix.setTranslate(x-m_pivotx, y-m_pivoty);
			matrix.postRotate(mHead, x, y); //asse di rotazione traslato su nuova pos
			matrix.preScale(currScale, currScale);
			
			sh_x = (-lght_z*((x-lght_cx)/(z-lght_z)))+lght_cx+10;// x+(10+z/10);
			sh_y = (-lght_z*((y-lght_cy)/(z-lght_z)))+lght_cy+10; // y+(10+z/10);//
			
			sh_matrix.setTranslate(sh_x-m_pivotx, sh_y-m_pivoty);
			sh_matrix.postRotate(mHead, sh_x, sh_y);
			sh_matrix.preScale(sh_scale, sh_scale);
		}
		
		Paint paint = new Paint(); 

		canvas.drawBitmap(mShadowFrames, sh_matrix, paint);
		canvas.drawBitmap(mFrames, matrix, paint);
		
		//debug block
		paint.setColor(Color.LTGRAY); 
		paint.setTextSize(30); 
		String text = "x: "+m_flyModel.get_x()+" | y: "+m_flyModel.get_y()+" | z: "+m_flyModel.get_z()+" | a: "+mHead;
		canvas.drawText(text, 30, 30, paint);
		text = "status: "+m_flyModel.get_mCurrStatusName();
		canvas.drawText(text, 30, 60, paint);
		
        canvas.restore();
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub

	}

	public Resources get_mRes() {
		return mRes;
	}

	public void set_mRes(Resources mRes) {
		this.mRes = mRes;
	}

	public float get_mHead() {
		return mHead;
	}

	public void set_mHead(float mHead) {
		this.mHead = mHead;
	}

	public float get_mScale() {
		return mScaleFactor;
	}

	public void set_mScale(float mScale) {
		this.mScaleFactor = mScale;
	}

	public FlyStatus getFly() {
		return m_flyModel;
	}

	public void setFlyModel(FlyStatus m_flyModel) {
		this.m_flyModel = m_flyModel;
	}

}
