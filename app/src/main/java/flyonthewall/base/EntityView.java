package flyonthewall.base;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
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

}
