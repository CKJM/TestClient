package me.zeroeightsix.kami.module.modules.coolmodules;

import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemStack;

@Module.Info(
   name = "Item Count",
   description = "adds the totemm, gapple, xp, and crystal counter",
   category = Module.Category.coolmodules
)
public class PvPInfo extends Module {
   public static Object Info;
   public static String String;
   public static int xpbottle;
   public static int tots;
   public static int crystals;
   public static int Gaps;
   public boolean moving = false;
   public boolean returnI = false;
   private Setting soft = this.register(Settings.b("Soft"));

   public void onUpdate() {
      if (mc.currentScreen instanceof GuiContainer && this.returnI) {
         int t = -1;

         for(int i = 0; i < 45; ++i) {
            if (mc.player.inventory.getStackInSlot(i).isEmpty) {
               t = i;
               break;
            }
         }

         if (t == -1) {
            mc.playerController.windowClick(0, t < 9 ? t + 36 : t, 0, ClickType.PICKUP, mc.player);
         }

         this.returnI = false;
         super.toggle();
      }

      tots = mc.player.inventory.mainInventory.stream().filter((itemStack) -> {
         return itemStack.getItem() == Items.TOTEM_OF_UNDYING;
      }).mapToInt(ItemStack::getCount).sum();
      if (mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
         ++tots;
      }

      crystals = mc.player.inventory.mainInventory.stream().filter((itemStack) -> {
         return itemStack.getItem() == Items.END_CRYSTAL;
      }).mapToInt(ItemStack::getCount).sum();
      if (mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL) {
         ++crystals;
      }

      Gaps = mc.player.inventory.mainInventory.stream().filter((itemStack) -> {
         return itemStack.getItem() == Items.GOLDEN_APPLE;
      }).mapToInt(ItemStack::getCount).sum();
      if (mc.player.getHeldItemOffhand().getItem() == Items.GOLDEN_APPLE) {
         ++Gaps;
      }

      xpbottle = mc.player.inventory.mainInventory.stream().filter((itemStack) -> {
         return itemStack.getItem() == Items.EXPERIENCE_BOTTLE;
      }).mapToInt(ItemStack::getCount).sum();
      if (mc.player.getHeldItemOffhand().getItem() == Items.EXPERIENCE_BOTTLE) {
         ++xpbottle;
      }

   }

   public String getHudInfo() {
      String var10000 = String;
      java.lang.String.valueOf(Gaps);
      var10000 = String;
      java.lang.String.valueOf(tots);
      var10000 = String;
      java.lang.String.valueOf(crystals);
      var10000 = String;
      java.lang.String.valueOf(xpbottle);
      return null;
   }
}
