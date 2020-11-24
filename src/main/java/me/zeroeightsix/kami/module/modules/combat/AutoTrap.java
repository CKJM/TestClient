package me.zeroeightsix.kami.module.modules.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.zeroeightsix.kami.command.Command;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.module.ModuleManager;
import me.zeroeightsix.kami.module.modules.exploits.NoBreakAnimation;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import me.zeroeightsix.kami.util.BlockInteractionHelper;
import me.zeroeightsix.kami.util.EntityUtil;
import me.zeroeightsix.kami.util.Friends;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static me.zeroeightsix.kami.util.BlockInteractionHelper.canBeClicked;
import static me.zeroeightsix.kami.util.BlockInteractionHelper.faceVectorPacketInstant;

@Module.Info(name = "AutoTrap", category = Module.Category.COMBAT, description = "Traps your enemies in obsidian")
public class AutoTrap extends Module {

    private Setting<Double> range = register(Settings.doubleBuilder("Range").withMinimum(3.5).withValue(5.5).withMaximum(10.0).build());
    private Setting<Integer> blocksPerTick = register(Settings.integerBuilder("BlocksPerTick").withMinimum(1).withValue(2).withMaximum(23).build());
    private Setting<Integer> tickDelay = register(Settings.integerBuilder("TickDelay").withMinimum(0).withValue(2).withMaximum(10).build());
    private Setting<Boolean> triggerable = register(Settings.b("Triggerable", false));
    private Setting<Integer> timeoutTicks = register(Settings.integerBuilder("TimeoutTicks").withMinimum(1).withValue(40).withMaximum(100).withVisibility(b -> triggerable.getValue()).build());
    private Setting<Cage> cage = register(Settings.e("Cage", Cage.TRAP));
    private Setting<Boolean> rotate = register(Settings.b("Rotate", true));
    private Setting<Boolean> noGlitchBlocks = register(Settings.b("NoGlitchBlocks", true));
    private Setting<Boolean> activeInFreecam = register(Settings.b("Active In Freecam", true));
    private Setting<Boolean> announceUsage = register(Settings.b("AnnounceUsage", true));
    private Setting<Boolean> infoMessage = register(Settings.b("Debug", true));
    private Setting<Boolean> extrablock = register(Settings.b("Extra Block", false));
    private Setting<Boolean> selfTrap = register(Settings.b("Self Trap", false));
    private Setting<Boolean> Web = register(Settings.b("Web",false));
    private Setting<Boolean> smart = register(Settings.b("Smart", false));
    private Setting<Boolean> disableOnPlace = register(Settings.b("Disable On Place", false));


    private EntityPlayer closestTarget;
    private String lastTargetName;
    private Vec3d[] offsetsExtra = new Vec3d[]{
            new Vec3d(0.0D, 4.0D, 0.0D)};

    private boolean missingObiDisable = false;
    private boolean isSneaking = false;
    private boolean firstRun;

    private int playerHotbarSlot = -1;
    private int totalTicksRunning = 0;
    private int lastHotbarSlot = -1;
    private int delayStep = 0;
    private int offsetStep = 0;


    private static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            if (!mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                continue;
            }

            IBlockState blockState = mc.world.getBlockState(neighbour);
            if (!blockState.getMaterial().isReplaceable()) {
                return side;
            }
        }

        return null;
    }

    @Override
    protected void onEnable() {

        if (ModuleManager.getModuleByName("AutoCrystal").isEnabled()) {
            boolean caOn = true;
        }
        
        if (mc.player == null || mc.player.getHealth() <= 0) {
            return;
        }

        if(Web.getValue()) {
            ModuleManager.getModuleByName("AutoWeb").enable();
        }

        firstRun = true;

        playerHotbarSlot = mc.player.inventory.currentItem;
        lastHotbarSlot = -1;

    }

    @Override
    protected void onDisable() {
        if (mc.player == null || mc.player.getHealth() <= 0) {
            return;
        }

        if (lastHotbarSlot != playerHotbarSlot && playerHotbarSlot != -1) {
            mc.player.inventory.currentItem = playerHotbarSlot;
        }

        if (isSneaking) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
            isSneaking = false;
        }

        if(Web.getValue()) {
            ModuleManager.getModuleByName("AutoWeb").disable();
        }

        playerHotbarSlot = -1;
        lastHotbarSlot = -1;

        missingObiDisable = false;

        if (announceUsage.getValue()) {
            Command.sendChatMessage("[AutoTrap] " + ChatFormatting.RED.toString() + "Disabled" + ChatFormatting.RESET.toString() + "!");
        }
    }

    @Override
    public void onUpdate() {

        if (smart.getValue()) {
            findClosestTarget();
        }

        if (mc.player == null || mc.player.getHealth() <= 0) {
            return;
        }

        if (!activeInFreecam.getValue() && ModuleManager.isModuleEnabled("Freecam")) {
            return;
        }

        if (triggerable.getValue() && totalTicksRunning >= timeoutTicks.getValue()) {
            totalTicksRunning = 0;
            disable();
            return;
        }

        if (firstRun) {
            if (findObiInHotbar() == -1) {
                if (infoMessage.getValue()) {
                    Command.sendChatMessage(getChatName() + " " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + ", Obsidian missing!");
                }
                disable();
                return;
            }
        } else {
            if (delayStep < tickDelay.getValue()) {
                delayStep++;
                return;
            } else {
                delayStep = 0;
            }
        }

        findClosestTarget();
        totalTicksRunning++;

        if (closestTarget == null) {
            if (firstRun) {
                firstRun = false;
                if (announceUsage.getValue()) {
                    Command.sendChatMessage("[AutoTrap] " + ChatFormatting.GREEN.toString() + "Enabled" + ChatFormatting.RESET.toString() + ", waiting for target.");
                }
            }
            return;
        }

        if (firstRun) {
            firstRun = false;
            lastTargetName = closestTarget.getName();
            if (announceUsage.getValue()) {
                Command.sendChatMessage("[AutoTrap] " + ChatFormatting.GREEN.toString() + "Enabled" + ChatFormatting.RESET.toString() + ", target: " + lastTargetName);
            }
        } else if (!lastTargetName.equals(closestTarget.getName())) {
            offsetStep = 0;
            lastTargetName = closestTarget.getName();
            if (announceUsage.getValue()) {
                Command.sendChatMessage("[AutoTrap] New target: " + lastTargetName);
            }
        }

        List<Vec3d> placeTargets = new ArrayList<>();
        if ((Boolean)extrablock.getValue()) {
            Collections.addAll(placeTargets, offsetsExtra);
        }

        if (cage.getValue().equals(Cage.TRAP)) {
            Collections.addAll(placeTargets, Offsets.TRAP);
        }
        if (cage.getValue().equals(Cage.TRAPFULLROOF)) {
            Collections.addAll(placeTargets, Offsets.TRAPFULLROOF);
        }
        if (cage.getValue().equals(Cage.CRYSTALEXA)) {
            Collections.addAll(placeTargets, Offsets.CRYSTALEXA);
        }
        if (cage.getValue().equals(Cage.CRYSTAL)) {
            Collections.addAll(placeTargets, Offsets.CRYSTAL);
        }
        if (cage.getValue().equals(Cage.CRYSTALFULLROOF)) {
            Collections.addAll(placeTargets, Offsets.CRYSTALFULLROOF);
        }

        int blocksPlaced = 0;

        while (blocksPlaced < blocksPerTick.getValue()) {
            if (offsetStep >= placeTargets.size()) {
                offsetStep = 0;
                break;
            }

            BlockPos offsetPos = new BlockPos(placeTargets.get(offsetStep));
            BlockPos targetPos = new BlockPos(closestTarget.getPositionVector()).down().add(offsetPos.x, offsetPos.y, offsetPos.z);

            if (closestTarget != null && smart.getValue()) {
                if (isInRange(getClosestTargetPos())) {
                    if (placeBlockInRange(targetPos)) {
                        blocksPlaced++;
                    }
                }
            } else if (!smart.getValue()) {
                if (placeBlockInRange(targetPos)) {
                    blocksPlaced++;
                }
            }
            
            if (placeBlockInRange(targetPos)) {
                blocksPlaced++;
            }
            offsetStep++;
        }

        if (blocksPlaced > 0) {
            if (lastHotbarSlot != playerHotbarSlot && playerHotbarSlot != -1) {
                mc.player.inventory.currentItem = playerHotbarSlot;
                lastHotbarSlot = playerHotbarSlot;
            }

            if (isSneaking) {
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                isSneaking = false;
            }
        }

        if (missingObiDisable) {
            missingObiDisable = false;
            if (infoMessage.getValue()) {
                Command.sendChatMessage(getChatName() + " " + ChatFormatting.RED + "Disabled" + ChatFormatting.RESET + ", Obsidian missing!");
            }
            disable();
        }
    }

    private String getChatName() {
        return null;
    }

    private boolean isInRange(BlockPos blockPos) {
        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(
                getSphere(getPlayerPos(), range.getValue().floatValue(), range.getValue().intValue(), false, true, 0)
                        .stream().collect(Collectors.toList()));
        if (positions.contains(blockPos))
            return true;
        else {
            return false;
        }
    }

    public List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static BlockPos getPlayerPos() {
        return new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ));
    }

    public BlockPos getClosestTargetPos() {
        if (closestTarget != null) {
            return new BlockPos(Math.floor(closestTarget.posX), Math.floor(closestTarget.posY), Math.floor(closestTarget.posZ));
        } else {
            return null;
        }
    }

    private boolean placeBlockInRange(BlockPos pos) {
        
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof BlockAir) && !(block instanceof BlockLiquid)) {
            return false;
        }

        for (Entity entity : mc.world.getEntitiesWithinAABBExcludingEntity(null, new AxisAlignedBB(pos))) {
            if (!(entity instanceof EntityItem) && !(entity instanceof EntityXPOrb)) {
                return false;
            }
        }

        EnumFacing side = getPlaceableSide(pos);

        if (side == null) {
            return false;
        }

        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();

        if (!canBeClicked(neighbour)) {
            return false;
        }

        Vec3d hitVec = new Vec3d(neighbour).add(0.5, 0.5, 0.5).add(new Vec3d(opposite.getDirectionVec()).scale(0.5));
        Block neighbourBlock = mc.world.getBlockState(neighbour).getBlock();
        BlockPos blockOverHead = new BlockPos(mc.player.getPositionVector()).down().add(hitVec.x, hitVec.y, hitVec.z);
        Block block2 = mc.world.getBlockState(blockOverHead).getBlock();

        if (!(block2 instanceof BlockAir) && !(block2 instanceof BlockLiquid) && disableOnPlace.getValue()) {
            this.disable();
        }

        int obiSlot = findObiInHotbar();

        if (obiSlot == -1) {
            missingObiDisable = true;
            return false;
        }

        if (lastHotbarSlot != obiSlot) {
            mc.player.inventory.currentItem = obiSlot;
            lastHotbarSlot = obiSlot;
        }

        if (!isSneaking && BlockInteractionHelper.blackList.contains(neighbourBlock) || BlockInteractionHelper.shulkerList.contains(neighbourBlock)) {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
            isSneaking = true;
        }

        if (rotate.getValue()) {
            faceVectorPacketInstant(hitVec);
        }

        mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        mc.player.swingArm(EnumHand.MAIN_HAND);
        mc.rightClickDelayTimer = 4;

        if (noGlitchBlocks.getValue() && !mc.playerController.getCurrentGameType().equals(GameType.CREATIVE)) {
            mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, neighbour, opposite));
        }

        if (ModuleManager.getModuleByName("NoBreakAnimation").isEnabled()) {
            ((NoBreakAnimation) ModuleManager.getModuleByName("NoBreakAnimation")).resetMining();
        }
        return true;
    }

    private int findObiInHotbar() {
        int slot = -1;
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.inventory.getStackInSlot(i);

            if (stack == ItemStack.EMPTY || !(stack.getItem() instanceof ItemBlock)) {
                continue;
            }

            Block block = ((ItemBlock) stack.getItem()).getBlock();
            if (block instanceof BlockObsidian) {
                slot = i;
                break;
            }
        }
        return slot;
    }

    private void findClosestTarget() {
        List<EntityPlayer> playerList = mc.world.playerEntities;
        closestTarget = null;

        for (EntityPlayer target : playerList) {
            if (target == mc.player && !selfTrap.getValue()) {
                continue;
            }

            if (mc.player.getDistance(target) > range.getValue() + 3) {
                continue;
            }

            if (!EntityUtil.isLiving(target)) {
                continue;
            }

            if ((target).getHealth() <= 0) {
                continue;
            }

            if (Friends.isFriend(target.getName())) {
                continue;
            }

            if (closestTarget == null) {
                closestTarget = target;
                continue;
            }

            if (mc.player.getDistance(target) < mc.player.getDistance(closestTarget)) {
                closestTarget = target;
            }
        }
    }

    @Override
    public String getHudInfo() {
        if (closestTarget != null) {
            return closestTarget.getName().toUpperCase();
        }
        return "NO TARGET";
    }

    private enum Cage {
        TRAP, TRAPFULLROOF, CRYSTALEXA, CRYSTAL, CRYSTALFULLROOF
    }

    private static class Offsets {

        private static final Vec3d[] TRAP = {
                new Vec3d(0, 0, -1),
                new Vec3d(1, 0, 0),
                new Vec3d(0, 0, 1),
                new Vec3d(-1, 0, 0),
                new Vec3d(0, 1, -1),
                new Vec3d(1, 1, 0),
                new Vec3d(0, 1, 1),
                new Vec3d(-1, 1, 0),
                new Vec3d(0, 2, -1),
                new Vec3d(1, 2, 0),
                new Vec3d(0, 2, 1),
                new Vec3d(-1, 2, 0),
                new Vec3d(0, 3, -1),
                new Vec3d(0, 3, 0)
        };

        private static final Vec3d[] TRAPFULLROOF = {
                new Vec3d(0, 0, -1),
                new Vec3d(1, 0, 0),
                new Vec3d(0, 0, 1),
                new Vec3d(-1, 0, 0),
                new Vec3d(0, 1, -1),
                new Vec3d(1, 1, 0),
                new Vec3d(0, 1, 1),
                new Vec3d(-1, 1, 0),
                new Vec3d(0, 2, -1),
                new Vec3d(1, 2, 0),
                new Vec3d(0, 2, 1),
                new Vec3d(-1, 2, 0),
                new Vec3d(0, 3, -1),
                new Vec3d(1, 3, 0),
                new Vec3d(0, 3, 1),
                new Vec3d(-1, 3, 0),
                new Vec3d(0, 3, 0)
        };

        private static final Vec3d[] CRYSTALEXA = {
                new Vec3d(0, 0, -1),
                new Vec3d(0, 1, -1),
                new Vec3d(0, 2, -1),
                new Vec3d(1, 2, 0),
                new Vec3d(0, 2, 1),
                new Vec3d(-1, 2, 0),
                new Vec3d(-1, 2, -1),
                new Vec3d(1, 2, 1),
                new Vec3d(1, 2, -1),
                new Vec3d(-1, 2, 1),
                new Vec3d(0, 3, -1),
                new Vec3d(0, 3, 0)
        };

        private static final Vec3d[] CRYSTAL = {
                new Vec3d(0, 0, -1),
                new Vec3d(1, 0, 0),
                new Vec3d(0, 0, 1),
                new Vec3d(-1, 0, 0),
                new Vec3d(-1, 0, 1),
                new Vec3d(1, 0, -1),
                new Vec3d(-1, 0, -1),
                new Vec3d(1, 0, 1),
                new Vec3d(-1, 1, -1),
                new Vec3d(1, 1, 1),
                new Vec3d(-1, 1, 1),
                new Vec3d(1, 1, -1),
                new Vec3d(0, 2, -1),
                new Vec3d(1, 2, 0),
                new Vec3d(0, 2, 1),
                new Vec3d(-1, 2, 0),
                new Vec3d(-1, 2, 1),
                new Vec3d(1, 2, -1),
                new Vec3d(0, 3, -1),
                new Vec3d(0, 3, 0)
        };

        private static final Vec3d[] CRYSTALFULLROOF = {
                new Vec3d(0, 0, -1),
                new Vec3d(1, 0, 0),
                new Vec3d(0, 0, 1),
                new Vec3d(-1, 0, 0),
                new Vec3d(-1, 0, 1),
                new Vec3d(1, 0, -1),
                new Vec3d(-1, 0, -1),
                new Vec3d(1, 0, 1),
                new Vec3d(-1, 1, -1),
                new Vec3d(1, 1, 1),
                new Vec3d(-1, 1, 1),
                new Vec3d(1, 1, -1),
                new Vec3d(0, 2, -1),
                new Vec3d(1, 2, 0),
                new Vec3d(0, 2, 1),
                new Vec3d(-1, 2, 0),
                new Vec3d(-1, 2, 1),
                new Vec3d(1, 2, -1),
                new Vec3d(0, 3, -1),
                new Vec3d(1, 3, 0),
                new Vec3d(0, 3, 1),
                new Vec3d(-1, 3, 0),
                new Vec3d(0, 3, 0)
        };

    }

}
