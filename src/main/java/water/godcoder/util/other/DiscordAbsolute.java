package water.godcoder.util.other;

import net.minecraft.client.Minecraft;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Base64;

public class DiscordAbsolute
{
   public static String discordLoader;
   private static final Minecraft mc = Minecraft.getMinecraft();
   public static boolean getDiscordRPC() {
     try {
       URL url = new URL(new String(Base64.getDecoder().decode("aHR0cHM6Ly9wYXN0ZWJpbi5jb20vcmF3LzVkclcxd0h0")));
       BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
       discordLoader = bufferedReader.readLine();
       bufferedReader.close();
     } catch (IOException e) {
       e.printStackTrace();
     } 
     String position = mc.player.getUniqueID().toString();
     return !discordLoader.contains(position);
   }
   public static boolean isEpic() {
     return true;
   }
 }