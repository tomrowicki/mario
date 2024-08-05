package tomrowicki.physics2d;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.joml.Vector2f;
import tomrowicki.engine.GameObject;
import tomrowicki.engine.Transform;
import tomrowicki.physics2d.components.Box2DCollider;
import tomrowicki.physics2d.components.CircleCollider;
import tomrowicki.physics2d.components.Rigidbody2D;

public class Physics2D {

    private Vec2 gravity = new Vec2(0, -10f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 120.0f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public void add(GameObject go) {
        Rigidbody2D rb = go.getComponent(Rigidbody2D.class);
        // Does this game object have rigid body and has not been added to the physics engine already?
        if (rb != null && rb.getRawBody() == null) {
            Transform transform = go.transform;
            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float) Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.bullet = rb.isContinuousCollision();

            switch (rb.getBodyType()) {
                case Kinematic -> bodyDef.type = BodyType.KINEMATIC;
                case Static -> bodyDef.type = BodyType.STATIC;
                case Dynamic -> bodyDef.type = BodyType.DYNAMIC;
            }

            PolygonShape shape = new PolygonShape();
            CircleCollider circleCollider;
            Box2DCollider box2DCollider;

            // new syntax
            if ((circleCollider = go.getComponent(CircleCollider.class)) != null ) {
                shape.setRadius(circleCollider.getRadius());
//                bodyDef.position.set(transform.position.x, transform.position.y);
            } else if ((box2DCollider = go.getComponent(Box2DCollider.class)) != null) {
                Vector2f halfSize = new Vector2f(box2DCollider.getHalfSize()).mul(0.5f);
                Vector2f offset = box2DCollider.getOffset();
                Vector2f origin = new Vector2f(box2DCollider.getOrigin());
                shape.setAsBox(halfSize.x, halfSize.y, new Vec2(origin.x, origin.y), 0);

                Vec2 pos = bodyDef.position;
                float xPos = pos.x + offset.x;
                float yPos = pos.y + offset.y;
                bodyDef.position.set(xPos, yPos);
            }

            Body body = this.world.createBody(bodyDef);
            rb.setRawBody(body);
            body.createFixture(shape, rb.getMass());
        }
    }

    public void update (float dt) {
        physicsTime += dt;
        // this is all necessary due to the fact that frames can be generated at slightly different intervals
        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    public void destroyGameObject(GameObject go) {
        Rigidbody2D rb = go.getComponent(Rigidbody2D.class);
        if (rb != null && rb.getRawBody() != null) {
            world.destroyBody(rb.getRawBody());
            rb.setRawBody(null);
        }
    }
}
