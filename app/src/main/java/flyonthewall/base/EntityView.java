package flyonthewall.base;

import android.content.res.Resources;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;

/**
 * Created by andrea on 16/09/15.
 */
public abstract class EntityView extends Drawable {

    protected Resources mRes;
    protected EntityModel mEntityModel = null;
    protected Point mPivot = null;

    /**
     * returns the object bounding box reduced by the tolerance value.
     * tolerance value is positive in between 0 and pivot
     *
     * @param tolerance set collision tolerance
     * @return bounding box rectangle
     */
    public synchronized Rect getBoundingBox(int tolerance) {
        return new Rect(
                mEntityModel.get_x() - (mPivot.x - tolerance),
                mEntityModel.get_y() - (mPivot.y - tolerance),
                mEntityModel.get_x() + (mPivot.x - tolerance),
                mEntityModel.get_y() + (mPivot.y - tolerance)
        );
    }

    public synchronized Path getBoundingPath(int tolerance) {
        Path p = new Path();
        p.addRect(
                toLeft(mEntityModel.get_x()) + tolerance,
                toTop(mEntityModel.get_y()) + tolerance,
                toRight(mEntityModel.get_x()) - tolerance,
                toBottom(mEntityModel.get_y()) - tolerance,
                Path.Direction.CW
        );
        //RectF r = mapToViewF(getBoundingBox(tolerance));
        //p.addRect(r,Path.Direction.CW);

        return p;
    }

    public int mapToViewX(int x) {
        return x + mEntityModel.get_origin().x;
    }

    public int mapToViewY(int y) {
        return y + mEntityModel.get_origin().y;
    }

    public Point mapToView(Point p) {
        int x = p.x + mEntityModel.get_origin().x;
        int y = p.y + mEntityModel.get_origin().y;
        return new Point(x, y);
    }

    public Rect mapToView(Rect r) {
        Rect rv = new Rect(
                r.left + mEntityModel.get_origin().x,
                r.top + mEntityModel.get_origin().y,
                r.right + mEntityModel.get_origin().x,
                r.bottom + mEntityModel.get_origin().y
        );
        return rv;
    }

    public RectF mapToView(RectF r) {
        RectF rv = new RectF(
                r.left + mEntityModel.get_origin().x,
                r.top + mEntityModel.get_origin().y,
                r.right + mEntityModel.get_origin().x,
                r.bottom + mEntityModel.get_origin().y
        );
        return rv;
    }

    public RectF mapToViewF(Rect r) {
        RectF rv = new RectF(
                r.left + mEntityModel.get_origin().x,
                r.top + mEntityModel.get_origin().y,
                r.right + mEntityModel.get_origin().x,
                r.bottom + mEntityModel.get_origin().y
        );
        return rv;
    }

    public int toLeft(int x) {
        return x - mPivot.x;
    }

    public int toTop(int y) {
        return y - mPivot.y;
    }

    public int toLeft(float x) {
        return (int) x - mPivot.x;
    }

    public int toTop(float y) {
        return (int) y - mPivot.y;
    }

    public int toRight(int x) {
        return x + mPivot.x;
    }

    public int toBottom(int y) {
        return y + mPivot.y;
    }

    public Point toLeftTop(Point p) {
        int x = p.x - mPivot.x;
        int y = p.y - mPivot.y;
        return new Point(x, y);
    }

    public Point toRightBottom(Point p) {
        int x = p.x + mPivot.x;
        int y = p.y + mPivot.y;
        return new Point(x, y);
    }
}
