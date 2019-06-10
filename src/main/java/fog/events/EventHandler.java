package fog.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fog.graphics.FogRenderer;
import fog.graphics.opengl.ShaderProgram;
import fog.utils.Resources;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import static fog.utils.Utils.drawTexRect;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class EventHandler {

    public EventHandler() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void worldRenderLast(RenderWorldLastEvent event) {
        if (!MinecraftForge.EVENT_BUS.post(new VolumetricFogEvent.Pre(event.partialTicks))) {
            Resources.getFogRenderer().draw(
                    () -> MinecraftForge.EVENT_BUS.post(new VolumetricFogEvent.Draw(event.partialTicks))
            );
            MinecraftForge.EVENT_BUS.post(new VolumetricFogEvent.Post(event.partialTicks));
        }
    }

    @SubscribeEvent
    public void renderGameOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            ScaledResolution sr = event.resolution;
            FogRenderer fr = Resources.getFogRenderer();
            ShaderProgram fs = Resources.getFogShader();

            int depth = fr.getSceneDepth().getAttachment().getId();
            int back = fr.getBackFramebuffer().getAttachment().getId();
            int front = fr.getFrontFramebuffer().getAttachment().getId();

            glActiveTexture(GL_TEXTURE0);
            glBindTexture(GL_TEXTURE_2D, depth);
            glActiveTexture(GL_TEXTURE1);
            glBindTexture(GL_TEXTURE_2D, back);
            glActiveTexture(GL_TEXTURE2);
            glBindTexture(GL_TEXTURE_2D, front);

            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            fs.bind();
            drawTexRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight());
            glUseProgram(0); // TODO : May broke another shaders

            glActiveTexture(GL_TEXTURE0);
        }
    }
}
