package com.embl.personservice.api;


import com.embl.personservice.domain.Person;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class PersonDTO {
    private final List<Person> person;

    public PersonDTO(Person persons){
        person= new ArrayList<>();
        this.person.add(persons);
    }
}