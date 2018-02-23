package utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class Utils {
    private Utils() {
    }

    public static String defaultDateFormat(long date) {
        SimpleDateFormat fmt = new SimpleDateFormat();
        fmt.applyPattern("EEE, dd MMM yyyy");
        return fmt.format(new Date(date)).toUpperCase();
    }
}
