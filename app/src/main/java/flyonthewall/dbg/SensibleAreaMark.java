package flyonthewall.dbg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.MotionEvent;

import flyonthewall.InputDispatcher;
import flyonthewall.ViewManager;
import flyonthewall.base.EntityView;
import flyonthewall.base.OnTouchCallback;
import flyonthewall.fly.FlyView;

public class SensibleAreaMark extends EntityView {
    private static final String TAG = SensibleAreaMark.class.getSimpleName();
    private static SensibleAreaMark m_tmark = null;
    private float m_x;
    private float m_y;
    private boolean enable = false;
    private FlyView flyView = null;
    private int sensitivity = 0;
    private int fade_t = 100;
    private int fade_t_max = 100;
    private String name = "sens_area";

    private SensibleAreaMark() {
        m_x = 300;
        m_y = 300;

        registerToEvent();
        ViewManager.getViewManager().register(name, this);
    }

    public static SensibleAreaMark getMarker() {
        if (m_tmark == null)
            m_tmark = new SensibleAreaMark();
        return m_tmark;
    }

    public void setSensitivity(int s) {
        sensitivity = s;
    }

    public void setFlyView(FlyView flyView) {
        this.flyView = flyView;
    }

    private void setEnable(boolean e) {

        Log.d(TAG, "set to: " + e);
        if (flyView != null) {
            enable = e;
        }
        if (enable) {
            fade_t = fade_t_max;
        }
    }

    @Override
    public void draw(Canvas canva) {
        if (!enable)
            return;

        canva.save();
        Paint paint = new Paint();
        Style style = Style.STROKE;
        int alpha = 255 / fade_t_max * fade_t;
        paint.setColor(Color.RED);
        paint.setAlpha(alpha);
        paint.setStyle(style);

        fade_t--;
        if (alpha == 0) {
            setEnable(false);
        }

        canva.drawRect(flyView.getSensitiveArea(sensitivity), paint);
        canva.restore();
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

    void registerToEvent() {
        InputDispatcher.getInputDispatcher().registerToTouchEvent(name, new OnTouchCallback() {
            public void onTouch(MotionEvent event) {
                setEnable(true);
            }
        });
    }
}
