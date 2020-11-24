package me.zeroeightsix.kami.module.modules.coolmodules;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import me.zeroeightsix.kami.module.Module;

@Module.Info(
   name = "AutoSwitchUp",
   description = "Switch Up",
   category = Module.Category.coolmodules
)
public class AutoSwitchUp extends Module {
   protected void onEnable() {
      List myList = Arrays.asList("Just Left The Emperium For SKS", "Just Left Emperium For Highland!!");
      Random r = new Random();
      int randomitem = r.nextInt(myList.size());
      String randomElement = (String)myList.get(randomitem);
      mc.player.sendChatMessage(randomElement);
      super.toggle();
   }
}
