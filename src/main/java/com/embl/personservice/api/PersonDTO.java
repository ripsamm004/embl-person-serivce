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

    public static void addAllPerson(List<Person> persons){
        this.person.addAll(persons);
    }

    public void addPerson(Person persons){
        this.person.add(persons);
    }
}