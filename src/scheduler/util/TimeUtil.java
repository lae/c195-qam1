package scheduler.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import scheduler.State;

import java.sql.Timestamp;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper functions for dealing with time and dates.
 *
 * @author Musee Ullah
 */
public class TimeUtil {
    private static final ZoneId utcZone = ZoneId.of("Etc/UTC");
    private static final ZoneId localZone = ZoneId.systemDefault();

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
        return timestamp.toLocalDateTime().atZone(utcZone).withZoneSameInstant(localZone).toLocalDateTime();
    }

    /**
     * @param dt a local time and date
     * @return the converted UTC timestamp
     */
    public static Timestamp localToTS(LocalDateTime dt) {
        return Timestamp.valueOf(dt.atZone(localZone).withZoneSameInstant(utcZone).toLocalDateTime());
    }

    /**
     * Generates a list of start and end times within the user's local timezone that fall under business hours (8am to
     * 10pm within the timezone configured in State) at equal intervals (e.g. 30 minutes).
     *
     * @return a 2 item list containing a list of start times and a list of end times.
     */
    public static List<ObservableList<LocalTime>> generateBusinessHours() {
        ZonedDateTime businessStart = LocalDate.now().atTime(8, 0).atZone(State.getBusinessZone());
        ZonedDateTime businessEnd = LocalDate.now().atTime(22, 0).atZone(State.getBusinessZone());

        ArrayList<ObservableList<LocalTime>> times = new ArrayList<>();
        times.add(FXCollections.observableArrayList());
        times.add(FXCollections.observableArrayList());
        int interval = 30;

        for (ZonedDateTime zt = businessStart; zt.isBefore(businessEnd); zt = zt.plusMinutes(interval)) {
            LocalTime lt = zt.withZoneSameInstant(localZone).toLocalTime();
            times.get(0).add(lt);
            times.get(1).add(lt.plusMinutes(interval));
        }

        return times;
    }
}
