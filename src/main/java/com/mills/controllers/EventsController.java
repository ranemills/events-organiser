package com.mills.controllers;

import com.mills.entities.EventEntity;
import com.mills.models.Event;
import com.mills.models.InvitedRelationship;
import com.mills.models.Person;
import com.mills.repositories.EventRepository;
import com.mills.repositories.PersonRepository;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private PersonRepository personRepository;

    @RequestMapping(method = RequestMethod.GET)
    public List<EventEntity> events() {
        List<EventEntity> response = new ArrayList<>();
        for(Event event : eventRepository.findAll())
        {
            response.add(EventEntity.fromEvent(event));
        }
        return response;
    }

    @RequestMapping(method = RequestMethod.POST)
    public EventEntity createEvent(@RequestBody Map<String, String> requestParams) {
        Validate.notNull(requestParams.get("name"));

        Event event = new Event(requestParams.get("name"), DateTime.now().plusDays(7));
        eventRepository.save(event);

        return EventEntity.fromEvent(event);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public EventEntity event(@PathVariable("id") Long eventId) {
        Event event = eventRepository.findOne(eventId);
        return EventEntity.fromEvent(event);
    }

    @RequestMapping(value = "/{id}/invite", method = RequestMethod.POST)
    public EventEntity event(@PathVariable("id") Long eventId, @RequestParam("id") Long personId) {
        Event event = eventRepository.findOne(eventId);
        Person person = personRepository.findOne(personId);
        InvitedRelationship invitation = new InvitedRelationship(event, person);
        event.addInvitation(invitation);

        event = eventRepository.save(event);

        return EventEntity.fromEvent(event);
    }

}
