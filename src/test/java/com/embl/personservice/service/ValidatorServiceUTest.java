package com.embl.personservice.service;


import com.embl.personservice.api.exception.BadRequestException;
import com.embl.personservice.domain.Person;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
public class ValidatorServiceUTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Validator validator;

    @Before
    public void setup(){
        validator = new Validator();
    }

    @Test
    public void testWhenProductHasInvalidNameThenExpectFalse() {
        Person p0 = new Person("John9", "Keynes", "29", "red");
        assertThat(validator.validateName(p0.getFirst_name()), is(false));
    }

    @Test
    public void testWhenProductHasInvalidAgeThenExpectFalse() {
        Person p0 = new Person("John", "Keynes", "29T", "red");
        assertThat(validator.validateAge(p0.getAge()), is(false));
    }

    @Test
    public void testWhenProductInvalidNameThenExpectTrue() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        assertThat(validator.validateName(p0.getFirst_name()), is(true));
    }

    @Test
    public void testWhenProductHasAgeThenExpectTrue() {
        Person p0 = new Person("John", "Keynes", "29", "red");
        assertThat(validator.validateAge(p0.getAge()), is(true));
    }

    @Test
    public void testWhenProductIsValidThanNoException() throws Exception {
        Person p0 = new Person("John", "Keynes", "29", "red");
        validator.validate(p0);
    }

    @Test
    public void testWhenProductHasInvalidFirstNameThrowException() throws Exception {
        Person p0 = new Person("John90", "Keynes", "29", "red");
        thrown.expect(BadRequestException.class);
        thrown.expectMessage(equalTo("Person name"));
        validator.validate(p0);
    }

    @Test
    public void testWhenProductHasInvalidLastNameThrowException() throws Exception {
        Person p0 = new Person("John", "Keynes90", "29", "red");
        thrown.expect(BadRequestException.class);
        thrown.expectMessage(equalTo("Person name"));
        validator.validate(p0);
    }

    @Test
    public void testWhenProductHasInvalidAgeThrowException() throws Exception {
        Person p0 = new Person("John", "Keynes", "29T", "red");
        thrown.expect(BadRequestException.class);
        thrown.expectMessage(equalTo("Person age"));
        validator.validate(p0);
    }


    @Test
    public void testWhenProductInvalidWithNullFieldThrowException() throws Exception {
        Person p0 = new Person(null, "Keynes", "29T", "red");
        thrown.expect(BadRequestException.class);
        thrown.expectMessage(equalTo("DATA INVALID"));
        validator.validate(p0);
    }

    @Test
    public void testWhenProductInvalidWithEmptyValuesThrowException() throws Exception {
        Person p0 = new Person("", "Keynes", "", "red");
        thrown.expect(BadRequestException.class);
        thrown.expectMessage(equalTo("DATA INVALID"));
        validator.validate(p0);
    }

}
