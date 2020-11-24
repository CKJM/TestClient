package me.zeroeightsix.kami.util.other;

public class Value<T> {
    private String displayName;
    private String[] alias;
    private T value;
    private T type;
    private Value parent;

    public Value() {
    }

    public Value(String displayName, String[] alias, T value) {
        this.displayName = displayName;
        this.alias = alias;
        this.value = value;
    }

    public Value(String displayName, String[] alias, T value, T type) {
        this.displayName = displayName;
        this.alias = alias;
        this.value = value;
        this.type = type;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public T getValue() {
        return (T)this.value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getType() {
        return (T)this.type;
    }

    public void setType(T type) {
        this.type = type;
    }

    public Value getParent() {
        return this.parent;
    }

    public void setParent(Value parent) {
        this.parent = parent;
    }
}

