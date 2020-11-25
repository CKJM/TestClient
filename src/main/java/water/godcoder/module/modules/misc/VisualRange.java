package water.godcoder.module.modules.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import water.godcoder.command.Command;
import water.godcoder.module.Module;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;
import water.godcoder.util.Friends;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 26 October 2019 by hub
 * Updated 12 January 2020 by hub
 */
@Module.Info(name = "VisualRange", description = "Reports Players in VisualRange", category = Module.Category.MISC)
public class VisualRange extends Module {

    private Setting<Boolean> leaving = register(Settings.b("Leaving", false));

    private List<String> knownPlayers;

    @Override
    public void onUpdate() {

        if (mc.player == null) {
            return;
        }

        List<String> tickPlayerList = new ArrayList<>();

        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (entity instanceof EntityPlayer) {
                tickPlayerList.add(entity.getName());
            }
        }

        if (tickPlayerList.size() > 0) {
            for (String playerName : tickPlayerList) {

                if (playerName.equals(mc.player.getName())) {
                    continue;
                }

                if (!knownPlayers.contains(playerName)) {

                    knownPlayers.add(playerName);

                    if (Friends.isFriend(playerName)) {
                        sendNotification(ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                    } else {
                        sendNotification(ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " entered the Battlefield!");
                    }

                    return;

                }

            }
        }

        if (knownPlayers.size() > 0) {
            for (String playerName : knownPlayers) {

                if (!tickPlayerList.contains(playerName)) {

                    knownPlayers.remove(playerName);

                    if (leaving.getValue()) {
                        if (Friends.isFriend(playerName)) {
                            sendNotification(ChatFormatting.GREEN.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                        } else {
                            sendNotification(ChatFormatting.RED.toString() + playerName + ChatFormatting.RESET.toString() + " left the Battlefield!");
                        }
                    }

                    return;

                }
            }
        }

    }

    private void sendNotification(String s) {
        Command.sendChatMessage("[VisualRange] " + s);
    }

    @Override
    public void onEnable() {
        this.knownPlayers = new ArrayList<>();
    }

}
