package water.godcoder.module.modules.coolmodules;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import water.godcoder.module.Module;

@Module.Info(
   name = "DeathExcuse",
   description = "Reason You Died",
   category = Module.Category.coolmodules
)
public class DeathExcuse extends Module {
   private int delay = 0;

   public void onUpdate() {
      ++this.delay;
      List myList = Arrays.asList("Lag", "wo da e le R", "dont have flash bang", "ur so fat", "Wow Died Because of Ping", "i got a bad ping", "lel");
      Random r = new Random();
      int randomitem = r.nextInt(myList.size());
      String randomElement = (String)myList.get(randomitem);
      if (mc.player.isDead) {
         this.delay = 20000000;
      }

      if (this.delay > 20000100) {
         mc.player.sendChatMessage(randomElement);
         this.delay = 0;
      }

   }
}
