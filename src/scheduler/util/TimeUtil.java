package scheduler.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class TimeUtil {
    /**
     * @return a LocalDateTime object with the current UTC date and time.
     */
    public static LocalDateTime utcNow() {
        ZoneId utcZone = ZoneId.of("Etc/UTC");
        ZonedDateTime utcDT = ZonedDateTime.now(utcZone);
        return utcDT.toLocalDateTime();
    }
}
