package tomrowicki.engine;

import com.google.gson.*;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements JsonDeserializer<GameObject> {

    @Override
    public GameObject deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String name = jsonObject.get("name").getAsString();
        JsonArray components = jsonObject.get("components").getAsJsonArray();
        Transform transform = context.deserialize(jsonObject.get("transform"), Transform.class);
        int zIndex = jsonObject.get("zIndex").getAsInt();
        GameObject go = new GameObject(name, transform, zIndex);

        for (JsonElement component : components) {
            go.addComponent(context.deserialize(component, Component.class));
        }
        return go;
    }
}
