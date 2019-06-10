package fog.events;

import cpw.mods.fml.common.eventhandler.Cancelable;
import cpw.mods.fml.common.eventhandler.Event;

public class VolumetricFogEvent extends Event {

    public final float partialTicks;

    public VolumetricFogEvent(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    @Cancelable
    public static class Pre extends VolumetricFogEvent {

        public Pre(float partialTicks) {
            super(partialTicks);
        }
    }

    public static class Draw extends VolumetricFogEvent {

        public Draw(float partialTicks) {
            super(partialTicks);
        }
    }

    public static class Post extends VolumetricFogEvent {

        public Post(float partialTicks) {
            super(partialTicks);
        }
    }
}
