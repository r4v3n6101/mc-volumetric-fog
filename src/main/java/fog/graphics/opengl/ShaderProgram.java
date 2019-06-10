package fog.graphics.opengl;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;

public class ShaderProgram implements GLObject {
    private final int id;
    private final int[] shaders;

    private ShaderProgram(int id, int[] shaders) {
        this.id = id;
        this.shaders = shaders;
    }

    public static int createShader(int type, String code) {
        int shader = glCreateShader(type);
        glShaderSource(shader, code);
        glCompileShader(shader);
        if (glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
            System.err.println(glGetShaderInfoLog(shader, Integer.MAX_VALUE));
        }
        return shader;
    }

    public static ShaderProgram createShaderProgram(int... shaders) {
        int program = glCreateProgram();
        for (int shader : shaders) {
            glAttachShader(program, shader);
        }
        glLinkProgram(program);
        glValidateProgram(program);
        if (glGetProgrami(program, GL_VALIDATE_STATUS) == GL_FALSE) {
            System.err.println(glGetProgramInfoLog(program, Integer.MAX_VALUE));
        }
        return new ShaderProgram(program, shaders);
    }

    @Override
    public void bind() {
        glUseProgram(id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void free() {
        for (int s : shaders) {
            glDetachShader(id, s);
            glDeleteShader(s);
        }
        glDeleteProgram(id);
    }
}
