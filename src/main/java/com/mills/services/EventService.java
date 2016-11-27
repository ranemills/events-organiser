package com.mills.services;

import com.mills.entities.InvitationResponseEntity;
import com.mills.models.InvitedRelationship;
import com.mills.repositories.EventRepository;
import com.mills.repositories.InvitationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ryan on 27/11/16.
 */
@Service
public class EventService {

    private final InvitationRepository invitationRepository;

    @Autowired
    public EventService(InvitationRepository invitationRepository) {
        this.invitationRepository = invitationRepository;
    }

    public InvitationResponseEntity getEventResponseEntity(Long eventId, Long personId) {
        InvitedRelationship invitation = getEventResponse(eventId, personId);
        InvitationResponseEntity responseEntity = new InvitationResponseEntity();
        responseEntity.setResponse(invitation.getResponse());
        return responseEntity;
    }

    public InvitedRelationship getEventResponse(Long eventId, Long personId) {
        return invitationRepository.getResponse(eventId, personId);
    }

}
