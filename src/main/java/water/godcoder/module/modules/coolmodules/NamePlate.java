package water.godcoder.module.modules.coolmodules;

import water.godcoder.module.Module;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;

@Module.Info(
   name = "NamePlate",
   description = "Name Plate Options and such",
   category = Module.Category.coolmodules
)
public class NamePlate extends Module {
   private Setting RedNamePlatee = this.register(Settings.b("Og Red 16711680", false));
   private Setting WelcomePlateX1 = this.register(Settings.integerBuilder("WelcomePositionX").withRange(1, 1000).withValue((int)1).build());
   private Setting WelcomePlateY1 = this.register(Settings.integerBuilder("WelcomePositionY").withRange(1, 1000).withValue((int)1).build());
   private Setting AcePlateX1 = this.register(Settings.integerBuilder("AcePositionX").withRange(1, 1000).withValue((int)1).build());
   private Setting AcePlateY1 = this.register(Settings.integerBuilder("AcePositionY").withRange(1, 1000).withValue((int)10).build());
   private Setting NamePlateColor1 = this.register(Settings.integerBuilder("Color").withRange(1, 100000000).withValue((Number)16711680).build());
   public static boolean NamePlateToggled;
   public static int NamePlateColorReal;
   public static int AcePlateYReal;
   public static int AcePlateXReal;
   public static int WelcomePlateYReal;
   public static int WelcomePlateXReal;

   public void onUpdate() {
      if (this.isEnabled()) {
         NamePlateColorReal = (Integer)this.NamePlateColor1.getValue();
         AcePlateYReal = (Integer)this.AcePlateY1.getValue();
         AcePlateXReal = (Integer)this.AcePlateX1.getValue();
         WelcomePlateYReal = (Integer)this.WelcomePlateY1.getValue();
         WelcomePlateXReal = (Integer)this.WelcomePlateX1.getValue();
         NamePlateToggled = true;
      }

      if (this.isDisabled()) {
         NamePlateToggled = false;
      }

   }
}
