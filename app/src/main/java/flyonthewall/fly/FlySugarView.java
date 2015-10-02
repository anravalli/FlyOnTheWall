package flyonthewall.fly;

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
import android.graphics.Region;
import android.os.Build;
import android.util.Log;

import java.util.ArrayList;

import FlyOnTheWall.pkg.R;
import flyonthewall.ViewManager;
import flyonthewall.base.EntityView;

public class FlySugarView extends EntityView {

	private static final String TAG = FlyView.class.getSimpleName();;
    private final String name = "fly_sugar";
    //private RectF mScratchRect = new RectF(0, 0, 0, 0);
    Bitmap mSugarFrame = null;
    Bitmap mSugarLevel = null;
    ArrayList<Bitmap> mSugarFrames = null;
    int m_anim_index;
    int m_anim_alpha;
    private FlyStatus m_flyModel = null;
	private int left_x = 0;
	private int top_y = 0;
	private int lenght = 100;
	private int height = 10;
	private Paint mLinePaint;


    public FlySugarView() {
		Log.d(TAG, "FlySugarView - constructor");
        // Initialize paints for speedometer
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setARGB(128, 0, 255, 0);
        mRes = ViewManager.getViewManager().getViewRes();

        mSugarFrame = BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_fly_dark);
        mSugarLevel =  BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_level_green);

        mSugarFrames = new ArrayList<Bitmap>(11);
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f01));
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f02));
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f03));
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f04));
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f05));
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f06));
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f07));
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f08));
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f09));
        mSugarFrames.add(BitmapFactory.decodeResource(get_mRes(), R.drawable.sugar_anim_f10));
        mSugarFrames.add(null);
        m_anim_index = 10;
        m_anim_alpha = 255;

        ViewManager.getViewManager().register(name, this);

	}
	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap getSugarGaugeGreen(Bitmap bkground, Bitmap level, RectF mask) {

        Paint paint = new Paint();

        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = null;

        if (bkground.isMutable()) {
            //Log.println(Log.DEBUG,TAG,"---- is mutable ----");
            bitmap = bkground;
        } else {
            //Log.println(Log.DEBUG,TAG,"---- is NOT mutable ----");
            bitmap = bkground.copy(Bitmap.Config.ARGB_8888, true);
        }
        //bitmap=Bitmap.createBitmap(bkground.getWidth(),bkground.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        canvas.clipRect(mask, Region.Op.DIFFERENCE);
        canvas.drawBitmap(level, 0, 0, paint);

        return bitmap;
    }

    public static Bitmap getSugarGaugeGreen_Composition(Bitmap bkground, RectF mask) {

        Paint paint = new Paint();

        BitmapFactory.Options options = new BitmapFactory.Options();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            options.inMutable = true;
        }
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;

        Bitmap bitmap = null;
        if (bkground.isMutable()) {
            //Log.println(Log.DEBUG,TAG,"---- is mutable ----");
            bitmap = bkground;
        } else {
            //Log.println(Log.DEBUG,TAG,"---- is NOT mutable ----");
            bitmap = bkground.copy(Bitmap.Config.ARGB_8888, true);
        }

        //bitmap.setHasAlpha(true);
        Canvas canvas = new Canvas(bitmap);

        //draw dest
        //canvas.drawBitmap(progress, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //draw source
        paint.setARGB(128, 0, 255, 0);
        canvas.drawRect(mask, paint);
        return bitmap;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static Bitmap getSugarGauge_Animated(Bitmap background, Bitmap foreground, int alpha) {
        Paint paint = new Paint();

        Bitmap bckg = Bitmap.createBitmap(background.getWidth(), background.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bckg);
        //draw dest
        paint.setAlpha(alpha);
        canvas.drawBitmap(background, 0, 0, paint);
        //paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        //draw source
        if (foreground != null)
            canvas.drawBitmap(foreground, 0, 0, null);

        return bckg;
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
    public void draw(Canvas canvas) {
        canvas.save();

        //int c_width = canvas.getWidth();
        int c_height = canvas.getHeight();
        top_y = c_height - 20 - height;
        left_x = 20;

        Paint paint = new Paint();

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        synchronized (m_flyModel) {

            //mScratchRect.set(left_x, top_y, (float) (left_x + m_flyModel.get_mSugar()), top_y - height);

            if (!(m_flyModel.get_sugar() < 0)) {
                dummyDraw(canvas, c_height);

                Bitmap b = calculateAnimation(m_flyModel.get_sugar(), mRes);
                canvas.drawBitmap(b, left_x + mSugarFrame.getWidth() + 20, c_height - mSugarFrame.getHeight() - 20, paint);
            } else {
                dummyDraw(canvas, c_height);

                Bitmap b = mSugarFrames.get(mSugarFrames.size() - 2);
                paint.setAlpha(1);
                canvas.drawBitmap(b, left_x + mSugarFrame.getWidth() + 20, c_height - mSugarFrame.getHeight() - 20, paint);
            }
            paint.setAlpha(255);
            paint.setColor(Color.LTGRAY);
            paint.setTextSize(30);
            String text = "sugar: " + m_flyModel.get_sugar();
            canvas.drawText(text, left_x, c_height - mSugarFrame.getHeight() - 40, paint);
        }
        canvas.restore();
    }

    Bitmap calculateAnimation(int sugar, Resources res) {

        int top_idx = 0;
        int bot_idx = 0;
        int sugar_frag = m_flyModel.get_max_sugar() / 10;
        int num_alpha_frags = 255;
        float alpha_frag = ((float) sugar_frag) / 255;
        if ((m_anim_index - 1) * sugar_frag <= sugar) {
            // keep frame
            // animate alpha
            m_anim_alpha = (int) ((sugar - (m_anim_index - 1) * sugar_frag) / alpha_frag);
            //m_anim_alpha = (int)(alpha_frag * num_alpha_frags);
        } else {
            m_anim_index--;
            if (m_anim_index == 0) m_anim_index = 10;
            //reset alpha
            m_anim_alpha = 255;
        }
        //set frames
        if (m_anim_index == 1) {
            bot_idx = 9;
            top_idx = 10;
        } else {
            bot_idx = 10 - m_anim_index;
            top_idx = 11 - m_anim_index;
        }

        return getSugarGauge_Animated(mSugarFrames.get(bot_idx),
                mSugarFrames.get(top_idx),
                m_anim_alpha);
    }

    private void dummyDraw(Canvas canvas, int c_height) {

        Paint paint = new Paint();
        RectF sugarLevel = new RectF(0, 0,
                mSugarFrame.getWidth(),
                mSugarFrame.getHeight() - (mSugarFrame.getHeight() * (float) m_flyModel.get_sugar() / 500));

        Bitmap b = getSugarGaugeGreen(mSugarLevel, mSugarFrame, sugarLevel);
        canvas.drawBitmap(b, left_x, c_height - mSugarFrame.getHeight() - 20, paint);

        //b = getSugarGaugeGreen_Composition(mSugarFrame, sugarLevel);
        //canvas.drawBitmap(b, left_x+ mSugarFrame.getWidth()+20, c_height - mSugarFrame.getHeight() - 20, paint);
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

    /*public void set_mRes(Resources _mRes) {
        this.mRes = _mRes;
    }*/

    public Resources get_mRes() {
        return mRes;
    }
}
