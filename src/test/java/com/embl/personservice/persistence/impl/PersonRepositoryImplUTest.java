package com.embl.personservice.persistence.impl;


import com.embl.personservice.domain.Person;
import com.embl.personservice.persistence.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class PersonRepositoryImplUTest {

    private PersonRepository personRepository;

    @Before
    public void setup(){
        personRepository = new PersonRepositoryImpl();
    }

    @Test
    public void testWhenPersonNotExistThenExpectEmpty() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        Optional<Person> person = personRepository.getPerson(key);
        assertTrue(!person.isPresent());
    }

    @Test
    public void testWhenPersonExistThenExpectPerson() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        personRepository.addPerson(key, p0);

        Optional<Person> person = personRepository.getPerson(key);
        assertTrue(person.isPresent());
    }

    @Test
    public void testWhenPersonExistThenReturnListOfPerson() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key0 = p0.getFirst_name()+p0.getLast_name();
        Person p1 = new Person("Adam", "Smith", "30", "blue");
        String key1 = p1.getFirst_name()+p1.getLast_name();
        personRepository.addPerson(key0, p0);
        personRepository.addPerson(key1, p1);
        List<Person> personList = personRepository.getAllPerson();
        assertThat(personList.size(), is(2));
    }

    @Test
    public void testWhenNoPersonExistThenReturnEmptyList() {
        List<Person> personList = personRepository.getAllPerson();
        assertThat(personList.size(), is(0));
    }

    @Test
    public void testGivenPersonIfNotExistThenAddSuccessReturnPerson() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        Optional<Person> person = personRepository.addPerson(key, p0);
        assertTrue(person.isPresent());
    }

    @Test
    public void testGivenPersonIfExistThenAddFailReturnEmpty() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        personRepository.addPerson(key, p0);
        Optional<Person> person = personRepository.addPerson(key, p0);
        assertTrue(!person.isPresent());
    }

    @Test
    public void testGivenPersonIfExistThenUpdatePersonSuccess() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        personRepository.addPerson(key, p0);
        Optional<Person> person = personRepository.replacePerson(key, p0);
        assertTrue(person.isPresent());
    }

    @Test
    public void testGivenPersonIfNotExistThenAddPersonFail() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        Optional<Person> person = personRepository.replacePerson(key, p0);
        assertTrue(!person.isPresent());
    }

    @Test
    public void testGivenPersonIfExistThenRemovePersonSuccess() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        personRepository.addPerson(key, p0);
        boolean expected =  personRepository.removePerson(key);
        assertTrue(expected);
    }

    @Test
    public void testGivenPersonIfNotExistThenRemovePersonFail() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        boolean expected =  personRepository.removePerson(key);
        assertFalse(expected);
    }

}
