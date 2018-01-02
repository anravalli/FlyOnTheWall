/*
 *	Fly On The Wall: a Fly survival game :-)
 *
 *	Copyright 2012 - 2018 Andrea Ravalli <anravalli @ gmail.com>
 *
 *	This file is part of Fly On The Wall.
 *
 *	Fly On The Wall is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.

 *	Fly On The Wall is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.

 *	You should have received a copy of the GNU General Public License
 *	along with Fly On The Wall.  If not, see <http://www.gnu.org/licenses/>.
*/

package flyOnTheWall.dbg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;

import flyOnTheWall.InputDispatcher;
import flyOnTheWall.base.OnTouchCallback;

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
        return PixelFormat.UNKNOWN;
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
