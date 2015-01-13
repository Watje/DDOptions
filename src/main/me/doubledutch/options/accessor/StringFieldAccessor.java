package me.doubledutch.options.accessor;

import me.doubledutch.options.FieldAccessor;
import me.doubledutch.options.Result;

import java.lang.reflect.Field;

import static me.doubledutch.options.Result.success;

public class StringFieldAccessor extends AbstractFieldAccessor<String> {
    @Override
    public boolean supports(String type) {
        return "string".equalsIgnoreCase(type);
    }

    @Override
    protected String convertValue(String value) {
        return value;
    }

    @Override
    protected Result setDirectlyOnField(Object target, Field field, String value) throws IllegalAccessException {
        field.set(target, value);
        return success();
    }
}
