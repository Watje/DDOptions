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

/**
 * Implementing classes are responsible for actually setting
 * the value of a field on an instance. The type of field defines
 * how the value is converted to and instance.
 *
 * E.g. if {@link #supports(String)} supports type "boolean" then
 * {@link #set(Object, String, String)} would probably convert values
 * "1", "0", "true", "false", "yes", "no" to corresponding boolean
 * object and set it to the field.
 *
 * @see me.doubledutch.options.accessor.AbstractFieldAccessor
 */
public interface FieldAccessor {
    boolean supports(String type);
    Result set(Object target, String field, String value);
}
