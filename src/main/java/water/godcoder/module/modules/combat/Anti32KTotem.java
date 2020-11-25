package water.godcoder.module.modules.combat;

import water.godcoder.module.*;
import net.minecraft.client.gui.inventory.*;
import net.minecraft.init.*;
import net.minecraft.inventory.*;
import net.minecraft.entity.player.*;

@Module.Info(name = "Anti32K-Totem", category = Module.Category.COMBAT)
public class Anti32KTotem extends Module
{
    public void onUpdate() {
        if (Anti32KTotem.mc.currentScreen instanceof GuiContainer) {
            return;
        }
        if (Anti32KTotem.mc.player.	inventory.getStackInSlot(0).getItem() == Items.TOTEM_OF_UNDYING) {
            return;
        }
        for (int i = 9; i < 35; ++i) {
            if (Anti32KTotem.mc.player.inventory.getStackInSlot(i).getItem() == Items.TOTEM_OF_UNDYING) {
                Anti32KTotem.mc.playerController.windowClick(Anti32KTotem.mc.player.inventoryContainer.windowId, i, 0, ClickType.SWAP, (EntityPlayer)Anti32KTotem.mc.player);
                break;
            }
        }
    }
}
