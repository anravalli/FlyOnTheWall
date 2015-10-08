package flyonthewall;

/**
 * Created by andrea on 08/10/15.
 */
public class GameModel {
    private GameStatus mStatus = GameStatus.stopped;


    public synchronized GameStatus getStatus() {
        return mStatus;
    }

    public synchronized void setStatus(GameStatus mStatus) {
        this.mStatus = mStatus;
    }
}
