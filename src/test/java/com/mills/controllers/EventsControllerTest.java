package com.mills.controllers;

import com.mills.enums.ResponseEnum;
import com.mills.entities.EventEntity;
import com.mills.models.Event;
import com.mills.models.InvitedRelationship;
import com.mills.models.Person;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Before
    public void freezeTime() {
        DateTimeUtils.setCurrentMillisFixed(0);
    }

    @Test
    public void canRetrieveEvents()
        throws Exception
    {
        Event event1 = new Event("name1", DateTime.now().plusDays(7));
        Event event2 = new Event("name2", DateTime.now().plusDays(7));

        eventRepository.save(Arrays.asList(event1, event2));

        EventEntity eventEntity1 = new EventEntity().setId(event1.getId()).setName("name1").setDate(DateTime.now().plusDays(7));
        EventEntity eventEntity2 = new EventEntity().setId(event2.getId()).setName("name2").setDate(DateTime.now().plusDays(7));

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
        Event event = new Event("event", DateTime.now().plusDays(7));
        Person person = new Person("person");
        InvitedRelationship relationship = new InvitedRelationship(event, person);
        relationship.setResponse(ResponseEnum.YES);
        event.setInvitations(Collections.singletonList(relationship));

        eventRepository.save(event);

        EventEntity expected = new EventEntity().setId(event.getId())
                                                .setName("event")
                                                .setDate(DateTime.now().plusDays(7))
                                                .addInvitation(new EventEntity.InvitationEntity(person.getId(),
                                                                                                "person",
                                                                                                ResponseEnum.YES));

        mockMvc.perform(get("/api/events"))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(Collections.singletonList(expected)), true));
    }

    @Test
    public void canRetrieveSingleEvent()
        throws Exception
    {
        Event event = new Event("event", DateTime.now().plusDays(7));
        eventRepository.save(event);

        EventEntity expected = new EventEntity().setId(event.getId())
                                                .setDate(DateTime.now().plusDays(7))
                                                .setName("event");

        mockMvc.perform(get(String.format("/api/events/%s", event.getId())))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(expected), true));
    }

    @Test
    public void canInvitePerson()
        throws Exception
    {
        Event event = new Event("event", DateTime.now().plusDays(7));
        eventRepository.save(event);
        Person person = new Person("person");
        personRepository.save(person);

        EventEntity expected = new EventEntity().setId(event.getId())
                                                .setName("event")
                                                .setDate(DateTime.now().plusDays(7))
                                                .addInvitation(new EventEntity.InvitationEntity(person.getId(), "person", ResponseEnum.NO_RESPONSE));

        mockMvc.perform(post(String.format("/api/events/%s/invite?id=%s", event.getId(), person.getId())))
               .andExpect(status().isOk())
               .andExpect(content().json(asJson(expected), true));

        Event received = eventRepository.findOne(event.getId());
        assertThat(received.getInvitations(), hasSize(1));
        assertThat(received.getInvitations().get(0).getPerson(), equalTo(person));
    }

    @Test
    public void canCreateEvent()
        throws Exception
    {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "test");

        mockMvc.perform(post("/api/events").content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name", is("test")));

        Iterable<Event> received = eventRepository.findAll();
        List<Event> eventList = new ArrayList<>();

        for(Event event : received) {
            eventList.add(event);
        }

        assertThat(eventList, hasSize(1));
        assertThat(eventList.get(0).getName(), is("test"));
    }

    @Test
    public void canGetResponseObjectWithNonDefault()
        throws Exception
    {
        Event event  = new Event("testEvent", DateTime.now());
        Person person = new Person("testPerson");

        InvitedRelationship invitation = new InvitedRelationship(event, person);
        invitation.setResponse(ResponseEnum.MAYBE);

        invitationRepository.save(invitation);

        mockMvc.perform(get(String.format("/api/events/%s/%s", event.getId(), person.getId())))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.response", is("MAYBE")));
    }

    @Test
    public void canGetResponseObject()
        throws Exception
    {
        Event event  = new Event("testEvent", DateTime.now());
        Person person = new Person("testPerson");

        InvitedRelationship invitation = new InvitedRelationship(event, person);

        invitationRepository.save(invitation);

        mockMvc.perform(get(String.format("/api/events/%s/%s", event.getId(), person.getId())))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.response", is("NO_RESPONSE")));
    }

}
