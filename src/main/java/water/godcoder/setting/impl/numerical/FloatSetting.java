package water.godcoder.setting.impl.numerical;

import water.godcoder.setting.converter.AbstractBoxedNumberConverter;
import water.godcoder.setting.converter.BoxedFloatConverter;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Created by 086 on 12/10/2018.
 */
public class FloatSetting extends NumberSetting<Float> {

    private static final BoxedFloatConverter converter = new BoxedFloatConverter();

    public FloatSetting(Float value, Predicate<Float> restriction, BiConsumer<Float, Float> consumer, String name, Predicate<Float> visibilityPredicate, Float min, Float max) {
        super(value, restriction, consumer, name, visibilityPredicate, min, max);
    }

    @Override
    public AbstractBoxedNumberConverter converter() {
        return converter;
    }

}
