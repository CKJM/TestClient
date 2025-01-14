package water.godcoder.setting.impl.numerical;

import water.godcoder.setting.converter.AbstractBoxedNumberConverter;
import water.godcoder.setting.converter.BoxedIntegerConverter;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Created by 086 on 12/10/2018.
 */
public class IntegerSetting extends NumberSetting<Integer> {

    private static final BoxedIntegerConverter converter = new BoxedIntegerConverter();

    public IntegerSetting(Integer value, Predicate<Integer> restriction, BiConsumer<Integer, Integer> consumer, String name, Predicate<Integer> visibilityPredicate, Integer min, Integer max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }

    @Override
    public AbstractBoxedNumberConverter converter() {
        return converter;
    }

}
