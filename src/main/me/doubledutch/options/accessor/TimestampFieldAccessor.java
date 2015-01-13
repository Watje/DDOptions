package me.doubledutch.options.accessor;

import me.doubledutch.options.Result;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static me.doubledutch.options.Result.success;

public class TimestampFieldAccessor extends AbstractFieldAccessor<Long> {
    @Override
    public boolean supports(String type) {
        return "timestamp".equalsIgnoreCase(type);
    }

    @Override
    protected Long convertValue(String value) {
        return javax.xml.bind.DatatypeConverter.parseDateTime(value).getTimeInMillis();
    }

    @Override
    protected Result setDirectlyOnField(Object target, Field field, Long value) throws IllegalAccessException {
        field.setLong(target, value);
        return success();
    }
}
