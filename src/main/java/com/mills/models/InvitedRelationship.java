package com.mills.models;

import com.mills.enums.ResponseEnum;
import org.apache.commons.lang3.builder.ToStringBuilder;
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
    private ResponseEnum response;

    private InvitedRelationship() {}

    public InvitedRelationship(Event event, Person person)
    {
        this.event = event;
        this.person = person;
        this.response = ResponseEnum.NO_RESPONSE;
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

    public ResponseEnum getResponse() {
        return response;
    }

    public InvitedRelationship setResponse(ResponseEnum response) {
        this.response = response;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                   .append("relationshipId", relationshipId)
                   .append("event", event)
                   .append("person", person)
                   .append("response", response)
                   .toString();
    }
}
