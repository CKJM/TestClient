package water.godcoder.util.other;

public class EventLightUpdate
extends EventCancellable {
    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean state) {

    }
}

