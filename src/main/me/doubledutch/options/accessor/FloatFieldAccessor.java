package me.doubledutch.options.accessor;

import me.doubledutch.options.FieldAccessException;
import me.doubledutch.options.Result;

import java.lang.reflect.Field;

import static me.doubledutch.options.Result.success;

public class FloatFieldAccessor extends AbstractFieldAccessor<Float> {
    @Override
    public boolean supports(String type) {
        return "float".equals(type);
    }

    @Override
    protected Float convertValue(String value) {
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            throw new FieldAccessException("The argument is not valid a valid number", e);
        }
    }

    @Override
    protected Result setDirectlyOnField(Object target, Field field, Float value) throws IllegalAccessException {
        field.setFloat(target, value);
        return success();
    }
}
