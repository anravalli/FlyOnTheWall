package flyonthewall.base;

import android.graphics.Rect;

import flyonthewall.EntityManager;

/**
 * Created by andrea on 17/09/15.
 */
public class Entity {

    protected String name = "";
    protected EntityType type = EntityType.None;
    protected Rect bounding_box = null;
    //protected EntityManager manager = null;

    public Entity(String n_name, EntityType n_type) {
        this.type = n_type;
        this.name = n_name;
        //this.manager = EntityManager.getEntityManager();
        //register();
    }

    protected void register() {
        EntityManager.getEntityManager().registerEntity(this);
    }

    protected void unregister() {
        EntityManager.getEntityManager().unregisterEntity(name);
    }

    public synchronized void update() {
        //TODO check for better construct (abstract, etc)
        //by default do nothing
    }

    public String getName() {
        return name;
    }

    public EntityType getType() {
        return type;
    }
}
