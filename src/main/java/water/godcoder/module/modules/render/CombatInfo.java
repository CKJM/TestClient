package water.godcoder.module.modules.render;

import java.awt.Color;
import java.awt.Font;

import water.godcoder.gui.font.CFontRenderer;
import water.godcoder.module.Module;
import water.godcoder.module.ModuleManager;
import water.godcoder.setting.Setting;
import water.godcoder.setting.Settings;
import water.godcoder.util.ColourUtils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

@Module.Info(name="Combat Info", category=Module.Category.RENDER)
public class CombatInfo
        extends Module {
    private Setting<Float> x = this.register(Settings.f("InfoX", 0.0f));
    private Setting<Float> y = this.register(Settings.f("InfoY", 200.0f));
    private Setting<Boolean> rainbow = this.register(Settings.b("Rainbow", false));
    private Setting<Integer> red = this.register(Settings.integerBuilder("Red").withRange(0, 255).withValue(255).build());
    private Setting<Integer> green = this.register(Settings.integerBuilder("Green").withRange(0, 255).withValue(255).build());
    private Setting<Integer> blue = this.register(Settings.integerBuilder("Blue").withRange(0, 255).withValue(255).build());
    CFontRenderer cFontRenderer = new CFontRenderer(new Font("Arial", 0, 18), true, false);

    @Override
    public void onRender() {
        int drgb;
        float yCount = this.y.getValue().floatValue();
        int ared = this.red.getValue();
        int bgreen = this.green.getValue();
        int cblue = this.blue.getValue();
        int color = drgb = ColourUtils.toRGBA(ared, bgreen, cblue, 255);
        int totems = CombatInfo.mc.player.inventory.mainInventory.stream().filter(itemStack -> {
            if (itemStack.getItem() != Items.TOTEM_OF_UNDYING) return false;
            return true;
        }).mapToInt(ItemStack::getCount).sum();
        if (CombatInfo.mc.player.getHeldItemOffhand().getItem() == Items.TOTEM_OF_UNDYING) {
            ++totems;
        }
        if (this.rainbow.getValue().booleanValue()) {
            int argb;
            float[] hue = new float[]{(float) (System.currentTimeMillis() % 11520L) / 11520.0f};
            int rgb = Color.HSBtoRGB(hue[0], 1.0f, 1.0f);
            int red = rgb >> 16 & 255;
            int green = rgb >> 8 & 255;
            int blue = rgb & 255;
            color = argb = ColourUtils.toRGBA(red, green, blue, 255);
        }
        {
            this.cFontRenderer.drawStringWithShadow("CA: " + this.getCaura(), this.x.getValue().floatValue(), (yCount += 10.0f) - (float) this.cFontRenderer.getHeight() - 1.0f, color);
            this.cFontRenderer.drawStringWithShadow("KA: " + this.getKA(), this.x.getValue().floatValue(), (yCount += 10.0f) - (float) this.cFontRenderer.getHeight() - 1.0f, color);
            return;
        }
    }

    private String getCaura() {
        String x = "OFF";
        if (ModuleManager.getModuleByName("CrystalAura") == null) return x;
        return Boolean.toString(ModuleManager.getModuleByName("CrystalAura").isEnabled()).toUpperCase();
    }

    private String getKA() {
        String x = "OFF";
        if (ModuleManager.getModuleByName("Aura") == null) return x;
        return Boolean.toString(ModuleManager.getModuleByName("Aura").isEnabled()).toUpperCase();
    }

}

