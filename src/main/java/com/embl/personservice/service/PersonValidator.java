package com.embl.personservice.service;

import com.embl.personservice.api.exception.BadRequestException;
import com.embl.personservice.api.ErrorEnum;
import com.embl.personservice.domain.Person;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PersonValidator
{

    private final String personNamePattern = "[A-Za-z]+";

    public boolean validateName(String name)
    {
        Pattern p = Pattern.compile(personNamePattern);
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public boolean validateAge(String inputAge)
    {
        if (inputAge.length() <= 3 && inputAge.length() >=1) {
            try {
                Integer i = Integer.valueOf(inputAge);
                return i <= 99 && i >= 0;
            }
            catch (NumberFormatException e) {
                return false;
            }
        }
        return false;
    }

    public void validate(Person person){
        if(!validateName(person.getFirst_name()) || !validateName(person.getLast_name())) throw new BadRequestException("Person Name", ErrorEnum.API_ERROR_PERSON_NAME_NOT_CORRECT);
        if(!validateAge(person.getAge())) throw new BadRequestException("Person age", ErrorEnum.API_ERROR_PERSON_AGE_CORRECT);
    }
}