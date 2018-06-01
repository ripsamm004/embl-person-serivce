package com.embl.personservice.api;


import com.embl.personservice.domain.Person;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
public class PersonDTO {
    private static final List<Person> person = new ArrayList<Person>();

    public PersonDTO(List<Person> persons){
        person.addAll(persons);
    }

    public PersonDTO(Person persons){
        person.add(persons);
    }
}