package water.godcoder.module.modules.chat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import water.godcoder.event.events.PacketEvent;
import water.godcoder.module.Module;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;
import water.godcoder.util.EntityUtil;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.network.*;
import net.minecraft.network.play.client.*;
import net.minecraft.world.*;
import net.minecraftforge.event.entity.living.*;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

@Module.Info(name = "AutoEZ", category = Module.Category.chat)
public class AutoEZ extends Module
{
    private ConcurrentHashMap<String, Integer> targetedPlayers;
    private Setting<modes> Mode;
    private Setting<owns> own;
    CPacketUseEntity cPacketUseEntity;
    Entity targetEntity;
    EntityLivingBase entity;
    EntityPlayer player;
    String name;
    @EventHandler
    public Listener<PacketEvent.Send> sendListener;
    @EventHandler
    public Listener<LivingDeathEvent> livingDeathEventListener;

    public AutoEZ() {
        Mode = register(Settings.e("EZ Mode", modes.NiceFight));
        own = register(Settings.e("Owns Mode", owns.Kotoit));
        sendListener = new Listener<PacketEvent.Send>(event -> {
            if (mc.player != null) {
                if (targetedPlayers == null) {
                    targetedPlayers = new ConcurrentHashMap<String, Integer>();
                }
                if (event.getPacket() instanceof CPacketUseEntity) {
                    cPacketUseEntity = (CPacketUseEntity)event.getPacket();
                    if (cPacketUseEntity.getAction().equals((Object)CPacketUseEntity.Action.ATTACK)) {
                        targetEntity = cPacketUseEntity.getEntityFromWorld((World)mc.world);
                        if (EntityUtil.isPlayer(targetEntity)) {
                            addTargetedPlayer(targetEntity.getName());
                        }
                    }
                }
            }
            return;
        }, (Predicate<PacketEvent.Send>[])new Predicate[0]);
        livingDeathEventListener = new Listener<LivingDeathEvent>(event -> {
            if (mc.player != null) {
                if (targetedPlayers == null) {
                    targetedPlayers = new ConcurrentHashMap<String, Integer>();
                }
                entity = event.getEntityLiving();
                if (entity != null && EntityUtil.isPlayer((Entity)entity)) {
                    player = (EntityPlayer)entity;
                    if (player.getHealth() <= 0.0f) {
                        name = player.getName();
                        if (shouldAnnounce(name)) {
                            doAnnounce(name);
                        }
                    }
                }
            }
        }, (Predicate<LivingDeathEvent>[])new Predicate[0]);
    }

    @Override
    public void onEnable() {
        targetedPlayers = new ConcurrentHashMap<String, Integer>();
    }

    @Override
    public void onDisable() {
        targetedPlayers = null;
    }

    @Override
    public void onUpdate() {
        if (isDisabled() || mc.player == null) {
            return;
        }
        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        for (Entity entity : mc.world.getLoadedEntityList()) {
            if (!EntityUtil.isPlayer(entity)) {
                continue;
            }
            EntityPlayer player = (EntityPlayer)entity;
            if (player.getHealth() > 0.0f) {
                continue;
            }
            String name2 = player.getName();
            if (shouldAnnounce(name2)) {
                doAnnounce(name2);
                break;
            }
        }
        targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                targetedPlayers.remove(name);
            }
            else {
                targetedPlayers.put(name, timeout - 1);
            }
        });
    }

    private boolean shouldAnnounce(String name) {
        return targetedPlayers.containsKey(name);
    }

    private void doAnnounce(String name) {
        targetedPlayers.remove(name);
        StringBuilder message = new StringBuilder();
        switch (Mode.getValue()) {
            case EZZZ: {
                message.append("EZZZ ");
                break;
            }
            case GG: {
                message.append("GG ");
                break;
            }
            case NiceFight: {
                message.append("Nice fight ");
                break;
            }
        }
        message.append(name);
        message.append("! ");
        switch (own.getValue()) {
            case Kotoit: {
                message.append("Kotoit");
                break;
            }
            case HACK: {
                message.append("KONC'Client");
                break;
            }
        }
        message.append(" owns me and all");
        String messageSanitized = message.toString().replaceAll("\u79ae", "");
        if (messageSanitized.length() > 255) {
            messageSanitized = messageSanitized.substring(0, 255);
        }
        mc.player.connection.sendPacket((Packet)new CPacketChatMessage(messageSanitized));
    }

    public void addTargetedPlayer(String name) {
        if (Objects.equals(name, mc.player.getName())) {
            return;
        }
        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap<String, Integer>();
        }
        targetedPlayers.put(name, 20);
    }

    private enum modes
    {
        EZZZ,
        NiceFight,
        GG;
    }

    private enum owns
    {
        Kotoit,
        HACK;
    }
}
