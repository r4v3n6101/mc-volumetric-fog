package fog;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fog.graphics.FogRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import static org.lwjgl.opengl.GL11.*;

@Mod(modid = "volumetric-fog")
public class ModMain {

    private static FogRenderer fr;
    private static int w, h;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void rwle(RenderWorldLastEvent event) {
        {
            Minecraft mc = Minecraft.getMinecraft();
            if (w != mc.displayWidth || h != mc.displayHeight || fr == null) {
                if (fr != null) {
                    fr.free();
                }
                fr = new FogRenderer(mc.displayWidth, mc.displayHeight);
            }
            w = mc.displayWidth;
            h = mc.displayHeight;
        }

        fr.draw(() -> {
            glPushMatrix();
            glTranslatef(0, 0, 0.5f);
            glRotatef(90, 0, 1, 0);
            RenderBlocks.getInstance().renderBlockAsItem(Blocks.fence, 0, 1f);
            glPopMatrix();
        });
    }

    @SubscribeEvent
    public void hud(RenderGameOverlayEvent.Post event) {
        if (event.type == RenderGameOverlayEvent.ElementType.ALL) {
            ScaledResolution sr = event.resolution;
            fr.getMinecraftCopy().getAttachment().bind();
            drawTexRect(0, 0, (int) (128f / sr.getScaleFactor()), (int) (128f / sr.getScaleFactor()));

            fr.getFrontFramebuffer().getAttachment().bind();
            drawTexRect(0, (int) (128f / sr.getScaleFactor()), (int) (128f / sr.getScaleFactor()), (int) (128f / sr.getScaleFactor()));

            fr.getBackFramebuffer().getAttachment().bind();
            drawTexRect(0, (int) (256f / sr.getScaleFactor()), (int) (128f / sr.getScaleFactor()), (int) (128f / sr.getScaleFactor()));
        }
    }

    private void drawTexRect(int x, int y, int w, int h) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + h, 0, 0, 1);
        tessellator.addVertexWithUV(x + w, y + h, 0, 1, 1);
        tessellator.addVertexWithUV(x + w, y, 0, 1, 0);
        tessellator.addVertexWithUV(x, y, 0, 0, 0);
        tessellator.draw();
    }
}
