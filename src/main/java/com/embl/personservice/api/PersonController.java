package com.embl.personservice.api;

import com.embl.personservice.api.exception.BadRequestException;
import com.embl.personservice.domain.Person;
import com.embl.personservice.service.PersonService;
import com.embl.personservice.service.Validator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.logging.Level;
import java.util.logging.Logger;


@RestController
@RequestMapping("/person")
public class PersonController {

    private static final Logger logger = Logger.getLogger(PersonController.class.getName());

    @Inject
    protected Validator validator;

    @Inject
    protected PersonService personService;


    @GetMapping()
    public ResponseEntity<PersonDTO> showAll() {
        logger.log(Level.INFO, "GET : SHOW ALL PERSON");
        PersonDTO personDTO = new PersonDTO(personService.getAllPerson());
        return new ResponseEntity(personDTO, HttpStatus.OK);
    }


    @GetMapping("/{firstName}/{lastName}")
    public ResponseEntity<PersonDTO> getByName(@PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName ) {
        logger.log(Level.INFO, "GET : FIND PERSON BY FIRST NAME AND LAST NAME");
        PersonDTO personDTO = new PersonDTO(personService.getPerson(firstName, lastName));
        return new ResponseEntity(personDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PersonDTO> create(@RequestBody @NotNull Person person) {
        logger.log(Level.INFO, "POST PERSON DATA : " + person);
        validator.validate(person);
        PersonDTO personDTO = new PersonDTO(personService.addPerson(person));
        return new ResponseEntity(personDTO, HttpStatus.CREATED);
    }


    @PutMapping("/{firstName}/{lastName}")
    public ResponseEntity<PersonDTO> replace(@RequestBody Person person, @PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName ) {
        logger.log(Level.INFO, "PUT : UPDATE PERSON : " + person);
        if(StringUtils.isEmpty(firstName)
                || StringUtils.isEmpty(lastName)
                || !person.getFirst_name().equals(firstName)
                || !person.getLast_name().equals(lastName)) {

            throw new BadRequestException("Person name not match", ErrorEnum.API_ERROR_PERSON_NAME_NOT_MATCH);
        }

        validator.validate(person);
        PersonDTO personDTO = new PersonDTO(personService.replacePerson(person));
        return new ResponseEntity(personDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{firstName}/{lastName}")
    public ResponseEntity<Object> delete(@PathVariable @NotNull String firstName, @PathVariable @NotNull String lastName) {
        logger.log(Level.INFO, "DELETE : PERSON");
        personService.removePerson(firstName,lastName);
        return new ResponseEntity(null, HttpStatus.OK);
    }

}