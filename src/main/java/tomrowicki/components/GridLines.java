package tomrowicki.components;

import org.joml.Vector2f;
import org.joml.Vector3f;
import tomrowicki.engine.Camera;
import tomrowicki.engine.Window;
import tomrowicki.renderer.DebugDraw;
import tomrowicki.util.Settings;

public class GridLines extends Component {

    @Override
    public void editorUpdate(float dt) {
        Camera camera = Window.getScene().camera();
        Vector2f cameraPos = camera.position;
        // projection matrix is a normalized system of screen coords using "world units"
        Vector2f projectionSize = camera.getProjectionSize();

        float firstX = ((int) (cameraPos.x / Settings.GRID_WIDTH) - 1) * Settings.GRID_WIDTH;
        float firstY = ((int) (cameraPos.y / Settings.GRID_HEIGHT) - 1) * Settings.GRID_HEIGHT;;

        int numVerLines = (int) (projectionSize.x * camera.getZoom() / Settings.GRID_WIDTH) + 2;
        int numHzLines = (int) (projectionSize.y * camera.getZoom() / Settings.GRID_HEIGHT) + 2;

        float height = (int) (projectionSize.y * camera.getZoom()) + Settings.GRID_HEIGHT * 2;
        float width = (int) (projectionSize.x * camera.getZoom()) + Settings.GRID_WIDTH * 2;

        int maxLines = Math.max(numVerLines, numHzLines);
        Vector3f color = new Vector3f(0.2f, 0.2f, 0.2f);
        for (int i = 0; i < maxLines; i++) {
            float x = firstX + (Settings.GRID_WIDTH * i);
            float y = firstY + (Settings.GRID_HEIGHT * i);

            if (i < numVerLines) {
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, y + height), color);
            }

            if (i < numHzLines) {
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}
