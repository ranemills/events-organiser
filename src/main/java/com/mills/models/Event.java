package com.mills.models;

import org.joda.time.DateTime;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NodeEntity
public class Event {
    @GraphId
    private Long id;

    private String name;
    private Date date;
    @Relationship(type = "INVITED")
    private List<InvitedRelationship> invitations = new ArrayList<>();

    private Event() {
    }

    public Event(String name, DateTime date) {
        setName(name);
        setDate(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public DateTime getDate() {
        return new DateTime(date);
    }

    public void setDate(DateTime date) {
        this.date = date.toDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addInvitation(InvitedRelationship invitation)
    {
        this.invitations.add(invitation);
    }

    public List<InvitedRelationship> getInvitations()
    {
        return invitations;
    }

    public void setInvitations(List<InvitedRelationship> invitations)
    {
        this.invitations = invitations;
    }
}
