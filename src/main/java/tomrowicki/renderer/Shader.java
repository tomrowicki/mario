package tomrowicki.renderer;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {

    private int shaderProgramId;
    private boolean beingUsed = false;

    private String vertexSource;
    private String fragmentSource;
    private String filepath;

    public Shader(String filepath) {
        this.filepath = filepath;
        try {
            String source = new String(Files.readAllBytes(Paths.get(filepath)));
            String[] splitString = source.split("(#type)( )+([a-zA-Z]+)");
            // Find the first pattern after #type
            int index = source.indexOf("#type") + 6;
            int eol = source.indexOf("\r\n");
            String firstPattern = source.substring(index, eol).trim();
            // Find the second pattern after #type
            index = source.indexOf("#type", eol) + 6;
            eol = source.indexOf("\r\n", index);
            String secondPattern = source.substring(index, eol).trim();

            if (firstPattern.equals("vertex")) {
                vertexSource = splitString[1];
            } else if (firstPattern.equals("fragment")) {
                fragmentSource = splitString[1];
            } else {
                throw new IOException("Unexpected token: '" + firstPattern);
            }

            if (secondPattern.equals("vertex")) {
                vertexSource = splitString[2];
            } else if (secondPattern.equals("fragment")) {
                fragmentSource = splitString[2];
            } else {
                throw new IOException("Unexpected token: '" + secondPattern);
            }
        } catch (IOException e) {
            e.printStackTrace();
            assert false: "Could not load shader from file: " + filepath;
        }
        System.out.println(vertexSource);
        System.out.println(fragmentSource);
    }

    public void compile() {
        /*
            Compile and link shaders
         */
        int vertexId, fragmentId;
        // First compile and load the vertex shader
        vertexId = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader src to the GPU
        glShaderSource(vertexId, vertexSource);
        glCompileShader(vertexId);
        // Check for errors in the compilation process
        int success = glGetShaderi(vertexId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(vertexId, GL_INFO_LOG_LENGTH); // get shader info
            System.out.println("vertex shader compilation error: " + glGetShaderInfoLog(vertexId, len) + " filepath: " + filepath);
            assert false : ""; // exiting the program
        }

        // First compile and load the fragment shader
        fragmentId = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader src to the GPU
        glShaderSource(fragmentId, fragmentSource);
        glCompileShader(fragmentId);
        // Check for errors in the compilation process
        success = glGetShaderi(fragmentId, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int len = glGetShaderi(fragmentId, GL_INFO_LOG_LENGTH); // get shader info
            System.out.println("fragment shader compilation error: " + glGetShaderInfoLog(fragmentId, len) + " filepath: " + filepath);
            assert false : ""; // exiting the program
        }

        // Link shaders and check for errors
        shaderProgramId = glCreateProgram();
        glAttachShader(shaderProgramId, vertexId);
        glAttachShader(shaderProgramId, fragmentId);
        glLinkProgram(shaderProgramId);
        // Check for linking errors
        success = glGetProgrami(shaderProgramId, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int len = glGetProgrami(shaderProgramId, GL_INFO_LOG_LENGTH); // get program info
            System.out.println("linking of shaders failed: " + glGetProgramInfoLog(shaderProgramId, len) + " filepath: " + filepath);
            assert false : ""; // exiting the program
        }
    }

    public void use() {
        // Bind shader program
        if (!beingUsed) {
            glUseProgram(shaderProgramId);
            beingUsed = true;
        }
    }

    public void detach() {
        glUseProgram(0);
        beingUsed = false;
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16); // matrix's capacity
        mat4.get(matBuffer); // actually 1-dimensional matrix - vector with 16 values
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }

    public void uploadVec4f(String varName, Vector4f vec) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform4f(varLocation, vec.x,vec.y, vec.z, vec.w); // f - float
    }

    public void uploadFloat(String varName, float val) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform1f(varLocation, val);
    }

    public void uploadInt(String varName, int val) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        glUniform1i(varLocation, val);
    }

    public void uploadMat3f(String varName, Matrix3f mat3) {
        int varLocation = glGetUniformLocation(shaderProgramId, varName);
        use();
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(9);
        mat3.get(matBuffer);
        glUniformMatrix3fv(varLocation, false, matBuffer); // fv - float vector
    }
}
