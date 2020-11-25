package water.godcoder.gui.kami.theme.kami;

import water.godcoder.gui.kami.KamiGUI;
import water.godcoder.gui.kami.theme.staticui.RadarUI;
import water.godcoder.gui.kami.theme.staticui.TabGuiUI;
import water.godcoder.gui.rgui.component.container.use.Frame;
import water.godcoder.gui.rgui.component.use.Button;
import water.godcoder.gui.rgui.render.AbstractComponentUI;
import water.godcoder.gui.rgui.render.font.FontRenderer;
import water.godcoder.gui.rgui.render.theme.AbstractTheme;

/**
 * Created by 086 on 26/06/2017.
 */
public class KamiTheme extends AbstractTheme {

    FontRenderer fontRenderer;

    public KamiTheme() {
        installUI(new RootButtonUI<Button>());
        installUI(new GUIUI());
        installUI(new RootGroupboxUI());
        installUI(new KamiFrameUI<Frame>());
        installUI(new RootScrollpaneUI());
        installUI(new RootInputFieldUI());
        installUI(new RootLabelUI());
        installUI(new RootChatUI());
        installUI(new RootCheckButtonUI());
        installUI(new KamiActiveModulesUI());
        installUI(new KamiSettingsPanelUI());
        installUI(new RootSliderUI());
        installUI(new KamiEnumbuttonUI());
        installUI(new RootColorizedCheckButtonUI());
        installUI(new KamiUnboundSliderUI());

        installUI(new RadarUI());
        installUI(new TabGuiUI());

        fontRenderer=KamiGUI.fontRenderer;
    }

    @Override
    public FontRenderer getFontRenderer() {
        return fontRenderer;
    }

    public class GUIUI extends AbstractComponentUI<KamiGUI> {
    }
}
