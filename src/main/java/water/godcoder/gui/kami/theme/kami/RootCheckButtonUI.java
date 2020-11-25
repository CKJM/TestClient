package water.godcoder.gui.kami.theme.kami;

import water.godcoder.gui.font.CFontRenderer;
import water.godcoder.gui.kami.KamiGUI;
import water.godcoder.gui.rgui.component.container.Container;
import water.godcoder.gui.rgui.component.use.CheckButton;
import water.godcoder.gui.rgui.render.AbstractComponentUI;
import water.godcoder.gui.rgui.render.font.FontRenderer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by 086 on 4/08/2017.
 */
public class RootCheckButtonUI<T extends CheckButton> extends AbstractComponentUI<CheckButton> {
    CFontRenderer cFontRenderer = new CFontRenderer(new Font("Verdana", 0, 18), true, true);


    protected Color backgroundColour = new Color(200, 56, 56);
    protected Color backgroundColourHover = new Color(255,66,66);

    protected Color idleColourNormal = new Color(200, 200, 200);
    protected Color downColourNormal = new Color(190, 190, 190);

    protected Color idleColourToggle = new Color(250, 120, 120);
    protected Color downColourToggle = idleColourToggle.brighter();

    @Override
    public void renderComponent(CheckButton component, FontRenderer ff) {

        glColor4f(backgroundColour.getRed()/255f, backgroundColour.getGreen()/255f, backgroundColour.getBlue()/255f, component.getOpacity());
        if (component.isToggled()){
            glColor3f(.9f, backgroundColour.getGreen()/255f, backgroundColour.getBlue()/255f);
        }
        if (component.isHovered() || component.isPressed()){
            glColor4f(backgroundColourHover.getRed()/255f, backgroundColourHover.getGreen()/255f, backgroundColourHover.getBlue()/255f, component.getOpacity());
        }

        String text = component.getName();
        int c = component.isPressed() ? 0xaaaaaa : component.isToggled() ? 0xff3333 : 0xdddddd;
        if (component.isHovered())
            c = (c & 0x7f7f7f) << 1;

        glColor3f(1,1,1);
        KamiGUI.fontRenderer.drawString(1, KamiGUI.fontRenderer.getFontHeight() / 2 - 2, c, text);
    }

    @Override
    public void handleAddComponent(CheckButton component, Container container) {
        component.setWidth(KamiGUI.fontRenderer.getStringWidth(component.getName()) + 28);
        component.setHeight(KamiGUI.fontRenderer.getFontHeight()+2);
    }
}
