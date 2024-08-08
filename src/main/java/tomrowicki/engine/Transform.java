package tomrowicki.engine;

import org.joml.Vector2f;
import tomrowicki.components.Component;
import tomrowicki.editor.JImGui;

public class Transform extends Component {

    public Vector2f position;
    public Vector2f scale;

    public float rotation = 0;
    public int zIndex ;

    public Transform() {
        init(new Vector2f(), new Vector2f());
    }

    public Transform(Vector2f position) {
        init(position, new Vector2f());
    }

    public Transform(Vector2f position, Vector2f scale) {
        init(position, scale);
    }

    public void init(Vector2f position, Vector2f scale) {
        this.position = position;
        this.scale = scale;
        zIndex = 0;
    }

    @Override
    public void imgui() {
        gameObject.name = JImGui.inputText("Name: ", gameObject.name);
        JImGui.drawVec2Control("Position", this.position);
        JImGui.drawVec2Control("Scale", this.scale, 32.0f);
        rotation = JImGui.dragFloat("Rotation", this.rotation);
        zIndex = JImGui.dragInt("Z-Index", this.zIndex);
    }

    public Transform copy() {
        return new Transform(new Vector2f(this.position), new Vector2f(this.scale));
    }

    public void copy(Transform to) {
        to.position.set(this.position);
        to.scale.set(this.scale);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!(obj instanceof Transform)) return false;
        Transform other = (Transform) obj;
        return this.position.equals(other.position) && this.scale.equals(other.scale)
                && this.zIndex == other.zIndex && this.rotation == other.rotation;
    }
}
