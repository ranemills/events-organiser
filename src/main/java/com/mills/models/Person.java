package com.mills.models;

import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.util.ArrayList;
import java.util.List;

@NodeEntity
public class Person {
    @GraphId
    private Long id;

    private String name;
    @Relationship(type = "INVITED")
    private List<InvitedRelationship> invitations = new ArrayList<>();

    private Person() {
    }

    public Person(String name) {
        setName(name);
    }

    public List<InvitedRelationship> getInvitations() {
        return invitations;
    }

    public void setInvitations(List<InvitedRelationship> invitations) {
        this.invitations = invitations;
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
}
