package tomrowicki.components;

public class FontRenderer extends Component {

    @Override
    public void start() {
        if (gameObject.getComponent(FontRenderer.class) != null) {
            System.out.println("Found font renderer!");
        }
    }

    @Override
    public void update(float dt) {

    }
}
