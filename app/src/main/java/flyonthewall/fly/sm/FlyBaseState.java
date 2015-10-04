package flyonthewall.fly.sm;

import flyonthewall.base.EntityStateMachine;
import flyonthewall.fly.FlyStatus;

/**
 * Created by andrea on 04/10/15.
 */
public abstract class FlyBaseState extends EntityStateMachine {

    protected FlyStatus m_flyModel = null;

    public void consumeSugar() {
        m_flyModel.set_sugar(m_flyModel.get_sugar() - m_sugarConsumeSpeed);
    }

    @Override
    public void enterState(FlyStatus status) {
        m_model = status;
    }
}
