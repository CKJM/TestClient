package water.godcoder.util.other;

public class BooleanValue
extends Value {
    public BooleanValue() {
    }

    public BooleanValue(String displayName, String[] alias, Object value) {
        super(displayName, alias, value);
    }

    public boolean getBoolean() {
        return (Boolean)this.getValue();
    }

    public void setBoolean(boolean val) {
        this.setValue((Object)val);
    }
}

