package com.mills.entities;

import com.mills.models.Event;
import com.mills.models.InvitedRelationship;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ryanmills on 21/10/2016.
 */
public class EventEntity {

    private String name;
    private List<InvitationEntity> invitations = new ArrayList<>();

    public EventEntity() {

    }

    public static EventEntity fromEvent(Event event) {
        EventEntity entity = new EventEntity();
        entity.setName(event.getName());

        for (InvitedRelationship relationship : event.getInvitations()) {
            InvitationEntity invitationEntity = new InvitationEntity(relationship.getPerson().getName(), relationship.getResponse());
            entity.addInvitation(invitationEntity);
        }

        return entity;
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
