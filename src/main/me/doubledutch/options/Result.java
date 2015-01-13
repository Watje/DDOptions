/*
 * Copyright [2015] [DoubleDutch]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
