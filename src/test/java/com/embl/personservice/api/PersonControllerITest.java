package com.embl.personservice.api;

import com.embl.personservice.ApplicationMain;
import com.embl.personservice.domain.Person;
import com.embl.personservice.service.PersonService;
import com.embl.personservice.service.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(
        webEnvironment = WebEnvironment.RANDOM_PORT,
        classes = ApplicationMain.class)
public class PersonControllerITest {

    String apiEndPoint = "/person";

    @Autowired
    private PersonService personService;

    @Autowired
    private Validator validator;

    @Autowired
    private MockMvc mvc;


    @Test
    public void testWhenPersonNotExistThenReturnEmptyJsonArray()
            throws Exception {

        mvc.perform(get(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(greaterThan(0))))
                .andReturn();

    }

    @Test
    public void testWhenPersonExistThenReturnJsonArrayOfPerson()
            throws Exception {

        Person p0 = new Person("Json", "Keynes", "29", "red");
        Person p1 = new Person("Adam", "Smith", "40", "blue");

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("Json")))
                .andReturn();

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p1)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("Adam")))
                .andReturn();

        mvc.perform(get(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(greaterThan(0))))
                .andReturn();

    }


    @Test
    public void testGivenFirstNameAndLastNameWhenPersonExistThenReturnJsonArrayOfPerson()
            throws Exception {

        Person p0 = new Person("Jackob", "Keynes", "29", "red");
        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("Jackob")))
                .andReturn();

        mvc.perform(get(apiEndPoint + "/" + p0.getFirst_name() + "/" + p0.getLast_name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("Jackob")))
                .andReturn();
    }


    @Test
    public void testGivenFirstNameAndLastNameWhenPersonNotExistThenResponseJsonError()
            throws Exception {

        Person p0 = new Person("Julian", "Keynes", "29", "red");

        mvc.perform(get(apiEndPoint + "/" + p0.getFirst_name() + "/" + p0.getLast_name())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_NOT_FOUND.getCode())))
                .andReturn();
    }

    @Test
    public void testGivenRequestBodyWithInValidNameWhenAddPersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John3", "Keynes", "29", "red");

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_NAME_NOT_CORRECT.getCode())))
                .andReturn();

    }

    @Test
    public void testGivenRequestBodyWithInValidPersonAgeWhenAddPersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keynes", "29T", "red");

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_AGE_CORRECT.getCode())))
                .andReturn();
    }

    @Test
    public void testGivenRequestBodyWithInValidDataWhenAddPersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("", "Keynes", "29", "red");

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_INVALID_REQUEST_BODY.getCode())))
                .andReturn();

    }

    @Test
    public void testGivenRequestBodyWithInValidJsonDataWhenAddPersonThenResponseJsonError() throws Exception {

        String requestBodyPayLoad = "";

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBodyPayLoad))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_REQUEST_BODY_FORMAT_INVALID.getCode())))
                .andReturn();
    }

    @Test
    public void testGivenValidRequestBodyWhenAddPersonThenResponseJsonArrayOfPerson() throws Exception {

        Person p0 = new Person("James", "Keynes", "29", "red");

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("James")))
                .andReturn();
    }

    @Test
    public void testGivenValidRequestBodyIfPersonAlreadyExistWhenAddPersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keynes", "29", "red");
        Person p1 = new Person("John", "Keynes", "29", "red");

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("John")))
                .andReturn();

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p1)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_ALREADY_EXIST.getCode())))
                .andReturn();
    }

    @Test
    public void testIfPersonNotExistWhenUpdatePersonThenResponseJsonError() throws Exception {

        Person p0 = new Person("John", "Keytes", "29", "red");
        String first_name = "John";
        String last_name = "Keytes";

        mvc.perform(put(apiEndPoint + "/" + first_name + "/" + last_name)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_NOT_FOUND.getCode())))
                .andReturn();

    }

    @Test
    public void testIfPersonNameMatchWhenUpdatePersonThenResponseJsonArrayOfPerson() throws Exception {

        Person p0 = new Person("Jacson", "Keynes", "29", "red");
        Person p1 = new Person("Jacson", "Keynes", "40", "red");
        String first_name = "Jacson";
        String last_name = "Keynes";

        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("Jacson")))
                .andReturn();

        mvc.perform(put(apiEndPoint + "/" + first_name + "/" + last_name)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p1)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("Jacson")))
                .andExpect(jsonPath("$.person[0].age", is("40")))
                .andReturn();

    }


    @Test
    public void testIfPersonNotExistWhenDeletePersonThenResponseJsonError() throws Exception {

        String first_name = "John";
        String last_name = "Keytes";

        mvc.perform(delete(apiEndPoint + "/" + first_name + "/" + last_name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(ErrorEnum.API_ERROR_PERSON_NOT_FOUND.getCode())))
                .andReturn();
    }


    @Test
    public void testIfPersonExistWhenDeletePersonThenResponseOK() throws Exception {

        Person p0 = new Person("John", "Keytes", "29", "red");
        String first_name = "John";
        String last_name = "Keytes";


        mvc.perform(post(apiEndPoint)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(p0)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.person", hasSize(1)))
                .andExpect(jsonPath("$.person[0].first_name", is("John")))
                .andReturn();

        mvc.perform(delete(apiEndPoint + "/" + first_name + "/" + last_name)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

    }


    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}