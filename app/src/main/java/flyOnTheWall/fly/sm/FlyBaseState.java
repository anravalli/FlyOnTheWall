package flyOnTheWall.fly.sm;

import flyOnTheWall.base.EntityModel;
import flyOnTheWall.base.EntityStateMachine;
import flyOnTheWall.fly.FlyStatus;

/**
 * Created by andrea on 04/10/15.
 */
public abstract class FlyBaseState extends EntityStateMachine {


    public void consumeSugar() {
        ((FlyStatus) m_model).set_sugar(((FlyStatus) m_model).get_sugar() - m_sugarConsumeSpeed);
    }

    @Override
    public void enterState(EntityModel status) {
        m_model = status;
    }
}
