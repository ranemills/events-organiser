package com.mills.models;

import org.neo4j.ogm.annotation.*;

/**
 * Created by ryanmills on 21/10/2016.
 */
@RelationshipEntity(type = "INVITED")
public class InvitedRelationship {
    @GraphId
    private Long relationshipId;

    @StartNode
    private Event event;

    @EndNode
    private Person person;

    @Property
    private String response;

    public InvitedRelationship(Event event, Person person)
    {
        this.event = event;
        this.person = person;
        this.response = "none";
    }

    public Event getEvent() {
        return event;
    }

    public InvitedRelationship setEvent(Event event) {
        this.event = event;
        return this;
    }

    public Person getPerson() {
        return person;
    }

    public InvitedRelationship setPerson(Person person) {
        this.person = person;
        return this;
    }

    public String getResponse() {
        return response;
    }

    public InvitedRelationship setResponse(String response) {
        this.response = response;
        return this;
    }
}
