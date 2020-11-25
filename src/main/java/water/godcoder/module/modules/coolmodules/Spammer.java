package water.godcoder.module.modules.coolmodules;

import water.godcoder.module.Module;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;
import net.minecraft.init.Items;

@Module.Info(
   name = "Spammer",
   description = "spam",
   category = Module.Category.coolmodules
)
public class Spammer extends Module {
   private Setting delays;
   private int delay = 0;
   private Setting DelayChange = this.register(Settings.integerBuilder("SecondDelay").withRange(1, 100).withValue((int)10).build());
   private Setting testchange = this.register(Settings.stringBuilder("Test").withValue("aple").build());

   public void onUpdate() {
      ++this.delay;
      if (this.delay > (Integer)this.DelayChange.getValue() * 40) {
         if (mc.player.sleeping) {
            mc.player.sendChatMessage("Im Sleeping Thanks To Test");
         }

         if (mc.player.inWater) {
            mc.player.sendChatMessage("Im Swimming Thanks To Test");
         }

         if (mc.player.isDead) {
            mc.player.sendChatMessage("I Died With Test Installed");
         }

         if (mc.player.isInWeb) {
            mc.player.sendChatMessage("Im In Webs Thanks To Test");
         }

         if (mc.player.inPortal) {
            mc.player.sendChatMessage("Im Traveling Threw A Portal Thanks To Test");
         }

         if (mc.player.isSneaking()) {
            mc.player.sendChatMessage("Im Sneaking Thanks To Ace Hack!");
         }

         if (mc.player.isElytraFlying()) {
            mc.player.sendChatMessage("Yee Haw Im Flyin Like A Cowboy Thanks To Test");
         }

         if (mc.fpsCounter == 144) {
            mc.player.sendChatMessage("Im At 144 Fps Thanks To Test");
         }

         if (mc.player.glowing) {
            mc.player.sendChatMessage("Im Glowing Thanks To Ace Hack!");
         }

         if (mc.player.isInLava()) {
            mc.player.sendChatMessage("Im Swimming Thanks To Hack Of Test");
         }

         if (mc.player.isSwingInProgress && mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
            mc.player.sendChatMessage("Im Crystalling You Thanks To Test");
         }

         if (mc.world.isRaining()) {
            mc.player.sendChatMessage("Yee Haw Its Raining Cats and Dogs Out There Thanks Test!");
         }

         if (mc.playerController.isHittingBlock && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_PICKAXE) {
            mc.player.sendChatMessage("Thanks To Test Im Mining!!!");
         }

         if (mc.player.isSwingInProgress && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD) {
            mc.player.sendChatMessage("Im Slaying You Thanks To Test");
         }

         if (mc.player.getHealth() == 10.0F && mc.player.getHeldItemMainhand().getItem() == Items.GOLDEN_APPLE) {
            mc.player.sendChatMessage("Thanks To Test Im Eating Gapples!");
         }

         if (mc.player.getLastAttackedEntityTime() == 1) {
            mc.player.sendChatMessage("Thanks To Test Im Full On health!!");
         }

         if (mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE) {
            mc.player.sendChatMessage("YEE HAWWWW IM THROWIN XP THANKS TO Test");
         }

         this.delay = 0;
      }

   }
}
