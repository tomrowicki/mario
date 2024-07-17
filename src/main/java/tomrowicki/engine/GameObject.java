package tomrowicki.engine;

import java.util.ArrayList;
import java.util.List;

public class GameObject {

    private String name;
    private List<Component> components;

    public Transform transform;
    private int zIndex;

    public GameObject(String name) {
        this.components = new ArrayList<>();
        this.name = name;
        this.transform = new Transform();
        this.zIndex = 0;
    }

    public GameObject(String name, Transform transform, int zIndex) {
        this.components = new ArrayList<>();
        this.name = name;
        this.transform = transform;
        this.zIndex = zIndex;
    }

    public <T extends Component> T getComponent(Class<T> componentClass) {
        for (Component component : components) {
            if (componentClass.isAssignableFrom(component.getClass())) {
                try {
                    return componentClass.cast(component);
                } catch (ClassCastException e) {
                    e.printStackTrace();
                    assert false : "Error: Casting component.";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass) {
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            if (componentClass.isAssignableFrom(component.getClass())) {
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component component) {
        this.components.add(component);
        component.gameObject = this;
    }

    public void update(float dt) {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).update(dt);
        }
    }

    public void start() {
        for (int i = 0; i < components.size(); i++) {
            components.get(i).start();
        }
    }

    public void imgui() {
        for (Component c : components) {
            c.imgui();
        }
    }

    public int getZindex() {
        return zIndex;
    }
}
