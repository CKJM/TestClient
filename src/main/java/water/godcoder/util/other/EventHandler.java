package water.godcoder.util.other;

public interface EventHandler
{
    <E> void handle(final E p0);

    <T> T getListener();

    Iterable<EventFilter> getFilters();
}
