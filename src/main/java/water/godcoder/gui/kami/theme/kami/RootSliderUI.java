package water.godcoder.gui.kami.theme.kami;

import water.godcoder.gui.kami.RenderHelper;
import water.godcoder.gui.kami.RootSmallFontRenderer;
import water.godcoder.gui.rgui.component.container.Container;
import water.godcoder.gui.rgui.component.use.Slider;
import water.godcoder.gui.rgui.render.AbstractComponentUI;
import water.godcoder.gui.rgui.render.font.FontRenderer;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by 086 on 8/08/2017.
 */
public class RootSliderUI extends AbstractComponentUI<Slider> {

    RootSmallFontRenderer smallFontRenderer = new RootSmallFontRenderer();

    @Override
    public void renderComponent(Slider component, FontRenderer aa) {
        glColor4f(1,0.33f,0.33f,component.getOpacity());
        glLineWidth(2.5f);
        int height = component.getHeight();
        double value = component.getValue();
        double w = component.getWidth() * ((value - component.getMinimum()) / (component.getMaximum() - component.getMinimum()));
        float downscale = 1.1f;
        glBegin(GL_LINES);
        {
            glVertex2d(0,height/downscale);
            glVertex2d(w,height/downscale);
        }
        glColor3f(0.33f,0.33f,0.33f);
        {
            glVertex2d(w,height/downscale);
            glVertex2d(component.getWidth(),height/downscale);
        }
        glEnd();
        glColor3f(1,0.33f,0.33f);
        RenderHelper.drawCircle((int)w,height/downscale,2f);

        String s = value + "";
        if (component.isPressed()){
            w -= smallFontRenderer.getStringWidth(s)/2;
            w = Math.max(0,Math.min(w, component.getWidth()-smallFontRenderer.getStringWidth(s)));
            smallFontRenderer.drawString((int) w, 0, s);
        }else{
            smallFontRenderer.drawString(0,0,component.getText());
            smallFontRenderer.drawString(component.getWidth() - smallFontRenderer.getStringWidth(s), 0, s);
        }
        glDisable(GL_TEXTURE_2D);
    }

    @Override
    public void handleAddComponent(Slider component, Container container) {
        component.setHeight(component.getTheme().getFontRenderer().getFontHeight()+2);
        component.setWidth(smallFontRenderer.getStringWidth(component.getText()) + smallFontRenderer.getStringWidth(component.getMaximum() + "") + 3);
    }
}
