package me.zeroeightsix.kami.module.modules.combat;

import me.zero.alpine.listener.EventHandler;
import me.zero.alpine.listener.Listener;
import me.zeroeightsix.kami.event.events.PacketEvent;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Module.Info(name = "MultiTask", category = Module.Category.COMBAT)
public class MultiTask extends Module {

    public List<BlockPos> crystalplaced = new ArrayList<>();
    public List<EntityEnderCrystal> crystalspawned = new ArrayList<>();
    @SubscribeEvent
    public void onMouseInput(final InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButtonState() && mc.player != null && mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL && (mc.gameSettings.keyBindUseItem.isPressed() || Mouse.getEventButton() == MouseEvent.BUTTON3)) {
            BlockPos q = mc.objectMouseOver.getBlockPos();
            RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(q.x + .5, q.y, q.z + .5));
            EnumFacing f;
            if (result == null || result.sideHit == null) {
                f = EnumFacing.UP;
            } else {
                f = result.sideHit;
            }
            mc.playerController.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(q, f, EnumHand.OFF_HAND, 0, 0, 0));
            mc.player.swingArm(EnumHand.OFF_HAND);
        }
    }
    @EventHandler
    private Listener<PacketEvent.Send> uwu = new Listener<>(event -> {
       if (event.getPacket() instanceof CPacketPlayerTryUseItemOnBlock) {
           if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && ((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getHand().equals(EnumHand.MAIN_HAND)) {
               crystalplaced.add(((CPacketPlayerTryUseItemOnBlock) event.getPacket()).getPos());
           }
       }
    });
    @Override
    public void onUpdate() {
        List<EntityEnderCrystal> uwu = mc.world.loadedEntityList.stream().filter(entity -> entity instanceof EntityEnderCrystal).map(entity -> (EntityEnderCrystal) entity).collect(Collectors.toList());
        for (EntityEnderCrystal enderCrystal : uwu) {
            if (crystalplaced.contains(new BlockPos(enderCrystal.posX - 0.5, enderCrystal.posY - 1, enderCrystal.posZ - 0.5))) {
                crystalspawned.add(enderCrystal);
            }
        }
    }

}
