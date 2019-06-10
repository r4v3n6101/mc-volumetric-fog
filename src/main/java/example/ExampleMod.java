package example;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fog.events.VolumetricFogEvent;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;

import static org.lwjgl.opengl.GL11.*;

@Mod(modid = "example-fog")
public class ExampleMod {

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void renderTestFog(VolumetricFogEvent.Draw event) {
        glPushMatrix();
        glTranslated(
                5 - RenderManager.renderPosX,
                6 - RenderManager.renderPosY,
                5 - RenderManager.renderPosZ
        );
        glScalef(10f,10f,10f);
        RenderBlocks.getInstance().renderBlockAsItem(Blocks.coal_block, 0, 1);
        glPopMatrix();
    }
}
