package flyonthewall.pkg.fly;

import flyonthewall.pkg.GameView;
import FlyOnTheWall.pkg.R;

import android.annotation.TargetApi;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;

public class FlySugarView extends Drawable {

	private static final String TAG = FlyView.class.getSimpleName();;
	private FlyStatus m_flyModel = null;
	private int left_x = 0;
	private int top_y = 0;
	private int lenght = 100;
	private int height = 10;
	private Paint mLinePaint;
	
	private RectF mScratchRect = new RectF(0, 0, 0, 0);
	Bitmap mSugarFrames = null;
    Bitmap mSugarLevel = null;
    Bitmap b = null;

    private Resources mRes;

    public FlySugarView(GameView parent)
	{
		Log.d(TAG, "");
        // Initialize paints for speedometer
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setARGB(128, 0, 255, 0);
        set_mRes(parent.getRes());

        mSugarFrames = BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_fly_dark);
        mSugarLevel =  BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_level);



	}
	
	@Override
	public void draw(Canvas canvas) {
		canvas.save();
		
		//int c_width = canvas.getWidth();
		int c_height = canvas.getHeight();
		top_y = c_height - 20 - height;
        left_x = 20;

        Paint paint = new Paint();

        synchronized (m_flyModel) {
			//int sugarLevel = (int) (UI_BAR * m_flyModel.get_mSugar());


			mScratchRect.set(left_x, top_y, (float) (left_x + m_flyModel.get_mSugar()), top_y - height);

			//canvas.drawRect(mScratchRect, mLinePaint);
            float top_rect = c_height - mSugarFrames.getHeight() - 40;
            float left_rect = mSugarFrames.getWidth() + 40;

            RectF newRect = new RectF(left_rect, top_rect,
                    (float) (left_rect + mSugarFrames.getWidth()),
                    top_rect + mSugarFrames.getHeight());
            //canvas.drawRect(newRect, mLinePaint);
            //canvas.drawRect(mScratchRect, mLinePaint);

			//paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
			//canvas.drawBitmap(sugarLevel, left_x, c_height - mSugarFrames.getHeight() - 20, paint);
            //canvas.drawBitmap(mSugarFrames, left_x, c_height - mSugarFrames.getHeight() - 20, paint);

            //Bitmap rect = Bitmap.createBitmap(mSugarFrames.getWidth(), mSugarFrames.getHeight(), Bitmap.Config.ARGB_8888);
            //Bitmap b = getMaskedBitmap(get_mRes(), R.drawable.sugar_fly, R.drawable.sugar_fly_green);

            canvas.drawBitmap(mSugarFrames, left_x, c_height - mSugarFrames.getHeight() - 20, paint);

            RectF sugarLevel = new RectF(0,
                    //mSugarFrames.getHeight() - ((float) m_flyModel.get_mSugar()* mSugarFrames.getHeight()/500),
                    mSugarFrames.getHeight() - (float) mSugarFrames.getHeight()/2,
                    mSugarFrames.getWidth(),
                    mSugarFrames.getHeight());

            //RectF sugarLevel = new RectF(100,100,150,150);
            b = getMaskedBitmap(get_mRes(), mSugarLevel, sugarLevel);

            //draw level
            canvas.drawBitmap(b, left_x, c_height - mSugarFrames.getHeight() - 20, paint);

            paint.setColor(Color.LTGRAY);
            paint.setTextSize(30);

            String text = "sugar: "+ m_flyModel.get_mSugar();
            canvas.drawText(text, left_x, c_height - mSugarFrames.getHeight() - 40, paint);
		}
        canvas.restore();
	}

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap getMaskedBitmap(Resources res, Bitmap source, RectF mask) {

        Paint paint = new Paint();

        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = null;

        if (source.isMutable()) {
            Log.println(Log.DEBUG,TAG,"---- is mutable ----");
            bitmap = source;
        } else {
            Log.println(Log.DEBUG,TAG,"---- is NOT mutable ----");
            bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
            //source.recycle();
        }

        bitmap.setHasAlpha(true);
        Canvas canvas = new Canvas(bitmap);
        //draw dest
        canvas.drawBitmap(bitmap, 0, 0, paint);
        paint.setARGB(255, 100, 0, 255);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //draw source
        canvas.drawRect(mask, paint);

        return bitmap;
    }

    public static Bitmap getMaskedBitmap(Resources res, int sourceResId, int maskResId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap source = BitmapFactory.decodeResource(res, sourceResId, options);
        Bitmap bitmap;
        if (source.isMutable()) {
            bitmap = source;
        } else {
            bitmap = source.copy(Bitmap.Config.ARGB_8888, true);
            source.recycle();
        }
        //bitmap.setHasAlpha(true);
        Canvas canvas = new Canvas(bitmap);
        Bitmap mask = BitmapFactory.decodeResource(res, maskResId);
        Paint paint = new Paint();

        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.OVERLAY));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mask, 0, 0, paint);
        mask.recycle();
        return bitmap;
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
	public void setFlyModel(FlyStatus m_flyModel) {
		this.m_flyModel = m_flyModel;
	}

    public void set_mRes(Resources _mRes) {
        this.mRes = _mRes;
    }

    public Resources get_mRes() {
        return mRes;
    }
}
