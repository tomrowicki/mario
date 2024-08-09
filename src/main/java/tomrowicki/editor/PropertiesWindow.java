package tomrowicki.editor;

import imgui.ImGui;
import tomrowicki.components.NonPickable;
import tomrowicki.engine.GameObject;
import tomrowicki.engine.MouseListener;
import tomrowicki.physics2d.components.Box2DCollider;
import tomrowicki.physics2d.components.CircleCollider;
import tomrowicki.physics2d.components.Rigidbody2D;
import tomrowicki.renderer.PickingTexture;
import tomrowicki.scenes.Scene;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class PropertiesWindow {

    private GameObject activeGameObject = null;
    private PickingTexture pickingTexture;

    private float debounce = 0.2f;

    public PropertiesWindow(PickingTexture pickingTexture) {
        this.pickingTexture = pickingTexture;
    }

    public void update(float dt, Scene currentScene) {
        debounce -= dt;

        if (!MouseListener.isDragging() && MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_LEFT) && debounce < 0) {
            int x = (int) MouseListener.getScreenX();
            int y = (int) MouseListener.getScreenY();
            int gameObjectId = pickingTexture.readPixel(x, y);
            GameObject pickedObject = currentScene.getGameObject(gameObjectId);
            if (pickedObject != null && pickedObject.getComponent(NonPickable.class) == null) {
                activeGameObject = pickedObject;
            } else if (pickedObject == null && !MouseListener.isDragging()) {
                activeGameObject = null;
            }
            debounce = 0.2f;
        }
    }

    public void imgui() {
        if (activeGameObject != null) {
            ImGui.begin("Properties");

            if (ImGui.beginPopupContextWindow("ComponentAdder")) {
                if (ImGui.menuItem("Add Rigidbody")) {
                    if (activeGameObject.getComponent(Rigidbody2D.class) == null) {
                        activeGameObject.addComponent(new Rigidbody2D());
                    }
                }

                if (ImGui.menuItem("Add Box Collider")) {
                    if (activeGameObject.getComponent(Box2DCollider.class) == null &&
                            activeGameObject.getComponent(CircleCollider.class) == null) {
                        activeGameObject.addComponent(new Box2DCollider());
                    }
                }

                if (ImGui.menuItem("Add Circle Collider")) {
                    if (activeGameObject.getComponent(CircleCollider.class) == null &&
                            activeGameObject.getComponent(Box2DCollider.class) == null) {
                        activeGameObject.addComponent(new CircleCollider());
                    }
                }

                ImGui.endPopup();
            }

            activeGameObject.imgui();
            ImGui.end();
        }
    }

    public GameObject getActiveGameObject() {
        return activeGameObject;
    }

    public void setActiveGameObject(GameObject go) {
        activeGameObject = go;
    }
}
