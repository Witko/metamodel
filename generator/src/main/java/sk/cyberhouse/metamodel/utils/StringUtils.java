package sk.cyberhouse.metamodel.utils;

import java.util.List;

public class StringUtils {

    public static String quote(String value) {
        return "\"" + value + "\"";
    }

    public static String join(List<String> params) {
        return join(params.toArray(new String[params.size()]));
    }

    public static String join(String[] params) {
        return join(params, ", ");
    }

    public static String join(List<String> params, String delimiter) {
        return join(params.toArray(new String[params.size()]), delimiter);
    }

    public static String join(String[] params, String delimiter) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < params.length; i++) {
            builder.append(params[i]);
            if (i < params.length - 1) {
                builder.append(delimiter);
            }
        }
        return builder.toString();
    }
}
