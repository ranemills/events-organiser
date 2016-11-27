package com.mills.services;

import com.mills.entities.InvitationResponseEntity;
import com.mills.models.Event;
import com.mills.models.InvitedRelationship;
import com.mills.models.Person;
import com.mills.repositories.EventRepository;
import com.mills.repositories.InvitationRepository;
import com.mills.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ryan on 27/11/16.
 */
@Service
public class InvitationService {

    private final InvitationRepository invitationRepository;
    private final EventRepository eventRepository;
    private final PersonRepository personRepository;

    @Autowired
    public InvitationService(EventRepository eventRepository, InvitationRepository invitationRepository, PersonRepository personRepository) {
        this.eventRepository = eventRepository;
        this.invitationRepository = invitationRepository;
        this.personRepository = personRepository;
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

    public InvitationResponseEntity updateInvitation(Long eventId, Long personId, InvitationResponseEntity entity) {
        InvitedRelationship invitation = getEventResponse(eventId, personId);

        if(invitation == null) {
            invitation = createInvitation(eventId, personId);
        }

        invitation.setResponse(entity.getResponse());

        return saveInvitation(invitation);
    }

    private InvitedRelationship createInvitation(Long eventId, Long personId) {
        Event event = eventRepository.findOne(eventId);
        Person person = personRepository.findOne(personId);
        return new InvitedRelationship(event, person);
    }

    private InvitationResponseEntity saveInvitation(InvitedRelationship invitation) {
        invitationRepository.save(invitation);
        return new InvitationResponseEntity(invitation);
    }

}
