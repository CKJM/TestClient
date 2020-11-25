package water.godcoder.module.modules.movement;

import water.godcoder.module.Module;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;

@Module.Info(category=Module.Category.MOVEMENT, description="AirJump", name="AirJump")
public class AirJump
extends Module {
    private Boolean owo = false;
    private Setting<Float> speed = this.register(Settings.f("Speed", 5.0f));
    private Setting<Float> movementspeed = this.register(Settings.f("MoveSpeed", 10.0f));

    @Override
    protected void onEnable() {
        if (AirJump.mc.player == null) {
        }
    }

    @Override
    public void onUpdate() {
        AirJump.mc.player.capabilities.isFlying = false;
        AirJump.mc.player.jumpMovementFactor = this.movementspeed.getValue() / 100.0f;
        if (AirJump.mc.gameSettings.keyBindJump.isKeyDown()) {
            if (!this.owo) {
                AirJump.mc.player.motionY = this.speed.getValue() / 10.0f;
                this.owo = true;
            }
        } else if (!AirJump.mc.gameSettings.keyBindJump.isKeyDown()) {
            this.owo = false;
        }
    }
}

