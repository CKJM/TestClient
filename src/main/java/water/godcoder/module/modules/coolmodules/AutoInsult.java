package water.godcoder.module.modules.coolmodules;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import water.godcoder.module.Module;

@Module.Info(
   name = "AutoInsult",
   description = "WinDaArguement",
   category = Module.Category.coolmodules
)
public class AutoInsult extends Module {
   protected void onEnable() {
      List myList = Arrays.asList("who?", "You Just got ez By test client", " I didnt die you did!", "someone sounds mad", " someone sounds angy", "be quiet kid its past your bedtime!", "You just got fucked by Impact AutoCrystal!", "Sorry i dont speak ã ¨é´�è±Šã¯£", "Beep Bob", "Your Just Mad test Is On Top", "You PVP Like Dog", "You Smell Like Dog");
      Random r = new Random();
      int randomitem = r.nextInt(myList.size());
      String randomElement = (String)myList.get(randomitem);
      mc.player.sendChatMessage(randomElement);
      super.toggle();
   }
}
