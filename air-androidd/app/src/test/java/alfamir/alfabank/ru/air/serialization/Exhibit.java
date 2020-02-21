package alfamir.alfabank.ru.air.serialization;

import alfamir.alfabank.ru.air.serialization.Animal;

public class Exhibit {

    private String mDescription;
    private Animal mAnimal;

    public Exhibit() {
        mDescription = "This is a public exhibit.";
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String pDescription) {
        mDescription = pDescription;
    }

    public Animal getAnimal() {
        return mAnimal;
    }

    public void setAnimal(Animal pAnimal) {
        mAnimal = pAnimal;
    }

}
