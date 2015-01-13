package me.doubledutch.options.accessor;

import me.doubledutch.options.FieldAccessException;
import me.doubledutch.options.FieldAccessor;
import me.doubledutch.options.Result;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static me.doubledutch.options.Result.failure;
import static me.doubledutch.options.Result.success;

public abstract class AbstractFieldAccessor<T> implements FieldAccessor {
    @Override
    public Result set(Object target, String field, String value) {

        try {
            T convertedValue = convertValue(value);
            Field objField = getOptionField(target, field);
            return objField != null ?
                    setDirectlyOnField(target, objField, convertedValue) :
                    setOnFieldThroughSetter(target, field, convertedValue);
        } catch (FieldAccessException e) {
            return failure(e, e.getMessage());
        } catch (Exception e) {
            return failure(e, "this is an application error");
        }
    }

    private Result setOnFieldThroughSetter(Object target, String field, T convertedValue) throws IllegalAccessException, InvocationTargetException {
        Method objMethod = searchForOptionMethod(target, field, convertedValue.getClass());
        objMethod.invoke(target, convertedValue);
        return success();
    }

    protected abstract T convertValue(String value);

    protected abstract Result setDirectlyOnField(Object target, Field field, T value) throws IllegalAccessException;

    /**
     * Uses getOptionMethod to search for methods either matching the name, or matching the name with a "set" in front of it.
     * This having field set to user will search for methods called user and methods called setUser.
     */
    protected static Method searchForOptionMethod(Object target, String field, Class type) {
        Method m = getOptionMethod(target, field, type);
        if (m != null) {
            return m;
        }
        String searchName = "set" + field.substring(0, 1).toUpperCase() + field.substring(1);
        return getOptionMethod(target, searchName, type);
    }

    /**
     * Attempts to use reflection to get the method on an object matching the given name and type.
     * If the method is found, it will be set to be accessible even if it's private or protected.
     *
     * @param target     the object whose class should be searched for the given method.
     * @param methodName the name of the method to locate
     * @param type       the class type of the method's single argument
     * @return the method object if it exists, null otherwise
     */
    protected static Method getOptionMethod(Object target, String methodName, Class type) {
        try {
            Method m = target.getClass().getDeclaredMethod(methodName, type);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException ignored) {
        }
        return null;
    }

    /**
     * Attempts to use reflection to get the Field on an object matching the given name.
     * If the field is found, it will be set to be accessible even if it's private or protected.
     *
     * @param target the object whose class should be searched for the given field.
     * @param field  the name of the field to locate
     * @return the field object if it exists, null otherwise
     */
    protected static Field getOptionField(Object target, String field) {
        try {
            Field f = target.getClass().getDeclaredField(field);
            if (f != null) {
                f.setAccessible(true);
                return f;
            }
        } catch (NoSuchFieldException ignored) {
        }
        return null;
    }
}
