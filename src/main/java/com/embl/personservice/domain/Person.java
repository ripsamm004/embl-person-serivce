package com.embl.personservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


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
@lombok.EqualsAndHashCode(of = {"first_name", "last_name"})
@Getter
@ToString
public class Person {
    private String first_name;
    private String last_name;
    private String age;
    private String favourite_colour;
}