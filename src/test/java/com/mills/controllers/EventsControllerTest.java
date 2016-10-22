package com.mills.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mills.entities.EventEntity;
import com.mills.models.Event;
import com.mills.models.InvitedRelationship;
import com.mills.models.Person;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.coyote.http11.Constants.a;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class EventsControllerTest extends AbstractControllerTest {

    @Test
    public void canRetrieveEvents()
        throws Exception
    {
        Event event1 = new Event("name1");
        Event event2 = new Event("name2");

        eventRepository.save(Arrays.asList(event1, event2));

        EventEntity eventEntity1 = new EventEntity().setName("name1");
        EventEntity eventEntity2 = new EventEntity().setName("name2");

        mockMvc.perform(get("/api/events"))
               .andExpect(status().isOk())
                .andExpect(content().json(asJson(Arrays.asList(eventEntity1, eventEntity2)), true));
    }

    @Test
    public void canRetrieveEmptyList()
        throws Exception
    {
        mockMvc.perform(get("/api/events"))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(new ArrayList<>()), true));
    }

    @Test
    public void canRetrieveEventWithInvitedPeople()
        throws Exception
    {
        Event event = new Event("event");
        Person person = new Person("person");
        InvitedRelationship relationship = new InvitedRelationship(event, person);
        relationship.setResponse("Yes");
        event.setInvitations(Collections.singletonList(relationship));

        eventRepository.save(event);

        EventEntity expected = new EventEntity().setName("event")
            .addInvitation(new EventEntity.InvitationEntity("person", "Yes"));

        mockMvc.perform(get("/api/events"))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(Collections.singletonList(expected)), true));
    }

    @Test
    public void canRetrieveSingleEvent()
        throws Exception
    {
        Event event = new Event("event");
        eventRepository.save(event);

        EventEntity expected = new EventEntity().setName("event");

        mockMvc.perform(get(String.format("/api/events/%s", event.getId())))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(expected), true));
    }


    @Test
    public void canInvitePerson()
        throws Exception
    {
        Event event = new Event("event");
        eventRepository.save(event);
        Person person = new Person("person");
        personRepository.save(person);

        EventEntity expected = new EventEntity().setName("event")
            .addInvitation(new EventEntity.InvitationEntity("person", "none"));

        mockMvc.perform(post(String.format("/api/events/%s/invite?id=%s", event.getId(), person.getId())))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(expected), true));

        Event received = eventRepository.findOne(event.getId());
        assertThat(received.getInvitations(), hasSize(1));
        assertThat(received.getInvitations().get(0).getPerson(), equalTo(person));
    }

    private static String asJson(List<EventEntity> entityList)
        throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(entityList);
    }

    private static String asJson(EventEntity entity)
        throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(entity);
    }

}
