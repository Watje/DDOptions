package me.doubledutch.options;

public interface FieldAccessor {
    boolean supports(String type);
    Result set(Object target, String field, String value);
}
