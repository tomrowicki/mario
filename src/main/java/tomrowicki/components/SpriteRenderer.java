package tomrowicki.components;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import tomrowicki.engine.Component;
import tomrowicki.engine.Transform;
import tomrowicki.renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;


//    public SpriteRenderer(Vector4f color) {
//        this.color = color;
//        this.sprite = null;
//        this.isDirty = true;
//    }
//
//    public SpriteRenderer(Sprite sprite) {
//        this.sprite = sprite;
//        this.color = new Vector4f(1.0f, 1.0f, 1.0f, 1.0f);
//        this.isDirty = true;
//    }

    @Override
    public void start() {
        this.lastTransform = gameObject.transform.copy();
    }

    @Override
    public void update(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imgui () {
        float[] imColor = {color.x, color.y, color.z, color.w};
        if (ImGui.colorPicker4("Color picker: ", imColor)) {
            this.color.set(imColor[0], imColor[1], imColor[2], imColor[3]);
            isDirty = true;
        }

    }

    public Vector4f getColor() {
        return color;
    }

    public Texture getTexture() {
        return sprite.getTexture();
    }

    public Vector2f[] getTexCoords() {
        return sprite.getTexCoords();
    }

    public void setSprite(Sprite sprite) {
        this.sprite = sprite;
        this.isDirty = true;
    }

    public void setColor(Vector4f color) {
        if (!this.color.equals(color)) {
            this.color.set(color);
            this.isDirty = true;
        }
    }

    public boolean isDirty() {
        return this.isDirty;
    }

    public void setClean() {
        isDirty = false;
    }


}
