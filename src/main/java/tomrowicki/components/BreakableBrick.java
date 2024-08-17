package tomrowicki.components;

import tomrowicki.util.AssetPool;

public class BreakableBrick extends Block {

    @Override
    void playerHit(PlayerController playerController) {
        // FIXME does not register; looks like sprites are loaded in a different order and some are even invisible
        System.out.println("is small?: " + playerController.isSmall());
        if (!playerController.isSmall()) {
            AssetPool.getSound("assets/sounds/break_block.ogg").play();
            gameObject.destroy();
        }
    }
}
