package me.zeroeightsix.kami.module.modules.render;

import me.zeroeightsix.kami.module.*;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.*;
import me.zeroeightsix.kami.setting.Setting;
import net.minecraftforge.common.*;
import me.zeroeightsix.kami.event.events.*;
import net.minecraft.client.*;
import net.minecraft.block.material.*;
import net.minecraft.entity.*;
import me.zeroeightsix.kami.util.*;
import net.minecraft.block.state.*;
import net.minecraft.util.math.*;
import net.minecraftforge.client.event.*;
import net.minecraft.world.*;
import net.minecraftforge.fml.common.eventhandler.*;

@Module.Info(name = "BlockHighlight", category = Module.Category.RENDER, description = "Make selected block bounding box more visible")
public class BlockHighlight extends Module
{
    private static BlockPos position;
    private Setting<Integer> red;
    private Setting<Integer> green;
    private Setting<Integer> blue;
    private Setting<Integer> alpha;
    private Setting<Float> width;
    
    public BlockHighlight() {
        this.red = this.register((Setting<Integer>)Settings.integerBuilder("Red").withRange(0, 255).withValue(255).build());
        this.green = this.register((Setting<Integer>)Settings.integerBuilder("Green").withRange(0, 255).withValue(0).build());
        this.blue = this.register((Setting<Integer>)Settings.integerBuilder("Blue").withRange(0, 255).withValue(0).build());
        this.alpha = this.register((Setting<Integer>)Settings.integerBuilder("Transparency").withRange(0, 255).withValue(70).build());
        this.width = this.register((Setting<Float>)Settings.floatBuilder("Thickness").withRange(1.0f, 10.0f).withValue(1.0f).build());
    }
    
    @Override
    protected void onEnable() {
        MinecraftForge.EVENT_BUS.register((Object)this);
    }
    
    @Override
    protected void onDisable() {
        MinecraftForge.EVENT_BUS.unregister((Object)this);
        BlockHighlight.position = null;
    }
    
    @Override
    public void onWorldRender(final RenderEvent event) {
        final Minecraft mc = Minecraft.getMinecraft();
        final RayTraceResult ray = mc.objectMouseOver;
        if (ray.typeOfHit == RayTraceResult.Type.BLOCK) {
            final BlockPos blockpos = ray.getBlockPos();
            final IBlockState iblockstate = mc.world.getBlockState(blockpos);
            if (iblockstate.getMaterial() != Material.AIR && mc.world.getWorldBorder().contains(blockpos)) {
                final Vec3d interp = MathUtil.interpolateEntity((Entity)mc.player, mc.getRenderPartialTicks());
                KamiTessellator.drawBoundingBox(iblockstate.getSelectedBoundingBox((World)mc.world, blockpos).grow(0.0020000000949949026).offset(-interp.x, -interp.y, -interp.z), this.width.getValue(), this.red.getValue(), this.green.getValue(), this.blue.getValue(), this.alpha.getValue());
            }
        }
    }
    
    @SubscribeEvent
    public void onDrawBlockHighlight(final DrawBlockHighlightEvent event) {
        if (BlockHighlight.mc.player == null || BlockHighlight.mc.world == null || (!BlockHighlight.mc.playerController.getCurrentGameType().equals((Object)GameType.SURVIVAL) && !BlockHighlight.mc.playerController.getCurrentGameType().equals((Object)GameType.CREATIVE))) {
            return;
        }
        event.setCanceled(true);
    }
}
