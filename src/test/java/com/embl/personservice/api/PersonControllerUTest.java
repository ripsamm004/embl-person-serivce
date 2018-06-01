package com.embl.personservice.api;

import com.embl.personservice.ApplicationMain;
import com.embl.personservice.domain.Person;
import com.embl.personservice.service.PersonService;
import com.embl.personservice.service.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    public void testGivenPersonFirstNameAndLastNameWhenPersonExistThenResponseJsonArrayOfPerson()
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
                .andReturn();

        verify(personService, times(1)).getAllPerson();
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