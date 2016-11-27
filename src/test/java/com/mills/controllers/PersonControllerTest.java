package com.mills.controllers;

import com.mills.enums.ResponseEnum;
import com.mills.entities.PersonEntity;
import com.mills.models.Event;
import com.mills.models.InvitedRelationship;
import com.mills.models.Person;
import org.joda.time.DateTime;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PersonControllerTest extends AbstractControllerTest {

    @Test
    public void canRetrievePersons()
        throws Exception
    {
        Person person1 = new Person("name1");
        Person person2 = new Person("name2");

        personRepository.save(Arrays.asList(person1, person2));

        PersonEntity personEntity1 = new PersonEntity().setId(person1.getId()).setName("name1");
        PersonEntity personEntity2 = new PersonEntity().setId(person2.getId()).setName("name2");

        mockMvc.perform(get("/api/people"))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(Arrays.asList(personEntity1, personEntity2)), true));
    }

    @Test
    public void canRetrieveEmptyList()
        throws Exception
    {
        mockMvc.perform(get("/api/people"))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(new ArrayList<>()), true));
    }

    @Test
    public void canRetrievePersonWithInvitedPeople()
        throws Exception
    {
        Event event = new Event("event", DateTime.now().plusDays(7));
        Person person = new Person("person");
        InvitedRelationship relationship = new InvitedRelationship(event, person);
        relationship.setResponse(ResponseEnum.YES);
        person.setInvitations(Collections.singletonList(relationship));

        personRepository.save(person);

        PersonEntity expected = new PersonEntity().setId(person.getId())
                                                  .setName("person")
                                                  .addInvitation(new PersonEntity.InvitationEntity(event.getId(), "event", ResponseEnum.YES));

        mockMvc.perform(get("/api/people"))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(Collections.singletonList(expected)), true));
    }

    @Test
    public void canRetrieveSinglePerson()
        throws Exception
    {
        Person person = new Person("person");
        personRepository.save(person);

        PersonEntity expected = new PersonEntity().setId(person.getId()).setName("person");

        mockMvc.perform(get(String.format("/api/people/%s", person.getId())))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(expected), true));
    }

}
