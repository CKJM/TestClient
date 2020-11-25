package water.godcoder.module.modules.player;

import water.godcoder.module.Module;

/**
 * @author 086
 */
@Module.Info(name = "Fastplace", category = Module.Category.PLAYER, description = "Nullifies block place delay")
public class Fastplace extends Module {

    @Override
    public void onUpdate() {
        mc.rightClickDelayTimer = 0;
    }
}
