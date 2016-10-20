package com.mills.controllers;

import com.mills.EventsApplication;
import com.mills.EventsNeo4jTestConfiguration;
import com.mills.models.Event;
import com.mills.repositories.EventRepository;
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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by ryan on 19/10/16.
 */
@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EventsApplication.class, EventsNeo4jTestConfiguration.class})
@ActiveProfiles(profiles = "test")
public class EventsControllerTest {

    @Autowired
    private WebApplicationContext _webApplicationContext;

    @Autowired
    private EventRepository eventRepository;

    private MockMvc mockMvc;

    @Before
    public void setUpMockMvc()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(_webApplicationContext)
                                 .build();
    }

    @Test
    @WithMockUser
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

}
