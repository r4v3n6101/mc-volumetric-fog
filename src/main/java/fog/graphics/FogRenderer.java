package fog.graphics;

import fog.graphics.opengl.DepthFramebuffer;
import fog.utils.Callback;
import fog.utils.Cleanable;

import static org.lwjgl.opengl.GL11.glGetInteger;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class FogRenderer implements Cleanable {

    private final DepthFramebuffer frontFramebuffer;
    private final DepthFramebuffer backFramebuffer;
    private final DepthFramebuffer sceneDepth;

    public FogRenderer(int width, int height) {
        frontFramebuffer = new DepthFramebuffer(width, height);
        backFramebuffer = new DepthFramebuffer(width, height);
        sceneDepth = new DepthFramebuffer(width, height);
    }

    public void draw(Callback draw) {
        int defaultFbo = glGetInteger(GL_FRAMEBUFFER_BINDING);
        sceneDepth.copyDepth(defaultFbo);
        drawBack(draw);
        drawFront(draw);
        restoreDefaultState(defaultFbo);
    }

    private void drawBack(Callback draw) {
        backFramebuffer.bind();
        glClearDepth(0f);
        glClear(GL_DEPTH_BUFFER_BIT);
        glDepthFunc(GL_GREATER);
        glCullFace(GL_FRONT);
        draw.call();
    }

    private void drawFront(Callback draw) {
        frontFramebuffer.bind();
        glClearDepth(1f);
        glClear(GL_DEPTH_BUFFER_BIT);
        glDepthFunc(GL_LEQUAL);
        glCullFace(GL_BACK);
        draw.call();
    }

    private void restoreDefaultState(int defBuf) {
        glBindFramebuffer(GL_FRAMEBUFFER, defBuf);
    }

    public DepthFramebuffer getFrontFramebuffer() {
        return frontFramebuffer;
    }

    public DepthFramebuffer getBackFramebuffer() {
        return backFramebuffer;
    }

    public DepthFramebuffer getSceneDepth() {
        return sceneDepth;
    }

    @Override
    public void free() {
        backFramebuffer.free();
        frontFramebuffer.free();
        sceneDepth.free();
    }
}
