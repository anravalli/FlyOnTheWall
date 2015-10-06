package flyonthewall.gamearea;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;

import flyonthewall.ViewManager;
import flyonthewall.base.EntityView;

/**
 * Created by andrea on 05/10/15.
 */
public class GameFakeView extends EntityView {

    private Rect areaBounds = null;
    private String name = "areabounds";

    public GameFakeView(GameAreaModel model) {
        mEntityModel = model;

        areaBounds = new Rect(
                model.get_x() + (int) (model.get_width() * 0.1),
                model.get_y() + (int) (model.get_height() * 0.1),
                model.get_x() + (int) (model.get_width() * 0.8),
                model.get_y() + (int) (model.get_height() * 0.8)
        );

        ViewManager.getViewManager().register(name, this);
    }

    public Rect getAreaBounds() {
        return areaBounds;
    }

    public void setAreaBounds(Rect areaBounds) {
        this.areaBounds = areaBounds;
    }

    @Override
    public Rect getBoundingBox(int tolerance) {
        return areaBounds;
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.save();

        Paint paint = new Paint();
        Paint.Style style = Paint.Style.STROKE;
        paint.setColor(Color.BLUE);
        paint.setAlpha(255);
        paint.setStyle(style);

        synchronized (mEntityModel) {
            canvas.drawRect(areaBounds, paint);
        }
        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
