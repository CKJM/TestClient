package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

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

