package me.doubledutch.options.accessor;

import me.doubledutch.options.FieldAccessException;
import me.doubledutch.options.Result;

import java.lang.reflect.Field;

import static me.doubledutch.options.Result.success;

public class IntegerFieldAccessor extends AbstractFieldAccessor<Integer> {
    @Override
    public boolean supports(String type) {
        return "integer".equals(type);
    }

    @Override
    protected Integer convertValue(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new FieldAccessException("The argument is not valid a valid number", e);
        }
    }

    @Override
    protected Result setDirectlyOnField(Object target, Field field, Integer value) throws IllegalAccessException {
        field.setInt(target, value);
        return success();
    }
}
