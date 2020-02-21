package alfamir.alfabank.ru.air.dates;

import org.junit.Test;

import java.util.Locale;

import ru.alfabank.alfamir.utility.date_formatter.DateFormatterImpl;

import static ru.alfabank.alfamir.Constants.DateFormat.DATE_PATTERN_9;

public class DateConverter {

    private static Locale russian = new Locale("ru");

    @Test
    public void convertString() {
        DateFormatterImpl dateFormatter = new DateFormatterImpl();

        String fDate = dateFormatter.getCurrentUtcTime(DATE_PATTERN_9);
        System.out.println(fDate);
    }
}
