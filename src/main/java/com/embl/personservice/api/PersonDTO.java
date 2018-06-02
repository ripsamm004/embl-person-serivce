package com.embl.personservice.api;


import com.embl.personservice.domain.Person;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@ToString
public class PersonDTO {
    private final List<Person> person;

    public PersonDTO(Person persons){
        person= new ArrayList<>();
        this.person.add(persons);
    }

    public List<Person> getPerson() {
        return Collections.unmodifiableList(person);
    }
}