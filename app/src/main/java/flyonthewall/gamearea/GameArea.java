package flyonthewall.gamearea;

import android.graphics.Point;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import flyonthewall.base.Entity;
import flyonthewall.base.EntityModel;
import flyonthewall.base.EntityStateMachine;
import flyonthewall.base.EntityType;

/**
 * Created by andrea on 05/10/15.
 */
public class GameArea extends Entity {
    private static final String TAG = GameArea.class.getSimpleName();
    private final GameFakeView m_FakeView;
    private final GameAreaModel mFakeStatus;
    private final int mTolerance = 0;

    public GameArea(GameAreaModel model) {
        super("area", EntityType.GameArea);
        mFakeStatus = model;
        m_FakeView = new GameFakeView(model);

        currentState = new EntityStateMachine() {

            private final String m_name = "gamearea";

            @Override
            public void manageCollision(HashMap<String, Entity> details) {
                Collection<Entity> entities = details.values();
                Iterator<Entity> entity_it = entities.iterator();

                while (entity_it.hasNext()) {
                    Entity e = entity_it.next();
                    Log.d("GameArea StateMachine", "collision with: " + e.getName() + " (" + e.getType() + ")");
                    if (e.getType() == EntityType.Fly) {
                        //move the game area in the same direction of the fly
                        //if (e.getBounding_box().contains())
                        //((GameAreaModel)m_model).get_width();
                        //((GameAreaModel)m_model).get_height();
                    }
                }

            }

            @Override
            public void enterState(EntityModel status) {
                m_model = status;
            }

            @Override
            public void exitState() {

            }

            @Override
            public void updatePosition() {

            }

            @Override
            public void update() {

            }
        };

        register();
        //registerToEvent();
        registerToMessages();
    }

    @Override
    public synchronized void update(Point mOrigin) {
        bounding_box = m_FakeView.getBoundingBox(mTolerance);
    }

}
