package tomrowicki.components;

import org.joml.Vector2f;
import org.joml.Vector4f;
import tomrowicki.editor.PropertiesWindow;
import tomrowicki.engine.*;

import static org.lwjgl.glfw.GLFW.*;

public class Gizmo extends Component {

    private Vector4f xAxisColor = new Vector4f(1f, 0.3f, 0.3f, 1f);
    private Vector4f xAxisColorHover = new Vector4f(1, 0, 0, 1);
    private Vector4f yAxisColor = new Vector4f(0.3f, 1, 0.3f, 1);
    private Vector4f yAxisColorHover = new Vector4f(0, 1, 0, 1);

    private GameObject xAxisObject;
    private GameObject yAxisObject;
    private SpriteRenderer xAxisSprite;
    private SpriteRenderer yAxisSprite;
    protected GameObject activeGameObject = null;

    private Vector2f xAxisOffset = new Vector2f(24f / 80f, -6f / 80f);
    private Vector2f yAxisOffset = new Vector2f(-7f / 80f, 21f / 80f);

    private float gizmoWidth = 16 / 80f; // in world units
    private float gizmoHeight = 48 / 80f;

    protected boolean xAxisActive = false;
    protected boolean yAxisActive = false;

    private boolean using = false;

    private PropertiesWindow propertiesWindow;

    public Gizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        this.xAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);
        this.yAxisObject = Prefabs.generateSpriteObject(arrowSprite, gizmoWidth, gizmoHeight);
        this.xAxisSprite = this.xAxisObject.getComponent(SpriteRenderer.class);
        this.yAxisSprite = this.yAxisObject.getComponent(SpriteRenderer.class);
        this.propertiesWindow = propertiesWindow;

        this.xAxisObject.addComponent(new NonPickable());
        this.yAxisObject.addComponent(new NonPickable());

        Window.getScene().addGameObjectToScene(this.xAxisObject);
        Window.getScene().addGameObjectToScene(this.yAxisObject);
    }

    @Override
    public void start() {
        xAxisObject.transform.rotation = 90; // in degrees
        yAxisObject.transform.rotation = 180;
        xAxisObject.transform.zIndex = 100;
        yAxisObject.transform.zIndex = 100;
        xAxisObject.setNoSerialize();
        yAxisObject.setNoSerialize();
    }

    @Override
    public void update(float dt) {
        if (using) {
            setInactive();
        }
    }

    @Override
    public void editorUpdate(float dt) {
        if (!using) {
            return;
        }

        activeGameObject = propertiesWindow.getActiveGameObject();
        if (activeGameObject != null) {
            setActive();

            // TODO: move this into another component
            if (KeyListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                KeyListener.keyBeginPress(GLFW_KEY_D)) {
                GameObject newObj = this.activeGameObject.copy();
                Window.getScene().addGameObjectToScene(newObj);
                newObj.transform.position.add(0.1f, 0.1f);
                this.propertiesWindow.setActiveGameObject(newObj);
                return;
            } else if (KeyListener.keyBeginPress(GLFW_KEY_DELETE)) {
                activeGameObject.destroy();
                this.setInactive();
                this.propertiesWindow.setActiveGameObject(null);
            }
        } else {
            setInactive();
            return;
        }

        boolean xAxisHot = checkXHoverState(); // being hovered over
        boolean yAxisHot = checkYHoverState();

        if ((xAxisHot || xAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            xAxisActive = true; // being dragged
            yAxisActive = false;
        } else if ((yAxisHot || yAxisActive) && MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT)) {
            yAxisActive = true;
            xAxisActive = false;
        } else {
            xAxisActive = false;
            yAxisActive = false;
        }

        if (activeGameObject != null) {
            xAxisObject.transform.position.set(activeGameObject.transform.position);
            yAxisObject.transform.position.set(activeGameObject.transform.position);
            xAxisObject.transform.position.add(xAxisOffset);
            yAxisObject.transform.position.add(yAxisOffset);
        }
    }

    private void setActive() {
        this.xAxisSprite.setColor(xAxisColor);
        this.yAxisSprite.setColor(yAxisColor);
    }

    private void setInactive() {
        activeGameObject = null;
        xAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
        yAxisSprite.setColor(new Vector4f(0, 0, 0, 0));
    }

    private boolean checkXHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= xAxisObject.transform.position.x + (gizmoHeight / 2.0f) &&
                mousePos.x >= xAxisObject.transform.position.x - (gizmoWidth / 2.0f) &&
                mousePos.y >= xAxisObject.transform.position.y - (gizmoHeight / 2.0f) &&
                mousePos.y <= xAxisObject.transform.position.y + (gizmoWidth / 2.0f)) {
            xAxisSprite.setColor(xAxisColorHover);
            return true;
        }
        xAxisSprite.setColor(xAxisColor);
        return false;
    }

    private boolean checkYHoverState() {
        Vector2f mousePos = new Vector2f(MouseListener.getOrthoX(), MouseListener.getOrthoY());
        if (mousePos.x <= yAxisObject.transform.position.x + (gizmoWidth / 2.0f) &&
                mousePos.x >= yAxisObject.transform.position.x - (gizmoWidth / 2.0f) &&
                mousePos.y <= yAxisObject.transform.position.y + (gizmoHeight / 2.0f) &&
                mousePos.y >= yAxisObject.transform.position.y - (gizmoHeight / 2.0f)) {
            yAxisSprite.setColor(yAxisColorHover);
            return true;
        }
        yAxisSprite.setColor(yAxisColor);
        return false;
    }

    public void setUsing() {
        this.using = true;
    }

    public void setNotUsing() {
        this.using = false;
        this.setInactive();
    }
}
