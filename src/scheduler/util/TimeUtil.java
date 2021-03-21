package scheduler.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Helper functions for dealing with time and dates.
 *
 * @author Musee Ullah
 */
public class TimeUtil {
    private static final ZoneId utcZone = ZoneId.of("Etc/UTC");

    /**
     * @return a LocalDateTime object with the current UTC date and time.
     */
    public static LocalDateTime utcNow() {
        ZonedDateTime utcDT = ZonedDateTime.now(utcZone);
        return utcDT.toLocalDateTime();
    }

    /**
     * @param timestamp a UTC timestamp
     * @return the converted local time and date
     */
    public static LocalDateTime tsToLocal(Timestamp timestamp) {
        ZoneId localZone = ZoneId.systemDefault();
        return timestamp.toLocalDateTime().atZone(utcZone).withZoneSameInstant(localZone).toLocalDateTime();
    }

    /**
     * @param dt a local time and date
     * @return the converted UTC timestamp
     */
    public static Timestamp localToTS(LocalDateTime dt) {
        ZoneId localZone = ZoneId.systemDefault();
        return Timestamp.valueOf(dt.atZone(localZone).withZoneSameInstant(utcZone).toLocalDateTime());
    }
}
