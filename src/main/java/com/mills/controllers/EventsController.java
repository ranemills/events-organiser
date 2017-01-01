package com.mills.controllers;

import com.mills.entities.EventEntity;
import com.mills.entities.InvitationResponseEntity;
import com.mills.models.Event;
import com.mills.repositories.EventRepository;
import com.mills.services.InvitationService;
import org.apache.commons.lang3.Validate;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    private final EventRepository eventRepository;
    private final InvitationService invitationService;

    @Autowired
    public EventsController(EventRepository eventRepository, InvitationService invitationService) {
        this.eventRepository = eventRepository;
        this.invitationService = invitationService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<EventEntity> getEvents() {
        List<EventEntity> response = new ArrayList<>();
        for (Event event : eventRepository.findAll()) {
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

    @RequestMapping(value = "/{eventId}", method = RequestMethod.GET)
    public EventEntity getEvent(@PathVariable("eventId") Long eventId) {
        Event event = eventRepository.findOne(eventId);
        return EventEntity.fromEvent(event);
    }

    @RequestMapping(value = "/{eventId}", method = RequestMethod.DELETE)
    public void deleteEvent(@PathVariable("eventId") Long eventId) {
        Event event = eventRepository.findOne(eventId);
        eventRepository.delete(event);
    }

    @RequestMapping(value = "/{eventId}/{personId}", method = RequestMethod.PUT)
    public InvitationResponseEntity updateResponse(@PathVariable("eventId") Long eventId,
                                 @PathVariable("personId") Long personId,
                                 @RequestBody InvitationResponseEntity responseEntity) {
        return invitationService.updateOrCreateInvitation(eventId, personId, responseEntity);
    }

    @RequestMapping(value = "/{eventId}/{personId}", method = RequestMethod.GET)
    public InvitationResponseEntity getResponse(@PathVariable("eventId") Long eventId,
                                                @PathVariable("personId") Long personId) {
        return invitationService.getEventResponseEntity(eventId, personId);
    }


}
