package water.godcoder.module.modules.movement;

import water.godcoder.module.Module;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;

import java.text.DecimalFormat;

/*
 * Created by Darki
 */

@Module.Info(name = "Step", description = "Makes you teleport up blocks", category = Module.Category.MOVEMENT)
public class Step extends Module {


    private Setting<Float> stepHeightSetting = register(Settings.floatBuilder("Step Height").withMinimum((float) 0).withMaximum((float) 6).withValue((float) 1).build());

    @Override
    public void onUpdate() {
        DecimalFormat df = new DecimalFormat("#");
        mc.player.stepHeight = Float.parseFloat(df.format(stepHeightSetting.getValue()));
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5F;
    }
}
