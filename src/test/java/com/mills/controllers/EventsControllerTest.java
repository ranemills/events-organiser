package com.mills.controllers;

import com.mills.EventsApplication;
import com.mills.EventsNeo4jTestConfiguration;
import com.mills.models.Event;
import com.mills.models.InvitedRelationship;
import com.mills.models.Person;
import com.mills.repositories.EventRepository;
import com.mills.repositories.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EventsApplication.class, EventsNeo4jTestConfiguration.class})
@ActiveProfiles(profiles = "test")
@WebAppConfiguration
@WithMockUser
public class EventsControllerTest {

    @Autowired
    private WebApplicationContext _webApplicationContext;

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PersonRepository personRepository;

    private MockMvc mockMvc;

    @Before
    public void setUpMockMvc()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(_webApplicationContext)
                                 .build();
        eventRepository.deleteAll();
        personRepository.deleteAll();
    }

    @Test
    public void canRetrieveEvents()
        throws Exception
    {
        Event event1 = new Event("name1");
        Event event2 = new Event("name2");

        eventRepository.save(Arrays.asList(event1, event2));

        mockMvc.perform(get("/api/events"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(2)))
               .andExpect(jsonPath("$[0].name", is("name1")))
               .andExpect(jsonPath("$[1].name", is("name2")));
    }

    @Test
    public void canRetrieveEmptyList()
        throws Exception
    {
        mockMvc.perform(get("/api/events"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$", hasSize(0)));
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

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("event")))
                .andExpect(jsonPath("$[0].invitations", hasSize(1)))
                .andExpect(jsonPath("$[0].invitations[0].name", is("person")))
                .andExpect(jsonPath("$[0].invitations[0].response", is("Yes")));
    }

    @Test
    public void canRetrieveSingleEvent()
        throws  Exception
    {
        Event event = new Event("event");
        eventRepository.save(event);

        mockMvc.perform(get(String.format("/api/events/%s", event.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("event")))
                .andExpect(jsonPath("$.invitations", hasSize(0)));
    }


    @Test
    public void canInvitePerson()
        throws Exception
    {
        Event event = new Event("event");
        eventRepository.save(event);
        Person person = new Person("person");
        personRepository.save(person);

        mockMvc.perform(post(String.format("/api/events/%s/invite?id=%s", event.getId(), person.getId())))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("event")))
        .andExpect(jsonPath("$.invitations", hasSize(1)))
        .andExpect(jsonPath("$.invitations[0].name", is("person")))
        .andExpect(jsonPath("$.invitations[0].response", is("none")));

        Event received = eventRepository.findOne(event.getId());
        assertThat(received.getInvitations(), hasSize(1));
        assertThat(received.getInvitations().get(0).getPerson(), equalTo(person));
    }

}
