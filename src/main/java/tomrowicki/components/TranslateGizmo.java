package tomrowicki.components;

import tomrowicki.editor.PropertiesWindow;
import tomrowicki.engine.MouseListener;

public class TranslateGizmo extends Gizmo {

    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {
        if (activeGameObject != null) {
            if (xAxisActive && !yAxisActive) {
                activeGameObject.transform.position.x -= MouseListener.getWorldX();
            } else if (yAxisActive) {
                activeGameObject.transform.position.y -= MouseListener.getWorldY();
            }
        }
        super.editorUpdate(dt);
    }
}
