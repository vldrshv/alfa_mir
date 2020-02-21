package alfamir.alfabank.ru.air.serialization;

public class Cat implements Animal {

    private String mName = "Cat";
    private String mHabbit = "Playing with yarn";

    public String getName() {
        return mName;
    }

    public void setName(String pName) {
        mName = pName;
    }

    public String getHabbit() {
        return mHabbit;
    }

    public void setHabbit(String pHabbit) {
        mHabbit = pHabbit;
    }

}
