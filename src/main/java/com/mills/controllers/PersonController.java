package com.mills.controllers;

import com.mills.entities.PersonEntity;
import com.mills.models.Person;
import com.mills.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryan on 22/10/16.
 */
@RestController
@RequestMapping("/api/people")
public class PersonController {

    private final PersonRepository personRepository;

    @Autowired
    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @RequestMapping
    public List<PersonEntity> people() {
        List<PersonEntity> response = new ArrayList<>();
        for (Person person : personRepository.findAll()) {
            response.add(PersonEntity.fromPerson(person));
        }
        return response;
    }

    @RequestMapping("/{id}")
    public PersonEntity person(@PathVariable("id") Long eventId) {
        Person event = personRepository.findOne(eventId);
        return PersonEntity.fromPerson(event);
    }

}
