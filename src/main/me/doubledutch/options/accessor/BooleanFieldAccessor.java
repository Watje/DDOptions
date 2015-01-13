package me.doubledutch.options.accessor;

import me.doubledutch.options.Result;

import java.lang.reflect.Field;

import static me.doubledutch.options.Result.success;

public class BooleanFieldAccessor extends AbstractFieldAccessor<Boolean> {
    @Override
    public boolean supports(String type) {
        return "boolean".equalsIgnoreCase(type);
    }

    @Override
    protected Boolean convertValue(String value) {
        return true;
    }

    @Override
    protected Result setDirectlyOnField(Object target, Field field, Boolean value) throws IllegalAccessException {
        field.setBoolean(target, value);
        return success();
    }
}
