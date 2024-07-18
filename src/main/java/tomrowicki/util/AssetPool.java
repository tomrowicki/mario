package tomrowicki.util;

import tomrowicki.components.Spritesheet;
import tomrowicki.renderer.Shader;
import tomrowicki.renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class AssetPool {

    private static Map<String, Shader> shaders = new HashMap<>();
    private static Map<String, Texture> textures = new HashMap<>();
    private static Map<String, Spritesheet> spritesheets = new HashMap<>();

    public static Shader getShader(String resourcePath) {
        File file = new File(resourcePath);
        if (shaders.containsKey(file.getAbsolutePath())) {
            return shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourcePath);
            shader.compile();
            shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourcePath) {
        File file = new File(resourcePath);
        if (textures.containsKey(file.getAbsolutePath())) {
            return textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture();
            texture.init(resourcePath);
            textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourcePath, Spritesheet spritesheet) {
        File file = new File(resourcePath);
        if (!spritesheets.containsKey(file.getAbsolutePath())) {
            spritesheets.put(file.getAbsolutePath(), spritesheet);
        }
    }

    public static Spritesheet getSpritesheet(String resourcePath) {
        File file = new File(resourcePath);
        assert spritesheets.containsKey(file.getAbsolutePath()) :
                "Error: Tried to access spritesheet from file " + file.getAbsolutePath() + " that has not been added to the asset pool";
        return spritesheets.getOrDefault(file.getAbsolutePath(), null);
    }
}
