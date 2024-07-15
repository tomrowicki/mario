package tomrowicki.renderer;

import tomrowicki.components.SpriteRenderer;
import tomrowicki.engine.GameObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Renderer {

    private final int MAX_BATCH_SIZE = 1000;
    private List<RenderBatch> batches;

    public Renderer() {
        batches = new ArrayList<>();
    }

    public void add(GameObject go) {
        SpriteRenderer spr = go.getComponent(SpriteRenderer.class);

        if (spr != null) {
            add(spr);
        }
    }

    public void add(SpriteRenderer sprite) {
        boolean added = false;
        for (RenderBatch batch : batches) {
            if (batch.hasRoom() && batch.getZindex() == sprite.gameObject.getZindex()) { // only sprites with the same z-index belong in one batch
                Texture tex = sprite.getTexture();
                if ((tex == null || (batch.hasTexture(tex) || batch.hasTextureRoom()))) {
                    batch.addSprite(sprite);
                    added = true;
                    break;
                }
            }
        }

        if (!added) {
            RenderBatch newBatch = new RenderBatch(MAX_BATCH_SIZE, sprite.gameObject.getZindex());
            newBatch.start();
            batches.add(newBatch);
            newBatch.addSprite(sprite);
            Collections.sort(batches);
        }
    }

    public void render() {
        for (RenderBatch batch : batches) {
            batch.render();
        }
    }
}
