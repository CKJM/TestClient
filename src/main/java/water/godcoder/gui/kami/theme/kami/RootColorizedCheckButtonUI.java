package water.godcoder.gui.kami.theme.kami;

import water.godcoder.gui.kami.KamiGUI;
import water.godcoder.gui.kami.RootSmallFontRenderer;
import water.godcoder.gui.kami.component.ColorizedCheckButton;
import water.godcoder.gui.rgui.component.use.CheckButton;
import water.godcoder.gui.rgui.render.font.FontRenderer;

import java.awt.*;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by 086 on 8/08/2017.
 */
public class RootColorizedCheckButtonUI extends RootCheckButtonUI<ColorizedCheckButton> {

    RootSmallFontRenderer ff = new RootSmallFontRenderer();

    public RootColorizedCheckButtonUI() {
        backgroundColour = new Color(200, backgroundColour.getGreen(), backgroundColour.getBlue());
        backgroundColourHover = new Color(255, backgroundColourHover.getGreen(), backgroundColourHover.getBlue());
        downColourNormal = new Color(190, 190, 190);
    }

    @Override
    public void renderComponent(CheckButton component, FontRenderer aa) {
        glColor4f(backgroundColour.getRed()/255f, backgroundColour.getGreen()/255f, backgroundColour.getBlue()/255f, component.getOpacity());
        if (component.isHovered() || component.isPressed()){
            glColor4f(backgroundColourHover.getRed()/255f, backgroundColourHover.getGreen()/255f, backgroundColourHover.getBlue()/255f, component.getOpacity());
        }
        if (component.isToggled()){
            glColor3f(backgroundColour.getRed()/255f, backgroundColour.getGreen()/255f, backgroundColour.getBlue()/255f);
        }

//        RenderHelper.drawRoundedRectangle(0,0,component.getWidth(), component.getHeight(), 3f);
        glLineWidth(2.5f);
        glBegin(GL_LINES);
        {
            glVertex2d(0,component.getHeight());
            glVertex2d(component.getWidth(),component.getHeight());
        }
        glEnd();

        Color idleColour = component.isToggled() ? idleColourToggle : idleColourNormal;
        Color downColour = component.isToggled() ? downColourToggle : downColourNormal;

        glColor3f(1,1,1);
        glEnable(GL_TEXTURE_2D);
        ff.drawString(component.getWidth() / 2 - KamiGUI.fontRenderer.getStringWidth(component.getName()) / 2, 0, component.isPressed() ? downColour : idleColour, component.getName());
        glDisable(GL_TEXTURE_2D);
    }
}
