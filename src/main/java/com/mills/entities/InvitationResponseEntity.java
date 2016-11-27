package com.mills.entities;

import com.mills.enums.ResponseEnum;
import com.mills.models.InvitedRelationship;

/**
 * Created by ryan on 27/11/16.
 */
public class InvitationResponseEntity {

    private ResponseEnum response;

    public InvitationResponseEntity() {}

    public InvitationResponseEntity(InvitedRelationship invitation) {
        response = invitation.getResponse();
    }

    public ResponseEnum getResponse() {
        return response;
    }

    public void setResponse(ResponseEnum response) {
        this.response = response;
    }
}
