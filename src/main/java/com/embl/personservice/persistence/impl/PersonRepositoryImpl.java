package com.embl.personservice.persistence.impl;

import com.embl.personservice.domain.Person;
import com.embl.personservice.persistence.PersonRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository("personRepository")
public class PersonRepositoryImpl implements PersonRepository {

    private final Map<String, Person> personList = new ConcurrentHashMap<>();

    @Override
    public Optional<Person> getPerson(String key) {
        return Optional.ofNullable(personList.get(key));
    }

    @Override
    public List<Person> getAllPerson() {
        return new ArrayList<>(personList.values());
    }

    @Override
    public Optional<Person> addPerson(String key, Person person) {
        return Optional.ofNullable(personList.putIfAbsent(key, person) == null ? person : null);
    }

    @Override
    public Optional<Person> replacePerson(String key, Person person) {
        return Optional.ofNullable(personList.replace(key, person) != null ? person : null);
    }

    @Override
    public boolean removePerson(String key) {
        return personList.remove(key) != null ? true : false;
    }

}