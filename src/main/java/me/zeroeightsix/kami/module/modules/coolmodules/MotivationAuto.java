package me.zeroeightsix.kami.module.modules.coolmodules;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.zeroeightsix.kami.module.Module;
import me.zeroeightsix.kami.setting.Setting;
import me.zeroeightsix.kami.setting.Settings;

@Module.Info(
   name = "MotivationAuto",
   description = "BeHappy",
   category = Module.Category.coolmodules
)
public class MotivationAuto extends Module {
   public static String motivationsAuto;
   private int delay = 0;
   private Setting DelayChange = this.register(Settings.integerBuilder("SecondDelay").withRange(1, 100).withValue((int)10).build());

   public void onUpdate() {
      ++this.delay;
      if (this.delay > (Integer)this.DelayChange.getValue() * 40) {
         if (mc.player.isServerWorld()) {
            String playername = mc.player.getName();
            List myList = Arrays.asList("§6Damn You Looking Fat Like A Cow" + playername, "§6You My Favorite PvPer " + playername, "§6Test Client On Top" + playername + "CKJM ON TOP! ", "§6You Dont Even Need Totems " + playername + " Your Too Good", "§6Remember Sexy Cow Loves You " + playername, "§6Damn " + playername + " You Da Best Simp", "§6Test Client Will Always Be Here For You " + playername, "§6I Know You Will Do Great Things With SKS " + playername, "§6PvP Takes Time And Practice Yet You Dont Need It. Weird Isnt It?", "§6Using Test Client Is The Closest Thing To Happy!", "§6" + playername + " You Are The MF PIMP", "§6 864 Knows You Da Best Pvper!", "§6Win This Fight For Dot5");
            Random r = new Random();
            int randomitem = r.nextInt(myList.size());
            String randomElement = (String)myList.get(randomitem);
            motivationsAuto = randomElement;
         }

         this.delay = 0;
      }

   }
}
