package com.embl.personservice.service;

import com.embl.personservice.api.ErrorEnum;
import com.embl.personservice.api.exception.NotFoundException;
import com.embl.personservice.domain.Person;
import com.embl.personservice.exception.ValidatorPersonAlreadyExistException;
import com.embl.personservice.persistence.PersonRepository;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

@Service
public class PersonService
{

    @Inject
    protected PersonRepository personRepository;

    public Person getPerson(String firstName, String lastName) {
        String key = firstName+lastName;
        return personRepository.getPerson(key).orElseThrow(() -> new NotFoundException("Person not found", ErrorEnum.API_ERROR_PERSON_NOT_FOUND));
    }

    public List<Person> getAllPerson() {
        return personRepository.getAllPerson();
    }

    public Person addPerson(Person person) {
        String key = person.getFirst_name()+person.getLast_name();
        return personRepository.addPerson(key, person)
                .orElseThrow(() -> new ValidatorPersonAlreadyExistException("Person exist", ErrorEnum.API_ERROR_PERSON_ALREADY_EXIST));
    }

    public Person replacePerson(Person person) {
        String key = person.getFirst_name()+person.getLast_name();
        return personRepository.replacePerson(key, person)
                .orElseThrow(() -> new NotFoundException("Person not found", ErrorEnum.API_ERROR_PERSON_NOT_FOUND));
    }

    public void removePerson(String firstName, String lastName) {
        String key = firstName+lastName;
        if (!personRepository.removePerson(key)) {
            throw new NotFoundException("Person not fount", ErrorEnum.API_ERROR_PERSON_NOT_FOUND);
        }
    }
}