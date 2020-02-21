package alfamir.alfabank.ru.air.utilities;

import com.google.common.base.Strings;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class InitialsProvider {

    @Test
    public void test(){
        List<String> names = new ArrayList<>();
        names.add("Авдуевский Михаил Сергеевич");
        names.add("Авдуевский Михаил Сергеевич Оглы");
        names.add("Авдуевский Михаил");
        names.add("System");
        names.add(null);
        for(String name : names){
            System.out.println(formInitials(name));
        }

    }

    public String formInitials(String name) {
        if(Strings.isNullOrEmpty(name)) return "";
        name = name.trim();
        String [] namePart = name.split(" ");
        int namePartCount = namePart.length;

        if(namePartCount == 1){
            return namePart[0].substring(0,1).toUpperCase();
        } else if (namePartCount == 2){
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(namePart[1].substring(0,1).toUpperCase());
            sBuilder.append(namePart[0].substring(0,1).toUpperCase());
            return sBuilder.toString();
        } else if (namePartCount > 2){
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(namePart[1].substring(0,1).toUpperCase());
            sBuilder.append(namePart[2].substring(0,1).toUpperCase());
            return sBuilder.toString();
        }
        return "";
    }

}
