package com.example.socialnetworkjava2.domain.validate;

import com.example.socialnetworkjava2.domain.Friendship;

public class FriendshipValidator implements Validator<Friendship>{
    @Override
    public void validate(Friendship friendship) throws ValidationException {
        if (friendship.getId_user1() == null || friendship.getId_user2() == null){
            throw new ValidationException("The id can`t be null!");
        }
        if (friendship.getDate() == null){
            throw new ValidationException("The date can`t be null");
        }
    }
}
