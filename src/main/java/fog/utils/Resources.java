package fog.utils;

import fog.graphics.FogRenderer;
import fog.graphics.opengl.ShaderProgram;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

import static fog.graphics.opengl.ShaderProgram.createShader;
import static fog.graphics.opengl.ShaderProgram.createShaderProgram;
import static fog.utils.Utils.resourceToString;
import static org.lwjgl.opengl.GL20.*;

public class Resources {

    private static ShaderProgram fogShader;
    private static FogRenderer fogRenderer;
    private static int framebufferWidth, framebufferHeight;

    private static ShaderProgram initShaderProgram() {
        try {
            ResourceLocation vertex = new ResourceLocation("fog:shaders/vertex.glsl");
            ResourceLocation fragment = new ResourceLocation("fog:shaders/fragment.glsl");
            fogShader = createShaderProgram(
                    createShader(GL_VERTEX_SHADER, resourceToString(vertex)),
                    createShader(GL_FRAGMENT_SHADER, resourceToString(fragment))
            );

            fogShader.bind();
            glUniform1i(glGetUniformLocation(fogShader.getId(), "scene"), 0);
            glUniform1i(glGetUniformLocation(fogShader.getId(), "back"), 1);
            glUniform1i(glGetUniformLocation(fogShader.getId(), "front"), 2);
            glUseProgram(0); // TODO : May broke another shaders
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fogShader;
    }

    public static ShaderProgram getFogShader() {
        return fogShader == null ? initShaderProgram() : fogShader;
    }

    private static FogRenderer initFogRenderer(int width, int height) {
        if (fogRenderer != null) fogRenderer.free();
        fogRenderer = new FogRenderer(width, height);
        framebufferWidth = width;
        framebufferHeight = height;

        return fogRenderer;
    }

    public static FogRenderer getFogRenderer(int width, int height) {
        boolean nullOrChanged = fogRenderer == null
                || width != framebufferWidth
                || height != framebufferHeight;
        return nullOrChanged ? initFogRenderer(width, height) : fogRenderer;
    }

    public static FogRenderer getFogRenderer() {
        Minecraft mc = Minecraft.getMinecraft();
        return getFogRenderer(mc.displayWidth, mc.displayHeight);
    }
}
