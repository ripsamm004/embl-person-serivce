package com.embl.personservice.service;

import com.embl.personservice.api.exception.NotFoundException;
import com.embl.personservice.domain.Person;
import com.embl.personservice.exception.ValidatorPersonAlreadyExistException;
import com.embl.personservice.persistence.PersonRepository;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;


@RunWith(SpringJUnit4ClassRunner.class)
public class PersonServiceUTest {


    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Mock
    protected PersonRepository personRepository;

    @InjectMocks
    private PersonService personService;


    @Test
    public void testWhenPersonExistThenReturnPersonByFirstAndLastName() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        Optional<Person> personOptional = Optional.of(p0);
        when(personRepository.getPerson(key)).thenReturn(personOptional);

        Person  personExpected = personService.getPerson(p0.getFirst_name(), p0.getLast_name());
        verify(personRepository, times(1)).getPerson(key);

        assertEquals(personExpected, p0);
        assertEquals(personExpected.getFirst_name(), p0.getFirst_name());
        assertEquals(personExpected.getLast_name(), p0.getLast_name());
        assertEquals(personExpected.getAge(), p0.getAge());
        assertEquals(personExpected.getFavourite_colour(), p0.getFavourite_colour());
    }

    @Test
    public void testGivenPersonByFirstAndLastNameWhenPersonNotExistThenThrowException() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        when(personRepository.getPerson(key)).thenReturn(Optional.<Person>empty());

        thrown.expect(NotFoundException.class);
        thrown.expectMessage(equalTo("Person not found"));
        personService.getPerson(p0.getFirst_name(), p0.getLast_name());
    }

    @Test
    public void testWhenPersonExistThenReturnListOfPerson() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        Person p1 = new Person("Adam", "Smith", "40", "blue");
        List<Person> personList = new ArrayList<>();
        personList.add(p0);
        personList.add(p1);

        when(personRepository.getAllPerson()).thenReturn(personList);
        List<Person> personListExpected = personService.getAllPerson();
        verify(personRepository, times(1)).getAllPerson();

        assertThat(personListExpected.size(), Matchers.is(2));
        assertEquals(personListExpected.get(0), p0);
        assertEquals(personListExpected.get(1), p1);
    }

    @Test
    public void testWhenNoPersonExistThenReturnListWithSizeZero() {
        List<Person> personList = new ArrayList<>();
        when(personRepository.getAllPerson()).thenReturn(personList);
        List<Person> personListExpected = personService.getAllPerson();
        assertThat(personListExpected.size(), Matchers.is(0));
    }


    @Test
    public void testGivenPersonIfNotExistWhenAddPersonThenAddSuccess() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();

        Optional<Person> personOptional = Optional.of(p0);
        when(personRepository.addPerson(key, p0)).thenReturn(personOptional);
        Person  personExpected  = personService.addPerson(p0);

        verify(personRepository, times(1)).addPerson(key, p0);

        assertEquals(personExpected, p0);
        assertEquals(personExpected.getFirst_name(), p0.getFirst_name());
        assertEquals(personExpected.getLast_name(), p0.getLast_name());
        assertEquals(personExpected.getAge(), p0.getAge());
        assertEquals(personExpected.getFavourite_colour(), p0.getFavourite_colour());
    }

    @Test
    public void testGivenPersonExistWhenAddPersonWithSameFirstAndLastNameThenAddFail() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        Person p1 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();

        when(personRepository.addPerson(key, p0)).thenReturn(Optional.<Person>empty());

        thrown.expect(ValidatorPersonAlreadyExistException.class);
        thrown.expectMessage(equalTo("Person exist"));
        personService.addPerson(p1);
    }


    @Test
    public void testGivenPersonExistWhenUpdatePersonThenUpdateSuccess() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        Person p1 = new Person("John", "Keynes", "30", "blue");
        String key = p0.getFirst_name()+p0.getLast_name();

        Optional<Person> personOptional = Optional.of(p1);
        when(personRepository.replacePerson(key, p0)).thenReturn(personOptional);
        Person  personExpected  = personService.replacePerson(p1);

        verify(personRepository, times(1)).replacePerson(key, p1);

        assertEquals(personExpected, p1);
        assertEquals(personExpected.getFirst_name(), p1.getFirst_name());
        assertEquals(personExpected.getLast_name(), p1.getLast_name());
        assertEquals(personExpected.getAge(), p1.getAge());
        assertEquals(personExpected.getFavourite_colour(), p1.getFavourite_colour());
    }

    @Test
    public void testGivenPersonNotExistWhenUpdatePersonThenUpdateSuccess() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        Person p1 = new Person("John", "Keynes", "30", "blue");
        String key = p0.getFirst_name()+p0.getLast_name();

        when(personRepository.replacePerson(key, p0)).thenReturn(Optional.<Person>empty());
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(equalTo("Person not found"));
        personService.replacePerson(p1);
    }

    @Test
    public void testGivenPersonExistWhenDeletePersonThenNoException() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        when(personRepository.removePerson(key)).thenReturn(true);
        personService.removePerson(p0.getFirst_name(), p0.getLast_name());
    }

    @Test
    public void testGivenPersonNotExistWhenDeletePersonThenThrowException() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        String key = p0.getFirst_name()+p0.getLast_name();
        when(personRepository.removePerson(key)).thenReturn(false);
        thrown.expect(NotFoundException.class);
        thrown.expectMessage(equalTo("Person not fount"));
        personService.removePerson(p0.getFirst_name(), p0.getLast_name());
    }


}
