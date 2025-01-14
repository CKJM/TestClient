package water.godcoder.module.modules.movement;

import water.godcoder.module.Module;

/**
 * Created by 086 on 11/10/2018.
 */
@Module.Info(name = "SafeWalk", category = Module.Category.MOVEMENT, description = "Keeps you from walking off edges")
public class SafeWalk extends Module {

    private static SafeWalk INSTANCE;

    public SafeWalk() {
        INSTANCE = this;
    }

    public static boolean shouldSafewalk() {
        return INSTANCE.isEnabled();
    }

}
