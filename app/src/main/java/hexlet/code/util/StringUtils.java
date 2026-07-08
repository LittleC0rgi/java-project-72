package hexlet.code.util;

public class StringUtils {
    private static final int MAX_LENGTH = 200;

    public static String truncate(String text) {
        if (text == null) {
            return "";
        }

        if (text.length() <= MAX_LENGTH) {
            return text;
        }

        return text.substring(0, MAX_LENGTH) + "...";
    }
}
