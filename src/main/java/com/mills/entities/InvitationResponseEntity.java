package com.mills.entities;

import com.mills.enums.ResponseEnum;

/**
 * Created by ryan on 27/11/16.
 */
public class InvitationResponseEntity {

    private ResponseEnum response;

    public ResponseEnum getResponse() {
        return response;
    }

    public void setResponse(ResponseEnum response) {
        this.response = response;
    }
}
