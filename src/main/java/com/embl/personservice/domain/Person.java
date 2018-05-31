package com.embl.personservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 *  This class represent the person. person can have a
 *  "first_name"
 *  "last_name"
 *  "age"
 *  "favourite_colour"
 *  a person is uniquely identify if the first_name and last_name is same.
 */

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Person {
    private String first_name;
    private String last_name;
    private String age;
    private String favourite_colour;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (first_name != null ? !first_name.equals(person.first_name) : person.first_name != null) return false;
        return !(last_name != null ? !last_name.equals(person.last_name) : person.last_name != null);

    }

    @Override
    public int hashCode() {
        int result = first_name != null ? first_name.hashCode() : 0;
        result = 31 * result + (last_name != null ? last_name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "first_name='" + first_name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", age='" + age + '\'' +
                ", favourite_colour='" + favourite_colour + '\'' +
                '}';
    }
}