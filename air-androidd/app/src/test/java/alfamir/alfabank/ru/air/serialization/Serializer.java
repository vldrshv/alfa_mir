package alfamir.alfabank.ru.air.serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.junit.Test;

public class Serializer {

    @Test
    public void main(){
        test3();
    }

    public void test (){
        Exhibit exhibit = new Exhibit();
        exhibit.setAnimal(new Cat());
        Gson gson = new GsonBuilder().registerTypeAdapter(Animal.class, new InterfaceAdapter2<Animal>())
                .create();
        String jsonString = gson.toJson(exhibit);
        System.out.println(jsonString);
        Exhibit deserializedExhibit = gson.fromJson(jsonString, Exhibit.class);
        System.out.println(deserializedExhibit);
    }

    public void test2 (){
        Cat cat = new Cat();
        Gson gson = new GsonBuilder().registerTypeAdapter(Animal.class, new InterfaceAdapter2<Animal>())
                .create();
        String json = gson.toJson(cat);
        System.out.println(json);
        Animal deserializedCat = gson.fromJson(json, Animal.class);
        System.out.println(deserializedCat);
    }

    public void test3(){
        Box box = new Box();
        Cat cat = new Cat();
        Dog dog = new Dog();
        box.setCat(cat);
        box.setCat(dog);

        Gson gson = new GsonBuilder().registerTypeAdapter(Animal.class,
                new InterfaceAdapter2<Animal>())
                .create();
        String json = gson.toJson(box);
        System.out.println(json);
        Box deserializedBox = gson.fromJson(json, Box.class);
        System.out.println(deserializedBox);
    }

}
