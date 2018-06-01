# Person Service (REST API)


## Requirements

* Java 8 or Above
* Maven
* GIT

## Installation
```
  > git pull git@github.com:ripsamm004/embl-person-service.git
  > cd embl-person-service
  > mvn clean install

```

## Run the server
```
 > java -jar target/person-service-0.0.1-SNAPSHOT.jar
```

## Rest person api server running on port [8081]

```
 > http://localhost:8081

```

## Service details

```

# Person service endpoint : /person


* Add a person

# Sample request

URL : http://localhost:8081/person
Request-Type: POST
Content-Type: application/json
Request Body :
{
  "first_name":"Adam",
  "last_name":"Smith",
  "age":"30",
  "favourite_colour": "Blue"
}

# Sample response

Response code : 201
Response content-type : application/json
Response Body:
{
    "person": [
        {
            "first_name": "Adam",
            "last_name": "Smith",
            "age": "30",
            "favourite_colour": "blue"
        }
    ]
}


* Get a person by first_name and last_name

> http://localhost:8081/person/{first_name}/{last_name}

# Sample request

Request-Type : GET
URL : http://localhost:8081/person/Adam/Smith

# Sample response

Response code : 200
Response content-type : application/json
Response Body:
{
    "person": [
        {
            "first_name": "Adam",
            "last_name": "Smith",
            "age": "30",
            "favourite_colour": "blue"
        }
    ]
}


* Get all person list

> http://localhost:8081/person

# Sample request


Request-Type : GET
URL : http://localhost:8081/person

# Sample response

Response code : 200
Response content-type : application/json
Response Body:
{
     "person": [
            {
               "first_name": "Adam",
               "last_name": "Smith",
               "age": "30",
               "favourite_colour": "blue"
            },
            {
                "first_name": "John",
                "last_name": "Keynes",
                "age": "29",
                "favourite_colour": "red"
            }
        ]
}

* Update a person by first_name and last_name

> http://localhost:8081/person/{first_name}/{last_name}

# Sample request

Request-Type : PUT
URL : http://localhost:8081/person/Adam/Smith
Content-Type: application/json
Request Body :
{
  "first_name":"Adam",
  "last_name":"Smith",
  "age":"89",
  "favourite_colour":"red"
}

# Sample response

Response code : 200
Response content-type : application/json
Response Body:
{
    "person": [
        {
            "first_name": "Adam",
            "last_name": "Smith",
            "age": "89",
            "favourite_colour": "red"
        }
    ]
}

* Remove a person by first_name and last_name

> http://localhost:8081/person/{first_name}/{last_name}

# Sample request

Request-Type : DELETE
URL : http://localhost:8081/person/Adam/Smith

# Sample response

Response code : 200


* Sample Error response

Content-Type : application/json

Response Body 01 :
{
    "code": "M0001",
    "message": "WE ARE EXPERIENCING SOME ISSUES, PLEASE TRY LATER"
}


Response Body 02:
{
    "code": "M0002",
    "message": "NO FOUND EXCEPTION"
}


Response Body 03:
{
    "code": "M0003",
    "message": "BAD REQUEST EXCEPTION"
}

Response Body 04:
{
    "code": "M0004",
    "message": "FORBIDDEN EXCEPTION"
}

Response Body 05:

{
    "code": "A0001",
    "message": "PERSON ALREADY EXIST"
}


Response Body 06:

{
    "code": "A0002",
    "message": "PERSON NOT FOUND"
}

Response Body 07:

{
    "code": "A0003",
    "message": "PERSON NAME NOT CORRECT"
}

Response Body 08:

{
    "code": "A0004",
    "message": "PERSON NAME NOT MATCH WITH REQUEST BODY"
}

Response Body 09:

{
    "code": "A0005",
    "message": "AGE FORMAT NOT CORRECT"
}

```