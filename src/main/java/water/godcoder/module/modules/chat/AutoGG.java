package water.godcoder.module.modules.chat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import water.godcoder.event.events.PacketEvent;
import water.godcoder.module.Module;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;
import water.godcoder.util.EntityUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;


@Module.Info(name = "AutoGG", description = "Posts a message when you kill a player", category = Module.Category.chat)
public class AutoGG extends Module {

    private ConcurrentHashMap<String, Integer> targetedPlayers = null;

    public Setting<Boolean> nonaked = register(Settings.b("NoNaked", true)); //TODO
    public static Setting<Mode> mode = Settings.e("Mode", Mode.CLINET);

    private final String CLINET1 = "Good Fight ";
    private final String CLINET2 = "! CKJM owns me and all!";
    private final String EZ = "EZZZZZZZ ";
    private final String BAD = "ur so bad Fuckboy";
    private final String DUTCHERINO1 = "keep going";
    private final String DUTCHERINO2 = "GG la ";
    private final String DUTCHERINO21 = "! GG im better then you ez";
    private final String DUTCHERINO3 = "AHA dont ez mad ";
    private final String DUTCHERINO31 = ",ur a fucking nn";
    private final String DUTCHERINO4 = "test client ownes me and all";
    private final String PING = "ping fag ez mad";

    public enum Mode {
        CLINET, EZ, BAD, DUTCHERINO, PING, RANDOM
    }

    public AutoGG() {
        register(mode);
    }
    private Setting<Integer> timeoutTicks = register(Settings.i("TimeoutTicks", 20));

    @Override
    public void onEnable() {
        targetedPlayers = new ConcurrentHashMap<>();
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
            targetedPlayers = new ConcurrentHashMap<>();
        }

        for (Entity entity : mc.world.getLoadedEntityList()) {

            // skip non player entities
            if (!EntityUtil.isPlayer(entity)) {
                continue;
            }
            EntityPlayer player = (EntityPlayer) entity;

            // skip if player is alive
            if (player.getHealth() > 0) {
                continue;
            }

            if (player.getArmorInventoryList().equals(null)) {
                return;
            }

            String name = player.getName();
            if (shouldAnnounce(name)) {
                doAnnounce(name);
                break;
            }

        }

        targetedPlayers.forEach((name, timeout) -> {
            if (timeout <= 0) {
                targetedPlayers.remove(name);
            } else {
                targetedPlayers.put(name, timeout - 1);
            }
        });

    }

    @EventHandler
    public Listener<PacketEvent.Send> sendListener = new Listener<>(event -> {

        if (mc.player == null) {
            return;
        }

        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap<>();
        }

        // return if packet is not of type CPacketUseEntity
        if (!(event.getPacket() instanceof CPacketUseEntity)) {
            return;
        }
        CPacketUseEntity cPacketUseEntity = ((CPacketUseEntity) event.getPacket());

        // return if action is not of type CPacketUseEntity.Action.ATTACK
        if (!(cPacketUseEntity.getAction().equals(CPacketUseEntity.Action.ATTACK))) {
            return;
        }

        // return if targeted Entity is not a player
        Entity targetEntity = cPacketUseEntity.getEntityFromWorld(mc.world);
        if (!EntityUtil.isPlayer(targetEntity)) {
            return;
        }

        addTargetedPlayer(targetEntity.getName());

    });

    @EventHandler
    public Listener<LivingDeathEvent> livingDeathEventListener = new Listener<>(event -> {

        if (mc.player == null) {
            return;
        }

        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap<>();
        }

        EntityLivingBase entity = event.getEntityLiving();

        if (entity == null) {
            return;
        }

        // skip non player entities
        if (!EntityUtil.isPlayer(entity)) {
            return;
        }

        EntityPlayer player = (EntityPlayer) entity;

        // skip if player is alive
        if (player.getHealth() > 0) {
            return;
        }

        String name = player.getName();
        if (shouldAnnounce(name)) {
            doAnnounce(name);
        }

    });

    private boolean shouldAnnounce(String name) {
        return targetedPlayers.containsKey(name);
    }

    private void doAnnounce(String name) {

        targetedPlayers.remove(name);

        //TODO rework this mess
        switch (mode.getValue()) {
            case CLINET:
                postGG(CLINET1 + name + CLINET2);
                break;
            case EZ:
                postGG(EZ + name);
                break;
            case BAD:
                postGG(BAD);
                break;
            case PING:
                postGG(PING);
            case DUTCHERINO:
                Random r = new Random();
                int n = r.nextInt(4);
                switch (n += 1) {
                    case 1:
                        postGG(DUTCHERINO1);
                        break;
                    case 2:
                        postGG(DUTCHERINO2 + name + DUTCHERINO21);
                        break;
                    case 3:
                        postGG(DUTCHERINO3 + name + DUTCHERINO31);
                        break;
                    case 4:
                        postGG(DUTCHERINO4);
                }
                break;
            case RANDOM:
                String[] gglist = {CLINET1, DUTCHERINO1, EZ, DUTCHERINO2, DUTCHERINO3, DUTCHERINO4, BAD, PING};
                Random random = new Random();
                String randomgg = gglist[random.nextInt(gglist.length)];
                if (randomgg == DUTCHERINO2) {
                    randomgg = DUTCHERINO2 + name + DUTCHERINO21;
                }
                if (randomgg == DUTCHERINO3) {
                    randomgg = DUTCHERINO3 + name + DUTCHERINO31;
                }
                postGG(randomgg);
                break;

        }
    }

    public void postGG(String text) {
        mc.player.connection.sendPacket(new CPacketChatMessage(text));
    }

    public void addTargetedPlayer(String name) {

        // skip if self is the target
        if (Objects.equals(name, mc.player.getName())) {
            return;
        }

        if (targetedPlayers == null) {
            targetedPlayers = new ConcurrentHashMap<>();
        }

        targetedPlayers.put(name, timeoutTicks.getValue());

    }

}
