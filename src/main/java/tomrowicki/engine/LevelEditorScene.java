package tomrowicki.engine;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import tomrowicki.renderer.Shader;
import tomrowicki.renderer.Texture;
import tomrowicki.util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private String vertexShaderSrc = "#version 330 core\n" +
            "layout (location=0) in vec3 aPos; // \"a\" stands for \"attribute\"\n" +
            "layout (location=1) in vec4 aColor;\n" +
            "\n" +
            "out vec4 fColor; // fragment shader\n" +
            "\n" +
            "void main() {\n" +
            "    fColor = aColor;\n" +
            "    gl_Position = vec4(aPos, 1.0);\n" +
            "}";

    private String fragmentShaderSrc = "#version 330 core\n" +
            "\n" +
            "in vec4 fColor;\n" +
            "\n" +
            "out vec4 color;\n" +
            "\n" +
            "void main () {\n" +
            "    color = fColor;\n" +
            "}";

    private int vertexId, fragmentId, shaderProgram;

    private float[] vertexArray = {
            // position             // color                    // UV Coordinates
            100.5f,  0.5f,    0.0f,   1.0f, 0.0f, 0.0f, 1.0f,   1, 1,    // bottom right - 0
            0.5f,   100.5f,  0.0f,   0.0f, 1.0f, 0.0f, 1.0f,    0, 0,    // top left -     1
            100.5f, 100.5f,  0.0f,   0.0f, 0.0f, 1.0f, 1.0f,    1, 0,    // top right -    2
            0.5f,    0.5f,   0.0f,   1.0f, 1.0f, 0.0f, 1.0f,    0, 1     // bottom left -  3
    };

    // IMPORTANT: must be counter-clockwise order
    private int[] elementArray = {
            /*
                        x           x


                        x           x
             */
            2, 1, 0, // top right triangle
            0, 1, 3 // bottom left triangle
    };

    private int vaoId, vboId, eboId;  // vertex array obj, vertex buffer obj, element buffer obj

    private Shader defaultShader;
    private Texture testTexture;

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();

        this.testTexture = new Texture("assets/images/testImage.jpg");

        /*
            Generate VAO, VBO, and EBO buffer objects and send to GPU
        */
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);
        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip(); // forces correct orientation for OpenGL
        // Create VBO, upload vertex buffer
        vboId = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        // Create the indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboId = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);
        // Aff the vertex attribute pointers; here we specify how to interpret the vertex array
        int positionsSize = 3; // 3 for X, Y, Z
        int colorsSize = 4; // 4 for R, G, B, A
        int uvSize = 2;
        int vertexSizeInBytes = (positionsSize + colorsSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionsSize, GL_FLOAT, false, vertexSizeInBytes, 0); // 0 at the beginning corresponds to location 0 in the shader - see default.glsl
        glEnableVertexAttribArray(0); // see above comment

        glVertexAttribPointer(1, colorsSize, GL_FLOAT, false, vertexSizeInBytes, positionsSize * Float.BYTES); // 1 corresponds to location 1 in the shader
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeInBytes, (positionsSize + colorsSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt) {
        /*
        YT comment:
        I noticed that in the Time class that you store the start time in a float when System.nanoTime() returns long.
        Although you fixed it by using one of lwjgl's functions, the real issue here isn't with System.nanoTime(), but rather with how you are storing it.
        Storing longs/doubles in floats/ints can cause issues due to data loss as longs/doubles use 8 bytes while floats/ints use 4 bytes.
         */
//        System.out.println("We're runnning at " + (1.0f / dt) + " FPS"); // FPS counter
        camera.position.x -= dt * 50.0f; // camera moving constantly left
        camera.position.y -= dt * 20.0f;

        defaultShader.use();

        // Upload texture to shader
        defaultShader.uploadTexture("TEX_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());

        // Bind the VAO
        glBindVertexArray(vaoId);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0); // bind nothing
        defaultShader.detach(); // use nothing
    }
}
