package com.mills.controllers;

import com.mills.models.Event;
import com.mills.repositories.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/events")
public class EventsController {

    @Autowired
    private EventRepository eventRepository;

    @RequestMapping
    public Iterable<Event> events() {
        return eventRepository.findAll();
    }

}
