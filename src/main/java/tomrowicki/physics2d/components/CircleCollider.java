package tomrowicki.physics2d.components;

import tomrowicki.components.Component;

public class CircleCollider extends Collider {

    private float radius = 1f;

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
}
