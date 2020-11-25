package water.godcoder.mixin.client;

import water.godcoder.KamiMod;
import water.godcoder.event.events.EntityEvent;
import water.godcoder.module.modules.movement.SafeWalk;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Created by 086 on 16/11/2017.
 */
@Mixin(Entity.class)
public class MixinEntity {

    @Redirect(method = "applyEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;addVelocity(DDD)V"))
    public void addVelocity(Entity entity, double x, double y, double z) {
        EntityEvent.EntityCollision entityCollisionEvent = new EntityEvent.EntityCollision(entity, x, y, z);
        KamiMod.EVENT_BUS.post(entityCollisionEvent);
        if (entityCollisionEvent.isCancelled()) return;

        entity.motionX += x;
        entity.motionY += y;
        entity.motionZ += z;

        entity.isAirBorne = true;
    }

    @Redirect(method = "move", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isSneaking()Z"))
    public boolean isSneaking(Entity entity) {
        return SafeWalk.shouldSafewalk() || entity.isSneaking();
    }

}
