package com.embl.personservice.api;

import com.embl.personservice.api.exception.BadRequestException;
import com.embl.personservice.domain.Person;
import com.embl.personservice.service.PersonService;
import com.embl.personservice.service.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

@Slf4j
@RestController
@RequestMapping("/person")
public class PersonController {
    
    @Inject
    protected Validator validator;

    @Inject
    protected PersonService personService;

    @GetMapping()
    public ResponseEntity<PersonDTO> showAll() {
        log.info("GET : SHOW ALL PERSON");
        PersonDTO personDTO = new PersonDTO(personService.getAllPerson());
        return new ResponseEntity(personDTO, HttpStatus.OK);
    }


    @GetMapping("/{firstName}/{lastName}")
    public ResponseEntity<PersonDTO> getByName(@PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName ) {
        log.info("GET : FIND PERSON BY FIRST NAME AND LAST NAME");
        PersonDTO personDTO = new PersonDTO(personService.getPerson(firstName, lastName));
        return new ResponseEntity(personDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PersonDTO> create(@RequestBody @NotNull Person person) {
        log.info("POST PERSON DATA : " + person);
        validator.validate(person);
        PersonDTO personDTO = new PersonDTO(personService.addPerson(person));
        return new ResponseEntity(personDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<PersonDTO> replace(@RequestBody Person person, @PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName ) {
        log.info("PUT : UPDATE PERSON : " + person);

        validator.validate(person);

        if(!person.getFirst_name().equals(firstName)
                || !person.getLast_name().equals(lastName)) {
            throw new BadRequestException("Person name not match", ErrorEnum.API_ERROR_PERSON_NAME_NOT_MATCH);
        }

        PersonDTO personDTO = new PersonDTO(personService.replacePerson(person));
        return new ResponseEntity(personDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Object> delete(@PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName) {
        log.info("DELETE : PERSON");
        personService.removePerson(firstName,lastName);
        return new ResponseEntity(null, HttpStatus.OK);
    }

}