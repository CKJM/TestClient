package water.godcoder.setting;

import com.google.common.base.Converter;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import water.godcoder.setting.builder.SettingBuilder;
import water.godcoder.setting.builder.numerical.DoubleSettingBuilder;
import water.godcoder.setting.builder.numerical.FloatSettingBuilder;
import water.godcoder.setting.builder.numerical.IntegerSettingBuilder;
import water.godcoder.setting.builder.numerical.NumericalSettingBuilder;
import water.godcoder.setting.builder.primitive.BooleanSettingBuilder;
import water.godcoder.setting.builder.primitive.EnumSettingBuilder;
import water.godcoder.setting.builder.primitive.StringSettingBuilder;

public class Settings {
   public static FloatSettingBuilder floatBuilder() {
      return new FloatSettingBuilder();
   }

   public static DoubleSettingBuilder doubleBuilder() {
      return new DoubleSettingBuilder();
   }

   public static IntegerSettingBuilder integerBuilder() {
      return new IntegerSettingBuilder();
   }

   public static BooleanSettingBuilder booleanBuilder() {
      return new BooleanSettingBuilder();
   }

   public static StringSettingBuilder stringBuilder() {
      return new StringSettingBuilder();
   }

   public static EnumSettingBuilder enumBuilder(Class clazz) {
      return new EnumSettingBuilder(clazz);
   }

   public static Setting f(String name, float value) {
      return floatBuilder(name).withValue((Number)value).build();
   }

   public static Setting d(String name, double value) {
      return doubleBuilder(name).withValue((Number)value).build();
   }

   public static Setting i(String name, int value) {
      return integerBuilder(name).withValue((Number)value).build();
   }

   public static Setting b(String name, boolean value) {
      return booleanBuilder(name).withValue(value).build();
   }

   public static Setting b(String name) {
      return booleanBuilder(name).withValue(true).build();
   }

   public static Setting s(String name, String value) {
      return stringBuilder(name).withValue(value).build();
   }

   public static Setting e(String name, Enum value) {
      return enumBuilder(value.getClass()).withName(name).withValue(value).build();
   }

   public static NumericalSettingBuilder floatBuilder(String name) {
      return (new FloatSettingBuilder()).withName(name);
   }

   public static NumericalSettingBuilder doubleBuilder(String name) {
      return (new DoubleSettingBuilder()).withName(name);
   }

   public static NumericalSettingBuilder integerBuilder(String name) {
      return (new IntegerSettingBuilder()).withName(name);
   }

   public static BooleanSettingBuilder booleanBuilder(String name) {
      return (new BooleanSettingBuilder()).withName(name);
   }

   public static StringSettingBuilder stringBuilder(String name) {
      return (StringSettingBuilder)(new StringSettingBuilder()).withName(name);
   }

   public static SettingBuilder custom(String name, Object initialValue, final Converter converter, Predicate restriction, BiConsumer consumer, Predicate visibilityPredicate) {
      return (new SettingBuilder() {
         public Setting build() {
            return new Setting(this.initialValue, this.predicate(), this.consumer, this.name, this.visibilityPredicate()) {
               public Converter converter() {
                  return converter;
               }
            };
         }
      }).withName(name).withValue(initialValue).withConsumer(consumer).withVisibility(visibilityPredicate).withRestriction(restriction);
   }

   public static SettingBuilder custom(String name, Object initialValue, Converter converter, Predicate restriction, BiConsumer consumer, boolean hidden) {
      return custom(name, initialValue, converter, restriction, consumer, (t) -> {
         return !hidden;
      });
   }

   public static SettingBuilder custom(String name, Object initialValue, Converter converter, Predicate restriction, boolean hidden) {
      return custom(name, initialValue, converter, restriction, (t, t2) -> {
      }, hidden);
   }

   public static SettingBuilder custom(String name, Object initialValue, Converter converter, boolean hidden) {
      return custom(name, initialValue, converter, (input) -> {
         return true;
      }, (t, t2) -> {
      }, hidden);
   }

   public static SettingBuilder custom(String name, Object initialValue, Converter converter) {
      return custom(name, initialValue, converter, (input) -> {
         return true;
      }, (t, t2) -> {
      }, false);
   }
}
