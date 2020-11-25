package water.godcoder.gui.kami.theme.kami;

import water.godcoder.gui.font.CFontRenderer;
import water.godcoder.gui.rgui.component.AlignedComponent;
import water.godcoder.gui.rgui.component.use.Label;
import water.godcoder.gui.rgui.render.AbstractComponentUI;
import water.godcoder.gui.rgui.render.font.FontRenderer;
import org.lwjgl.opengl.GL11;

import java.awt.*;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.glDisable;

/**
 * Created by 086 on 2/08/2017.
 */
public class RootLabelUI<T extends Label> extends AbstractComponentUI<Label> {
    CFontRenderer cFontRenderer = new CFontRenderer(new Font("Verdana", 0, 18), true, true);


    @Override
    public void renderComponent(Label component, FontRenderer a) {
        a = component.getFontRenderer();
        String[] lines = component.getLines();
        int y = 0;
        boolean shadow = component.isShadow();
        for (String s : lines){
            int x = 0;
            if (component.getAlignment() == AlignedComponent.Alignment.CENTER)
                x = component.getWidth() / 2 - a.getStringWidth(s) / 2;
            else if (component.getAlignment() == AlignedComponent.Alignment.RIGHT)
                x = component.getWidth() - a.getStringWidth(s);

            if (shadow)
                a.drawStringWithShadow(x,y,255,255,255,s);
            else
                a.drawString(x,y,s);
            y += a.getFontHeight() + 3;
        }
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        glDisable(GL_BLEND);
    }

    @Override
    public void handleSizeComponent(Label component) {
        String[] lines = component.getLines();
        int y = 0;
        int w = 0;
        for (String s : lines){
            w = Math.max(w, component.getFontRenderer().getStringWidth(s));
            y += component.getFontRenderer().getFontHeight() + 3;
        }
        component.setWidth(w);
        component.setHeight(y);
    }
}
