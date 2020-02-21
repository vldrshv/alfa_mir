package alfamir.alfabank.ru.air.math;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Random {

    List<Long> mLongList = new ArrayList<>();

    @Test
    public void test(){



        List<String> items = new ArrayList<>();
        items.add("pasha");
        items.add("misha");
        items.add("ulya");
        for(int i = 0; i < items.size(); i++) System.out.println(items.get(i) + " i = " + i);

        System.out.println(items.size());

//        for(int i = 0; i < 10000000; i++){
//            long test = getRandomLong();
//            if(test==0){
//                System.out.println("It's 0!");
//            }
//        }
    }

    private long getRandomLong(){
        long viewId = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
        return viewId;
    }

}
