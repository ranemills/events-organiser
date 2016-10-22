package com.mills.entities;

import com.mills.models.InvitedRelationship;
import com.mills.models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanmills on 21/10/2016.
 */
public class PersonEntity {

    private String name;
    private List<InvitationEntity> invitations = new ArrayList<>();

    public PersonEntity() {

    }

    public static PersonEntity fromPerson(Person person) {
        PersonEntity entity = new PersonEntity();
        entity.setName(person.getName());

        for (InvitedRelationship relationship : person.getInvitations()) {
            InvitationEntity invitationEntity = new InvitationEntity(relationship.getEvent().getName(),
                                                                     relationship.getResponse());
            entity.addInvitation(invitationEntity);
        }

        return entity;
    }

    public String getName() {
        return name;
    }

    public PersonEntity setName(String name) {
        this.name = name;
        return this;
    }

    public List<InvitationEntity> getInvitations() {
        return invitations;
    }

    public PersonEntity setInvitations(List<InvitationEntity> invitations) {
        this.invitations = invitations;
        return this;
    }

    public PersonEntity addInvitation(InvitationEntity invitation) {
        this.invitations.add(invitation);
        return this;
    }

    public static class InvitationEntity {
        private String name;
        private String response;

        public InvitationEntity(String name, String response) {
            setName(name);
            setResponse(response);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getResponse() {
            return response;
        }

        public void setResponse(String response) {
            this.response = response;
        }
    }

}
