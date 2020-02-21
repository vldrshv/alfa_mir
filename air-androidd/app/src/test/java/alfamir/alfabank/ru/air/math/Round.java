package alfamir.alfabank.ru.air.math;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class Round {

    @Test
    public void round(){
        double doubleVar = 392.7989;
        int a = (int) Math.round(doubleVar);
        System.out.println(a);
    }
}
