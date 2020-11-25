// 
// Decompiled by Procyon v0.5.36
// 

package me.zeroeightsix.kami.module.modules.misc;

import com.mojang.authlib.GameProfile;
import me.zeroeightsix.kami.module.Module;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

import java.util.UUID;

@Module.Info(name = "FakePlayer", category = Module.Category.MISC, description = "Spawns a fake Player")
public class FakePlayer extends Module
{
    @Override
    protected void onEnable() {
        if (FakePlayer.mc.world == null) {
            return ;
        }
        final EntityOtherPlayerMP fakePlayer = new EntityOtherPlayerMP((World)FakePlayer.mc.world, new GameProfile(UUID.fromString("e195d3d7-e6dc-456e-8393-e2f90816a1af"), "CKJMQAQ_YT"));
        fakePlayer.copyLocationAndAnglesFrom((Entity)FakePlayer.mc.player);
        fakePlayer.rotationYawHead = FakePlayer.mc.player.rotationYawHead;
        FakePlayer.mc.world.addEntityToWorld(-100, (Entity)fakePlayer);
        this.disable();
        return ;
    }
}
