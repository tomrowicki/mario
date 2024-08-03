package tomrowicki.physics2d;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;

public class Physics2D {

    private Vec2 gravity = new Vec2(0, -10f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 120.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public void update (float dt) {
        physicsTime += dt;
        // this is all necessary due to the fact that frames can be generated at slightly different intervals
        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }
}
