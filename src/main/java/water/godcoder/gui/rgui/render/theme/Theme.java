package water.godcoder.gui.rgui.render.theme;

import water.godcoder.gui.rgui.component.Component;
import water.godcoder.gui.rgui.render.ComponentUI;
import water.godcoder.gui.rgui.render.font.FontRenderer;

/**
 * Created by 086 on 25/06/2017.
 */
public interface Theme {
    public ComponentUI getUIForComponent(Component component);
    public FontRenderer getFontRenderer();
}
