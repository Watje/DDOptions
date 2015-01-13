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

package me.doubledutch.options.accessor;

import me.doubledutch.options.Result;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Date;

import static me.doubledutch.options.Result.success;

public class TimestampFieldAccessor extends AbstractFieldAccessor<Long> {
    @Override
    public boolean supports(String type) {
        return "timestamp".equalsIgnoreCase(type);
    }

    @Override
    protected Long convertValue(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException expected) {
            /**
             * More information can be found at:
             * http://books.xmlschemata.org/relaxng/ch19-77049.html
             */
            return javax.xml.bind.DatatypeConverter.parseDateTime(value).getTimeInMillis();
        }
    }

    @Override
    protected Result setDirectlyOnField(Object target, Field field, Long value) throws IllegalAccessException {
        Object dateRepresentation = convertToCorrectDateRepresentation(field.getType(), value);
        field.set(target, dateRepresentation);
        return success();
    }

    private Object convertToCorrectDateRepresentation(Class type, Long value) {
        if (Date.class.isAssignableFrom(type)) return new Date(value);
        if (Calendar.class.isAssignableFrom(type)) return createCalendarWithMillis(value);
        return value;
    }

    protected static Calendar createCalendarWithMillis(Long value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(value);
        return calendar;
    }
}
