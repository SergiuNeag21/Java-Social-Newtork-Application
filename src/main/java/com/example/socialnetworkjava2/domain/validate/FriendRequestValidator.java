package com.example.socialnetworkjava2.domain.validate;

import com.example.socialnetworkjava2.domain.FriendRequest;

public class FriendRequestValidator implements Validator<FriendRequest>{
    @Override
    public void validate(FriendRequest friendRequest) throws ValidationException {
        if (friendRequest.getId_sender()== null) {
            throw new ValidationException("Sender id can`t be null!");
        }
        if (friendRequest.getId_receiver()== null) {
            throw new ValidationException("Receiver id can`t be null!");
        }
        if (friendRequest.getDate() == null) {
            throw new ValidationException("Invalid date");
        }
    }
}
