package me.doubledutch.options;

//    Copyright [2015] [DoubleDutch]
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//       http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

import me.doubledutch.options.accessor.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class DDOptions {
    private static Pattern optionPattern = compile("([^\\:]+)(\\:([^\\-]+)(\\-\\>(.+))?)?");
    private static Set<FieldAccessor> accessors = new HashSet<>();

    static {
        accessors.add(new BooleanFieldAccessor());
        accessors.add(new StringFieldAccessor());
        accessors.add(new IntegerFieldAccessor());
        accessors.add(new FloatFieldAccessor());
        accessors.add(new TimestampFieldAccessor());
    }

    /**
     * Splits the option configuration format into its three possible parts.
     *
     * @param option the option configuration to be parsed
     * @return The three possible components of an option configuration or null if the configuration could not be parsed.
     */
    protected static String[] splitOption(String option) {
        String[] result = new String[3];
        Matcher m = optionPattern.matcher(option);
        if (m.matches()) {
            result[0] = m.group(1);
            result[1] = m.group(3);
            result[2] = m.group(5);
            // If no type is given, boolean is used
            if (result[1] == null) result[1] = "boolean";
            // If no target field is given, the option name is used
            if (result[2] == null) result[2] = result[0];
            return result;
        }
        return null;
    }

    /**
     * Iterates through a set of options to find the one matching the given key - if it exists.
     *
     * @param key       The name of the option to find
     * @param optionSet The set of option configuration strings
     * @return The requested option configuration string or null if it could not be found
     */
    protected static String getOption(String key, String[] optionSet) {
        for (String option : optionSet) {
            String[] parsed = splitOption(option);
            if (parsed != null) {
                if (parsed[0].equals(key)) {
                    return option;
                }
            }
        }
        return null;
    }

    /**
     * Determines wether or not an option takes an argument. Currently, everything but booleans require an argument.
     * The output is undefined (read, it will also return true) if the option configuration could not be parsed.
     *
     * @param option the option configuration to check
     * @return true if the given option takes an argument.
     */
    protected static boolean needsArgument(String option) {
        String[] parsed = splitOption(option);
        if (parsed != null) {
            if ("boolean".equals(parsed[1])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Attempts to parse and set an option on the target object.
     *
     * @return Null if it was successful or an error message if the option could not be set.
     */
    private static String setOption(Object target, String value, String option) {
        String[] split = splitOption(option);
        String key = split[0];
        String field = split[2];
        String type = split[1];

        for (FieldAccessor accessor : accessors) {
            if (accessor.supports(type)) {
                Result result = accessor.set(target, field, value);
                if (result.isFailure()) {
                    return "Problem with " + key + ": " + result.errorMessage();
                }
                return null;
            }
        }
        return "The configuration for -" + key + " is invalid - this is an application error";
    }

    /**
     * Parse a set of options matchning the given configuration and attempt to set it on the given target object.
     * <p/>
     * The format of a single option configuration is as follows:
     * &lt;option&gt; = &lt;identifier&gt; (':' &lt;type&gt; ('->' &lt;mappedIdentifier&gt;)?)?
     * <p/>
     * If no type is specified, boolean is assumed. If no mapped identifier is specified, the base identifier is used.
     * Here are some samples of valid option configurations:
     * <ul>
     * <li>help
     * <li>count:integer
     * <li>threads:integer->setThreadCount
     * <li>log:string->logFilename
     * </ul>
     */
    public static List<String> setOptions(Object target, String[] data, String... optionSet) {
        List<String> errors = new LinkedList<String>();

        // Run through all arguments
        for (int index = 0; index < data.length; index++) {
            String value = data[index];
            if (value.startsWith("-")) {
                String key = value.substring(1);
                if (key.startsWith("-")) {
                    key = key.substring(1);
                }
                String option = getOption(key, optionSet);
                if (option != null) {
                    if (needsArgument(option)) {
                        if (index + 1 < data.length) {
                            index += 1;
                            String arg = data[index];
                            String result = setOption(target, arg, option);
                            if (result != null) {
                                errors.add(result);
                            }
                        } else {
                            errors.add("Missing argument for option " + value);
                        }
                    } else {
                        String result = setOption(target, null, option);
                        if (result != null) {
                            errors.add(result);
                        }
                    }
                }
            }
        }
        return errors;
    }
}