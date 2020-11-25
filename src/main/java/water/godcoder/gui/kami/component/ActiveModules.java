package water.godcoder.gui.kami.component;

import water.godcoder.gui.rgui.component.container.use.Frame;
import water.godcoder.gui.rgui.component.listen.RenderListener;
import water.godcoder.gui.rgui.component.use.Label;
import water.godcoder.gui.rgui.util.ContainerHelper;
import water.godcoder.gui.rgui.util.Docking;

public class ActiveModules extends Label {
//    public HashMap<Module, Integer> slide = new HashMap<>();

    public boolean sort_up = true;

    public ActiveModules() {
        super("");

        addRenderListener(new RenderListener() {
            @Override
            public void onPreRender() {
                Frame parentFrame = ContainerHelper.getFirstParent(Frame.class, ActiveModules.this);
                if (parentFrame == null) return;
                Docking docking = parentFrame.getDocking();
                if (docking.isTop()) sort_up = true;
                if (docking.isBottom()) sort_up = false;
            }

            @Override
            public void onPostRender() {}
        });
    }
};