package tomrowicki.engine;

import org.joml.Vector2f;
import tomrowicki.components.Sprite;
import tomrowicki.components.SpriteRenderer;
import tomrowicki.components.Spritesheet;
import tomrowicki.util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;

public class LevelEditorScene extends Scene {

    private GameObject obj1;
    private Spritesheet sprites;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        loadResources();

        this.camera = new Camera(new Vector2f(-250, 0));

        sprites = AssetPool.getSpritesheet("assets/images/spritesheet.png");

        obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100), new Vector2f(256, 256)), 4);
        obj1.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/blendImage1.png"))));
        this.addGameObjectToScene(obj1);

        GameObject obj2 = new GameObject("Object 2", new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 2);
        obj2.addComponent(new SpriteRenderer(new Sprite(AssetPool.getTexture("assets/images/blendImage2.png"))));
        this.addGameObjectToScene(obj2);
    }

    private void loadResources() {
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/spritesheet.png",
                new Spritesheet(AssetPool.getTexture("assets/images/spritesheet.png"),
                        16, 16, 26, 0));
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
}