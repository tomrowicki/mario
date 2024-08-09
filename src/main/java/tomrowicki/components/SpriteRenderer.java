package tomrowicki.components;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import tomrowicki.editor.JImGui;
import tomrowicki.engine.Transform;
import tomrowicki.renderer.Texture;

public class SpriteRenderer extends Component {

    private Vector4f color = new Vector4f(1f, 1f, 1f, 1f);
    private Sprite sprite = new Sprite();

    private transient Transform lastTransform;
    private transient boolean isDirty = true;

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
    public void editorUpdate(float dt) {
        if (!this.lastTransform.equals(this.gameObject.transform)) {
            this.gameObject.transform.copy(this.lastTransform);
            isDirty = true;
        }
    }

    @Override
    public void imgui () {
        if (JImGui.colorPicker4("Color Picker", this.color)) {
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

    public void setTexture(Texture texture) {
        sprite.setTexture(texture);
    }

    public void setDirty() {
        isDirty = true;
    }
}
