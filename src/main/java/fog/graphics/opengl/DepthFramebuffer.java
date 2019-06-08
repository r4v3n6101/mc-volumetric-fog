package fog.graphics.opengl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

public class DepthFramebuffer implements GLObject {

    private final int width, height, id;
    private final DepthTexture attachment;

    public DepthFramebuffer(int width, int height) {
        this.width = width;
        this.height = height;
        this.id = glGenFramebuffers();
        this.attachment = new DepthTexture(width, height);
        bind();
        attachment.bind();
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, attachment.getId(), 0);
    }

    public void copyDepth(int sourceBuf) {
        glBindFramebuffer(GL_READ_FRAMEBUFFER, sourceBuf);
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, id);
        glBlitFramebuffer(0, 0, width, height, 0, 0, width, height, GL_DEPTH_BUFFER_BIT, GL_NEAREST);
    }

    public DepthTexture getAttachment() {
        return attachment;
    }

    @Override
    public void bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, id);
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void free() {
        glDeleteFramebuffers(id);
    }
}
