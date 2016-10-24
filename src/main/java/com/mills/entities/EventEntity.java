package com.mills.entities;

import com.mills.enums.ResponseEnum;
import com.mills.models.Event;
import com.mills.models.InvitedRelationship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanmills on 21/10/2016.
 */
public class EventEntity {

    private Long id;
    private String name;
    private List<InvitationEntity> invitations = new ArrayList<>();

    public EventEntity() {

    }

    public static EventEntity fromEvent(Event event) {
        EventEntity entity = new EventEntity();
        entity.setId(event.getId());
        entity.setName(event.getName());

        for (InvitedRelationship relationship : event.getInvitations()) {
            InvitationEntity invitationEntity = new InvitationEntity(relationship.getPerson().getId(),
                    relationship.getPerson().getName(),
                    relationship.getResponse());
            entity.addInvitation(invitationEntity);
        }

        return entity;
    }

    public Long getId() {
        return id;
    }

    public EventEntity setId(Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public EventEntity setName(String name) {
        this.name = name;
        return this;
    }

    public List<InvitationEntity> getInvitations() {
        return invitations;
    }

    public EventEntity setInvitations(List<InvitationEntity> invitations) {
        this.invitations = invitations;
        return this;
    }

    public EventEntity addInvitation(InvitationEntity invitation) {
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
