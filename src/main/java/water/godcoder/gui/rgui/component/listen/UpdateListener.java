package water.godcoder.gui.rgui.component.listen;

import water.godcoder.gui.rgui.component.Component;

/**
 * Created by 086 on 3/08/2017.
 */
public interface UpdateListener<T extends Component> {
    public void updateSize(T component, int oldWidth, int oldHeight);
    public void updateLocation(T component, int oldX, int oldY);
}
