package tomrowicki.engine;

import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector4f;
import tomrowicki.components.RigidBody;
import tomrowicki.components.Sprite;
import tomrowicki.components.SpriteRenderer;
import tomrowicki.components.Spritesheet;
import tomrowicki.util.AssetPool;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet sprites;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();
        this.camera = new Camera(new Vector2f(-250, 0));
        if (levelLoaded) {
            this.activeGameObject = gameObjects.get(0);
            return;
        }

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 1);
        SpriteRenderer obj1SpriteRenderer = new SpriteRenderer();
        obj1SpriteRenderer.setColor(new Vector4f(1, 0, 0, 1));
        obj1.addComponent(obj1SpriteRenderer);
        obj1.addComponent(new RigidBody());
        this.addGameObjectToScene(obj1);
        this.activeGameObject = obj1;

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 2);
        SpriteRenderer obj2SpriteRenderer = new SpriteRenderer();
        Sprite obj2Sprite = new Sprite();
        obj2Sprite.setTexture(AssetPool.getTexture("assets/images/blendImage2.png"));
        obj2SpriteRenderer.setSprite(obj2Sprite);

        obj2.addComponent(obj2SpriteRenderer);
        this.addGameObjectToScene(obj2);
    }


    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));
        AssetPool.getTexture("assets/images/blendImage2.png");
    }

    private int spriteIndex = 0;
    private float spriteFlipTime = 0.2f;
    private float spriteFlipTimeLeft = 0.0f;

    @Override
    public void update(float dt) {
//        if (KeyListener.isKeyPressed(GLFW_KEY_RIGHT)) {
//            camera.position.x += 100f * dt;
//        } else if (KeyListener.isKeyPressed(GLFW_KEY_LEFT)) {
//            camera.position.x -= 100f * dt;
//        }
//        if (KeyListener.isKeyPressed(GLFW_KEY_UP)) {
//            camera.position.y += 100f * dt;
//        } else if (KeyListener.isKeyPressed(GLFW_KEY_DOWN)) {
//            camera.position.y -= 100f * dt;
//        }


        for (GameObject go : this.gameObjects) {
            go.update(dt);
        }

        this.renderer.render();
    }

    @Override
    public void imgui() {
        ImGui.begin("Test window");
        ImGui.text("Some random text");
        ImGui.end();
    }
}