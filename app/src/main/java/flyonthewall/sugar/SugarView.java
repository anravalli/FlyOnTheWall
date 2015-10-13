package flyonthewall.sugar;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;

import FlyOnTheWall.pkg.R;
import flyonthewall.ViewManager;
import flyonthewall.base.EntityView;

/**
 * Created by andrea on 27/09/15.
 */
public class SugarView extends EntityView {
    private static final String TAG = SugarView.class.getSimpleName();
    ArrayList<Bitmap> mSugarFrames = null;
    int m_anim_index;
    int m_anim_alpha;
    //private SugarEntityModel m_flyModel = null;
    private Paint mLinePaint;

    private String name = "sugar";


    public SugarView(SugarEntityModel model) {
        Log.d(TAG, "FlySugarView - constructor");
        // Initialize paints for speedometer
        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setARGB(128, 0, 255, 0);
        mRes = ViewManager.getViewManager().getViewRes();

        //initialize model
        mEntityModel = model;

        mSugarFrames = new ArrayList<Bitmap>(11);
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f01));
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f02));
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f03));
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f04));
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f05));
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f06));
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f07));
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f08));
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f09));
        mSugarFrames.add(BitmapFactory.decodeResource(mRes, R.drawable.sugar_anim_f10));
        mSugarFrames.add(null);
        m_anim_index = 10;
        m_anim_alpha = 255;

        //initialize pivot point
        int d_x = mSugarFrames.get(0).getWidth() / 2;
        int d_y = mSugarFrames.get(0).getHeight() / 2;
        mPivot = new Point(d_x, d_y);

        name = mEntityModel.get_ename();

        ViewManager.getViewManager().register(name, this);

    }

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

    Bitmap calculateAnimation(int sugar, Resources res) {
        int top_idx = 0;
        int bot_idx = 0;
        int sugar_frag = ((SugarEntityModel) mEntityModel).get_max_sugar() / 10;
        //int num_alpha_frags = 255;
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

    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        Paint paint = new Paint();

        if (android.os.Build.VERSION.SDK_INT >= 11) {
            //setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        SugarEntityModel model = get_model();
        synchronized (mEntityModel) {
            int real_x = model.get_x() + model.get_origin().x;
            int real_y = model.get_y() + model.get_origin().y;
            if (!(model.get_sugar() < 0)) {
                Bitmap b = calculateAnimation(model.get_sugar(), mRes);
                canvas.drawBitmap(b, real_x, real_y, paint);
            } else {
                Bitmap b = mSugarFrames.get(mSugarFrames.size() - 2);
                paint.setAlpha(0);
                canvas.drawBitmap(b, real_x - mPivot.x, real_y - mPivot.y, paint);
            }
            paint.setAlpha(255);
            paint.setColor(Color.LTGRAY);
            paint.setTextSize(30);
            String text = "sugar: " + model.get_sugar();
            canvas.drawText(text, model.get_x(), model.get_y() - 15, paint);
        }
        canvas.restore();
    }


    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }

    public SugarEntityModel get_model() {
        return (SugarEntityModel) mEntityModel;
    }

    public void set_model(SugarEntityModel m_model) {
        this.mEntityModel = m_model;
    }

}
