package fog.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class Utils {

    public static String resourceToString(ResourceLocation rl) throws IOException {
        InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(rl).getInputStream();
        return new String(IOUtils.toByteArray(is));
    }

    public static void drawTexRect(int x, int y, int w, int h) {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(x, y + h, 0, 0, 0);
        tessellator.addVertexWithUV(x + w, y + h, 0, 1, 0);
        tessellator.addVertexWithUV(x + w, y, 0, 1, 1);
        tessellator.addVertexWithUV(x, y, 0, 0, 1);
        tessellator.draw();
    }
}
