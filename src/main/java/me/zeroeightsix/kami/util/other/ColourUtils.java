package me.zeroeightsix.kami.util.other;

import java.awt.Color;

public class ColourUtils {
   public static Color rainbow() {
      float hue = (float)(System.nanoTime() + 10L) / 1.0E10F % 1.0F;
      long color = Long.parseLong(Integer.toHexString(Color.HSBtoRGB(hue, 1.0F, 1.0F)), 16);
      Color c = new Color((int)color);
      return new Color((float)c.getRed() / 255.0F, (float)c.getGreen() / 255.0F, (float)c.getBlue() / 255.0F, (float)c.getAlpha() / 255.0F);
   }
}
