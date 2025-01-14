package water.godcoder.setting.impl;

import com.google.common.base.Converter;
import water.godcoder.setting.Setting;
import water.godcoder.setting.converter.EnumConverter;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

/**
 * Created by 086 on 14/10/2018.
 */
public class EnumSetting<T extends Enum> extends Setting<T> {

    private EnumConverter converter;
    public final Class<? extends Enum> clazz;

    public EnumSetting(T value, Predicate<T> restriction, BiConsumer<T, T> consumer, String name, Predicate<T> visibilityPredicate, Class<? extends Enum> clazz) {
        super(value, restriction, consumer, name, visibilityPredicate);
        this.converter = new EnumConverter(clazz);
        this.clazz = clazz;
    }

    @Override
    public Converter converter() {
        return converter;
    }

}
