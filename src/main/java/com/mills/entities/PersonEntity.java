package com.mills.entities;

import com.mills.ResponseEnum;
import com.mills.models.InvitedRelationship;
import com.mills.models.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanmills on 21/10/2016.
 */
public class PersonEntity {

    private Long id;
    private String name;
    private List<InvitationEntity> invitations = new ArrayList<>();
    public PersonEntity() {

    }

    public static PersonEntity fromPerson(Person person) {
        PersonEntity entity = new PersonEntity();
        entity.setId(person.getId());
        entity.setName(person.getName());

        for (InvitedRelationship relationship : person.getInvitations()) {
            InvitationEntity invitationEntity = new InvitationEntity(relationship.getEvent().getId(),
                    relationship.getEvent().getName(),
                    relationship.getResponse());
            entity.addInvitation(invitationEntity);
        }

        return entity;
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
        private Long id;
        private String name;
        private ResponseEnum response;
        public InvitationEntity(Long id, String name, ResponseEnum response) {
            setId(id);
            setName(name);
            setResponse(response);
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

        public ResponseEnum getResponse() {
            return response;
        }

        public void setResponse(ResponseEnum response) {
            this.response = response;
        }
    }

}
