package tomrowicki.engine;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f; // 2 seconds

    public LevelEditorScene() {
        System.out.println("Inside Level Editor Scene");
    }

    @Override
    public void update(float dt) {

        /*
        YT comment:
        I noticed that in the Time class that you store the start time in a float when System.nanoTime() returns long.
        Although you fixed it by using one of lwjgl's functions, the real issue here isn't with System.nanoTime(), but rather with how you are storing it.
        Storing longs/doubles in floats/ints can cause issues due to data loss as longs/doubles use 8 bytes while floats/ints use 4 bytes.
         */

        System.out.println("We're runnning at " + (1.0f / dt) + " FPS"); // FPS counter

        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0.0f) {
            timeToChangeScene -= dt;
            Window.get().r -= dt * 5.0f;
            Window.get().g -= dt * 5.0f;
            Window.get().b -= dt * 5.0f;
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}
