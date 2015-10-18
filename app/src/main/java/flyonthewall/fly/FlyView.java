package flyonthewall.fly;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import FlyOnTheWall.pkg.R;
import flyonthewall.ViewManager;
import flyonthewall.base.EntityView;

public class FlyView extends EntityView {
	private static final String TAG = FlyView.class.getSimpleName();
    Bitmap mFrames = null;
    Bitmap mShadowFrames = null;
    //private FlyStatus m_flyModel = null; //model
    private float mHead = (float) 0.0;
	private float mScaleFactor = (float) 0.5;
	private int m_width = 0;
	private int m_heigth = 0;
	private int amb_light_z = 100;
	private int amb_light_cx = 300;
	private int amb_light_cy = 300;
    //private float m_pivotx;
    //private float m_pivoty;

    public FlyView(FlyStatus mFlyStatus) {
		Log.d(TAG, "Creating Fly view");
        mEntityModel = mFlyStatus;
        mRes = ViewManager.getViewManager().getViewRes();

		mFrames = BitmapFactory.decodeResource(mRes, R.drawable.fly);
		mShadowFrames = BitmapFactory.decodeResource(mRes, R.drawable.fly_shadow);
		
		m_width = mFrames.getWidth();
		m_heigth = mFrames.getHeight();

        mPivot = new Point(m_width / 2, m_heigth / 2);

		ViewManager.getViewManager().register("fly", this);
		
	}

	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		Matrix matrix = new Matrix();
		Matrix sh_matrix = new Matrix();
        //keep them as float since changing to int lead to strange behaviours
        float x, y, z, sh_x, sh_y;
        float currScale;
        float sh_scale;

        synchronized (mEntityModel) {
            mFrames = BitmapFactory.decodeResource(mRes, mEntityModel.get_spriteId());
            x = mapToViewX(mEntityModel.get_x());
            y = mapToViewY(mEntityModel.get_y());
            z = mEntityModel.get_z();
            currScale = (z + 50)/100;
			sh_scale = (float) (z/50+0.5);

            mPivot.x = (int) ((m_width / 2) * currScale);
            mPivot.y = (int) ((m_heigth / 2) * currScale);
            mHead = ((FlyStatus) mEntityModel).get_heading();

            matrix.setTranslate(toLeft(x), toTop(y));
            matrix.postRotate(mHead, x, y); //asse di rotazione traslato su nuova pos
			matrix.preScale(currScale, currScale);

			sh_x = (-amb_light_z * ((x - amb_light_cx) / (z - amb_light_z))) + amb_light_cx + 10;// x+(10+z/10);
			sh_y = (-amb_light_z * ((y - amb_light_cy) / (z - amb_light_z))) + amb_light_cy + 10; // y+(10+z/10);//

            sh_matrix.setTranslate(toLeft(sh_x), toTop(sh_y));
            sh_matrix.postRotate(mHead, sh_x, sh_y);
			sh_matrix.preScale(sh_scale, sh_scale);
		}
		
		Paint paint = new Paint(); 

		canvas.drawBitmap(mShadowFrames, sh_matrix, paint);
		canvas.drawBitmap(mFrames, matrix, paint);

		//debug block
        Paint.Style style = Paint.Style.STROKE;
        paint.setColor(Color.YELLOW);
        paint.setStyle(style);
        //Rect r = mapToView(getBoundingBox(20));
        RectF r = mapToViewF(getBoundingBox(20));
        Matrix m = new Matrix();
        m.postRotate(mHead, x, y);
        m.mapRect(r);
        canvas.drawRect(r, paint);

        style = Paint.Style.FILL_AND_STROKE;
        paint.setStyle(style);
        paint.setColor(Color.LTGRAY);
        paint.setTextSize(30);
        String text = "x: " + mEntityModel.get_x() + " | y: " + mEntityModel.get_y() + " | z: " + mEntityModel.get_z() + " | a: " + mHead;
        canvas.drawText(text, 30, 30, paint);
        text = "status: " + mEntityModel.get_mCurrStatusName();
        canvas.drawText(text, 30, 60, paint);
		
        canvas.restore();
	}

    public synchronized Rect getSensitiveArea(int sensitivity) {
        return getBoundingBox(-sensitivity);
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
        return (FlyStatus) mEntityModel;
    }

	public void setFlyModel(FlyStatus m_flyModel) {
        this.mEntityModel = m_flyModel;
    }

}
