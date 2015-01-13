package me.doubledutch.options;

public class Result {
    Exception exception;
    String errorMessage;

    public static Result success() {
        return new Result();
    }

    public static Result failure(Exception e, String errorMessage) {
        return new Result().markAsFailed(e, errorMessage);
    }

    private Result markAsFailed(Exception e, String message) {
        this.exception = e;
        this.errorMessage = message;
        return this;
    }

    public boolean isFailure() {
        return exception != null || errorMessage != null;
    }

    public String errorMessage() {
        return errorMessage;
    }
}
