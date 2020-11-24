package me.zeroeightsix.kami.util.other;

public class EventStageable {
    private EventStage stage;

    public EventStageable() {
    }

    public EventStageable(EventStage stage) {
        this.stage = stage;
    }

    public EventStage getStage() {
        return this.stage;
    }

    public void setStage(EventStage stage) {
        this.stage = stage;
    }

    public static class EventStage {
        public static EventStage PRE;
    }
}

