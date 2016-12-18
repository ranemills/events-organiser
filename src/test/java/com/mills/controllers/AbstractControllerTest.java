package com.mills.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mills.EventsApplication;
import com.mills.EventsNeo4jTestConfiguration;
import com.mills.repositories.EventRepository;
import com.mills.repositories.InvitationRepository;
import com.mills.repositories.PersonRepository;
import org.junit.Before;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {EventsApplication.class, EventsNeo4jTestConfiguration.class})
@ActiveProfiles(profiles = "test")
@WebAppConfiguration
@WithMockUser
public abstract class AbstractControllerTest {

    @Autowired
    protected WebApplicationContext _webApplicationContext;

    @Autowired
    EventRepository eventRepository;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    InvitationRepository invitationRepository;

    MockMvc mockMvc;

    static String asJson(Object object)
        throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }

    @Before
    public void setUpMockMvc()
    {
        mockMvc = MockMvcBuilders.webAppContextSetup(_webApplicationContext)
                                 .build();
        eventRepository.deleteAll();
        personRepository.deleteAll();
    }

}
