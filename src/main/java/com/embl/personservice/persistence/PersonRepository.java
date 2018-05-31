package com.embl.personservice.persistence;

import com.embl.personservice.domain.Person;

import java.util.List;
import java.util.Optional;

public interface PersonRepository {

    Optional<Person> getPerson(String key);
    List<Person> getAllPerson();
    Optional<Person> addPerson(String key, Person person);
    Optional<Person> replacePerson(String key, Person person);
    boolean removePerson(String key);
}