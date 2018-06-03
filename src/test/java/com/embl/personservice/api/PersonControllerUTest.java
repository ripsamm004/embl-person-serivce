package com.embl.personservice.api;

import com.embl.personservice.ApplicationMain;
import com.embl.personservice.api.exception.BadRequestException;
import com.embl.personservice.api.exception.ForbiddenException;
import com.embl.personservice.api.exception.NotFoundException;
import com.embl.personservice.api.exception.ServerException;
import com.embl.personservice.domain.Person;
import com.embl.personservice.exception.ValidatorPersonAlreadyExistException;
import com.embl.personservice.service.PersonService;
import com.embl.personservice.service.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ApplicationMain.class)
public class PersonControllerUTest {

    @MockBean
    private PersonService personService;

    @MockBean
    private Validator validator;

    @Autowired
    private MockMvc mvc;


    String apiEndPoint = "/person";


    @Test
    public void testWhenPersonExistThenReturnJsonArrayOfPerson()
            throws Exception {

        Person p0 = new Person("John", "Keynes", "29", "red");
        Person p1 = new Person("Adam", "Smith", "40", "blue");
        List<Person> personList = new ArrayList<>();
        personList.add(p0);
        personList.add(p1);

        when(personService.getAllPerson()).thenReturn(personList);

        mvc.perform(get(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(2)))
                .andExpect(jsonPath("$.person[*].first_name", containsInAnyOrder("John", "Adam")))
                .andReturn();

        verify(personService, times(1)).getAllPerson();
    }


    @Test
    public void testWhenNoPersonExistThenReturnEmptyJsonArray()
            throws Exception {


        List<Person> personList = new ArrayList<>();

        when(personService.getAllPerson()).thenReturn(personList);

        mvc.perform(get(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(0)))
                .andReturn();

        verify(personService, times(1)).getAllPerson();
    }

    @Test
    public void testGivenFirstNameAndLastNameWhenPersonExistThenReturnJsonArrayOfPerson()
            throws Exception {

        Person p0 = new Person("John", "Keynes", "29", "red");

        when(personService.getPerson(p0.getFirst_name(), p0.getLast_name())).thenReturn(p0);

        mvc.perform(get(apiEndPoint + "/" + p0.getFirst_name() + "/" + p0.getLast_name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("John")))
                .andReturn();

        verify(personService, times(1)).getPerson(p0.getFirst_name(), p0.getLast_name());
    }

    @Test
    public void testGivenFirstNameAndLastNameWhenPersonNotExistThenResponseJsonError()
            throws Exception {

        Person p0 = new Person("John", "Keynes", "29", "red");

        doThrow(new NotFoundException("Person not found", ErrorEnum.API_ERROR_PERSON_NOT_FOUND)).when(personService).getPerson(p0.getFirst_name(), p0.getLast_name());

        mvc.perform(get(apiEndPoint + "/" + p0.getFirst_name() + "/" + p0.getLast_name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_NOT_FOUND.getCode())))
                .andReturn();

        verify(personService, times(1)).getPerson(p0.getFirst_name(), p0.getLast_name());
    }

    @Test
    public void testGivenRequestBodyWithInValidNameWhenAddPersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John3", "Keynes", "29", "red");

        doThrow(new BadRequestException("Person name", ErrorEnum.API_ERROR_PERSON_NAME_NOT_CORRECT)).when(validator).validate(p0);
        when(personService.addPerson(p0)).thenReturn(any());

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_NAME_NOT_CORRECT.getCode())))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(0)).addPerson(p0);
    }

    @Test
    public void testGivenRequestBodyWithInValidPersonAgeWhenAddPersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keynes", "29T", "red");

        doThrow(new BadRequestException("Person age", ErrorEnum.API_ERROR_PERSON_AGE_CORRECT)).when(validator).validate(p0);
        when(personService.addPerson(p0)).thenReturn(any());

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_AGE_CORRECT.getCode())))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(0)).addPerson(p0);
    }

    @Test
    public void testGivenRequestBodyWithInValidDataWhenAddPersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("", "Keynes", "29", "red");

        doThrow(new BadRequestException("DATA INVALID", ErrorEnum.API_ERROR_INVALID_REQUEST_BODY)).when(validator).validate(p0);
        when(personService.addPerson(p0)).thenReturn(any());

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_INVALID_REQUEST_BODY.getCode())))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(0)).addPerson(p0);
    }

    @Test
    public void testGivenRequestBodyWithInValidJsonDataWhenAddPersonThenResponseJsonError() throws Exception {

        String requestBodyPayLoad = "";
        Person p0 = new Person("", "Keynes", "29", "red");

        doNothing().when(validator).validate(p0);
        when(personService.addPerson(p0)).thenReturn(p0);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyPayLoad))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_REQUEST_BODY_FORMAT_INVALID.getCode())))
                .andReturn();

        verify(validator, times(0)).validate(p0);
        verify(personService, times(0)).addPerson(p0);
    }


    @Test
    public void testGivenValidRequestBodyWhenAddPersonThenResponseJsonArrayOfPerson() throws Exception {

        Person p0 = new Person("John", "Keynes", "29", "red");

        doNothing().when(validator).validate(p0);
        when(personService.addPerson(p0)).thenReturn(p0);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("John")))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(1)).addPerson(p0);
    }


    @Test
    public void testGivenValidRequestBodyIfPersonAlreadyExistWhenAddPersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keynes", "29", "red");

        doNothing().when(validator).validate(p0);
        doThrow(new ValidatorPersonAlreadyExistException("Person exist", ErrorEnum.API_ERROR_PERSON_ALREADY_EXIST)).when(personService).addPerson(p0);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_ALREADY_EXIST.getCode())))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(1)).addPerson(p0);
    }

    @Test
    public void testGivenValidRequestBodyWhenForbiddenExceptionThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keynes", "29", "red");

        doNothing().when(validator).validate(p0);
        doThrow(new ForbiddenException("TEST FORBIDDEN", ErrorEnum.FORBIDDEN_EXCEPTION)).when(personService).addPerson(p0);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isForbidden())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.FORBIDDEN_EXCEPTION.getCode())))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(1)).addPerson(p0);
    }

    @Test
    public void testGivenValidRequestBodyWhenServerExceptionThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keynes", "29", "red");

        doNothing().when(validator).validate(p0);
        doThrow(new ServerException("SERVER EXCEPTION TEST", ErrorEnum.SERVER_EXCEPTION)).when(personService).addPerson(p0);

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.SERVER_EXCEPTION.getCode())))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(1)).addPerson(p0);
    }

    @Test
    public void testIfPersonNameNotMatchWhenUpdatePersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keynes", "29T", "red");
        String first_name = "John";
        String last_name = "Keytes";

        doNothing().when(validator).validate(p0);
        when(personService.replacePerson(p0)).thenReturn(any());

        mvc.perform(put(apiEndPoint + "/" + first_name + "/" + last_name)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_NAME_NOT_MATCH.getCode())))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(0)).replacePerson(p0);
    }

    @Test
    public void testIfPersonNotExistWhenUpdatePersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keytes", "29", "red");
        String first_name = "John";
        String last_name = "Keytes";

        doNothing().when(validator).validate(p0);
        doThrow(new NotFoundException("Person not found", ErrorEnum.API_ERROR_PERSON_NOT_FOUND)).when(personService).replacePerson(p0);

        mvc.perform(put(apiEndPoint + "/" + first_name + "/" + last_name)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_NOT_FOUND.getCode())))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(1)).replacePerson(p0);
    }

    @Test
    public void testIfPersonNameMatchWhenUpdatePersonThenResponseJsonArrayOfPerson() throws Exception {

        Person p0 = new Person("John", "Keynes", "29", "red");
        String first_name = "John";
        String last_name = "Keynes";

        doNothing().when(validator).validate(p0);
        when(personService.replacePerson(p0)).thenReturn(p0);

        mvc.perform(put(apiEndPoint + "/" + first_name + "/" + last_name)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("John")))
                .andReturn();

        verify(validator, times(1)).validate(p0);
        verify(personService, times(1)).replacePerson(p0);
    }

    @Test
    public void testIfPersonNotExistWhenDeletePersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keytes", "29", "red");
        String first_name = "John";
        String last_name = "Keytes";

        doThrow(new NotFoundException("Person not found", ErrorEnum.API_ERROR_PERSON_NOT_FOUND)).when(personService).removePerson(first_name, last_name);

        mvc.perform(delete(apiEndPoint + "/" + first_name + "/" + last_name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_NOT_FOUND.getCode())))
                .andReturn();

        verify(personService, times(1)).removePerson(first_name, last_name);
    }


    @Test
    public void testIfPersonExistWhenDeletePersonThenResponseOK() throws Exception {

        Person p0 = new Person("John", "Keytes", "29", "red");
        String first_name = "John";
        String last_name = "Keytes";

        doNothing().when(personService).removePerson(first_name, last_name);

        mvc.perform(delete(apiEndPoint + "/" + first_name + "/" + last_name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        verify(personService, times(1)).removePerson(first_name, last_name);
    }


    private static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}