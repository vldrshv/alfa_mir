package alfamir.alfabank.ru.air.serialization;

import java.util.ArrayList;
import java.util.List;

public class Box {
    List<Animal> mAnimals = new ArrayList<>();

    public void setCat(Animal animal){
        mAnimals.add(animal);
    }
}
