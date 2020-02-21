package ru.alfabank.alfamir.search.domain.mapper;

import javax.inject.Inject;

import io.reactivex.functions.Function;
import ru.alfabank.alfamir.search.data.dto.PersonRaw;
import ru.alfabank.alfamir.search.presentation.dto.Person;

public class PersonMapper implements Function<PersonRaw, Person> {

    @Inject
    PersonMapper(){ }

    @Override
    public Person apply(PersonRaw personRaw) throws Exception {
        String id = personRaw.getId();
        String imageUrl = personRaw.getImageUrl();
        String name = personRaw.getName();
        String position = personRaw.getPosition();
        Person person = new Person.Builder()
                .id(id)
                .imageUrl(imageUrl)
                .name(name)
                .posiiton(position)
                .build();
        return person;
    }
}