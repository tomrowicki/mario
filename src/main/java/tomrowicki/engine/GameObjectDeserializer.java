package tomrowicki.engine;

import com.google.gson.*;
import tomrowicki.components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.get("components").getAsJsonArray();
        GameObject go = new GameObject(name);

        for (JsonElement component : components) {
            go.addComponent(context.deserialize(component, Component.class));
        }
        go.transform = go.getComponent(Transform.class);
        return go;
    }
}
