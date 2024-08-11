package tomrowicki.engine;

import org.joml.Vector2f;
import tomrowicki.components.*;
import tomrowicki.util.AssetPool;

public class Prefabs {

    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY) {
        GameObject block = Window.getScene().createGameObject("SPRITE_OBJECT_GEN");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);
        return block;
    }

    public static GameObject generateMario() {
        Spritesheet playerSprites = AssetPool.getSpritesheet("assets/images/spritesheets/character_and_enemies_32.png");
        GameObject mario = generateSpriteObject(playerSprites.getSprite(0), 0.25f, 0.25f);

        AnimationState run = new AnimationState();
        run.title = "Run";
        float defaultFrameTime = 0.23f;
        run.addFrame(playerSprites.getSprite(0), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(3), defaultFrameTime);
        run.addFrame(playerSprites.getSprite(2), defaultFrameTime);
        run.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(run.title);
        mario.addComponent(stateMachine);

        return mario;
    }

    public static GameObject generateQuestionBlock() {
        Spritesheet items = AssetPool.getSpritesheet("assets/images/spritesheets/items.png");
        GameObject questionBlock = generateSpriteObject(items.getSprite(0), 0.25f, 0.25f);

        AnimationState run = new AnimationState();
        run.title = "Flicker";
        float defaultFrameTime = 0.23f;
        run.addFrame(items.getSprite(0), 0.57f);
        run.addFrame(items.getSprite(1), defaultFrameTime);
        run.addFrame(items.getSprite(2), defaultFrameTime);
        run.setLoop(true);

        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(run);
        stateMachine.setDefaultState(run.title);
        questionBlock.addComponent(stateMachine);

        return questionBlock;
    }
}
