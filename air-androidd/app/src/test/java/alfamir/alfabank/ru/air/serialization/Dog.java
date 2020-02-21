package alfamir.alfabank.ru.air.serialization;

public class Dog implements Animal{
    String klichka = "Bobick";
    String action = "Bark and Byte";

    @Override
    public String getName() {
        return klichka;
    }
}
