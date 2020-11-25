package water.godcoder.module.modules.misc;

import water.godcoder.module.Module;

/**
 * @author 086
 * @see water.godcoder.mixin.client.MixinNetworkManager
 */
@Module.Info(name = "NoPacketKick", category = Module.Category.MISC, description = "Prevent large packets from kicking you")
public class NoPacketKick {
    private static NoPacketKick INSTANCE;

    public NoPacketKick() {
        INSTANCE = this;
    }

    public static boolean isEnabled() {
        return INSTANCE.isEnabled();
    }

}
