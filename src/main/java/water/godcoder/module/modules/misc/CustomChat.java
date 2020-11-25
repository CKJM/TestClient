package water.godcoder.module.modules.misc;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import water.godcoder.event.events.PacketEvent;
import water.godcoder.module.Module;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;
import net.minecraft.network.play.client.CPacketChatMessage;

/**
 * Created by 086 on 8/04/2018.
 */
@Module.Info(name = "CustomChat", category = Module.Category.MISC)
public class CustomChat extends Module {

    private Setting<Boolean> commands = register(Settings.b("Commands", false));

    private final String KAMI_SUFFIX = "\u1d00 \u1d04\u1d00\u1d1b\u1d0d\u026a";

    @EventHandler
    public Listener<PacketEvent.Send> listener = new Listener<>(event -> {
        if (event.getPacket() instanceof CPacketChatMessage) {
            String s = ((CPacketChatMessage) event.getPacket()).getMessage();
            if (s.startsWith("/") && !commands.getValue()) return;
            s += KAMI_SUFFIX;
            if (s.length() >= 256) s = s.substring(0,256);
            ((CPacketChatMessage) event.getPacket()).message = s;
        }
    });

}
