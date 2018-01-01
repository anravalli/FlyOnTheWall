package flyonthewall.dbg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.MotionEvent;

import flyonthewall.InputDispatcher;
import flyonthewall.base.OnTouchCallback;

public class TouchMark extends Drawable {
    private float m_x;
    private float m_y;

    private static TouchMark m_tmark = null;

    public static TouchMark getMarker() {
        if (m_tmark == null)
            m_tmark = new TouchMark();
        return m_tmark;
    }

    TouchMark() {
        m_x = 300;
        m_y = 300;

        registerToEvent();
    }

    public void setTouch(float x, float y) {
        m_x = x;
        m_y = y;
    }

    @Override
    public void draw(Canvas canva) {
        canva.save();
        Paint paint = new Paint();

        Style style = Style.FILL_AND_STROKE;
        paint.setColor(Color.RED);
        paint.setStyle(style);
        canva.drawCircle(m_x, m_y, 5, paint);
        paint.setTextSize(30);

        String text = "x: " + m_x + " | y: " + m_y;
        canva.drawText(text, 30, 630, paint);
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
        InputDispatcher.getInputDispatcher().registerToTouchEvent("touch_mark", new OnTouchCallback() {
            public void onTouch(MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    setTouch((int) event.getX(), (int) event.getY());
                } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    setTouch((int) event.getX(), (int) event.getY());
                }
            }
        });
    }
    
    /*
     * canvas.drawCircle(x, y, 5, paint);
        paint.setColor(Color.RED);
        if (m_cur_y!=m_flyModel.get_y()){
            m_old_y = m_cur_y;
            m_cur_y = (int) m_flyModel.get_y();
        }
        if (m_cur_x!=m_flyModel.get_x()){
            m_old_x = m_cur_x;
            m_cur_x = (int)m_flyModel.get_x();
        }
        canvas.drawLine(m_old_x, m_old_y, m_cur_x, m_cur_y, paint);
        
        //canvas.drawLine(x, y, m_cur_x, m_cur_y, paint);

     */
}
