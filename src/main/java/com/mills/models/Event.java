package com.mills.models;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class Event {
    @GraphId
    private Long id;

    private String name;

    @Relationship(type = "INVITED")
    private List<InvitedRelationship> invitations = new ArrayList<>();

    private Event() {}

    public Event(String name) {
        setName(name);
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

    public void setInvitations(List<InvitedRelationship> invitations)
    {
        this.invitations = invitations;
    }

    public void addInvitation(InvitedRelationship invitation)
    {
        this.invitations.add(invitation);
    }

    public List<InvitedRelationship> getInvitations()
    {
        return invitations;
    }
}
