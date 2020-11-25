package me.zeroeightsix.kami.module.modules.movement;

import me.zeroeightsix.kami.module.*;
import net.minecraft.client.settings.*;
import me.zero.alpine.listener.EventHandler;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zero.alpine.listener.Listener;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.client.gui.*;
import org.lwjgl.input.*;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraft.client.entity.*;

@Module.Info(name = "GuiMove", description ="null", category = Module.Category.MOVEMENT)
public class GuiMove extends Module
{
    private Setting<Boolean> chat;
	
	@EventHandler
	private Listener<InputUpdateEvent> inputUpdateEventListener;
	public GuiMove() {
        this.chat = this.register(Settings.b("While Chat", false));
        this.inputUpdateEventListener = new Listener<InputUpdateEvent>(event -> {
            if (GuiMove.mc.currentScreen instanceof GuiContainer || (GuiMove.mc.currentScreen instanceof GuiChat && this.chat.getValue())) {
                if (Keyboard.isKeyDown(GuiMove.mc.gameSettings.keyBindForward.getKeyCode())) {
                    event.getMovementInput().moveForward = 1.0f;
                }
                if (Keyboard.isKeyDown(GuiMove.mc.gameSettings.keyBindBack.getKeyCode())) {
                    event.getMovementInput().moveForward = -1.0f;
                }
                if (Keyboard.isKeyDown(GuiMove.mc.gameSettings.keyBindLeft.getKeyCode())) {
                    event.getMovementInput().moveStrafe = 1.0f;
                }
                if (Keyboard.isKeyDown(GuiMove.mc.gameSettings.keyBindRight.getKeyCode())) {
                    event.getMovementInput().moveStrafe = -1.0f;
                }
            }
        });
    }
	
	@Override
    public void onUpdate() {
        if (GuiMove.mc.currentScreen instanceof GuiContainer || (GuiMove.mc.currentScreen instanceof GuiChat && this.chat.getValue())) {
            if (GuiMove.mc.player.isInLava() || GuiMove.mc.player.isInWater()) {
                final EntityPlayerSP player = GuiMove.mc.player;
                player.motionY += 0.039;
            }
            else if (GuiMove.mc.player.onGround && Keyboard.isKeyDown(GuiMove.mc.gameSettings.keyBindJump.getKeyCode())) {
                GuiMove.mc.player.jump();
            }
            if (Keyboard.isKeyDown((int)200)) {
                final EntityPlayerSP player2 = GuiMove.mc.player;
                player2.rotationPitch -= 7.0f;
            }
            if (Keyboard.isKeyDown((int)208)) {
                final EntityPlayerSP player3 = GuiMove.mc.player;
                player3.rotationPitch += 7.0f;
            }
            if (Keyboard.isKeyDown((int)205)) {
                final EntityPlayerSP player4 = GuiMove.mc.player;
                player4.rotationYaw += 7.0f;
            }
            if (Keyboard.isKeyDown((int)203)) {
                final EntityPlayerSP player5 = GuiMove.mc.player;
                player5.rotationYaw -= 7.0f;
            }
            if (Keyboard.isKeyDown(GuiMove.mc.gameSettings.keyBindSprint.getKeyCode())) {
                GuiMove.mc.player.setSprinting(true);
            }
        }
    }
}
