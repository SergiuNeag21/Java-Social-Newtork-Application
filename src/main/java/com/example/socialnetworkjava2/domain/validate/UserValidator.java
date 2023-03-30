package com.example.socialnetworkjava2.domain.validate;

import com.example.socialnetworkjava2.domain.User;

import java.util.Objects;

public class UserValidator implements Validator<User>{
    @Override
    public void validate(User entity) throws ValidationException {
        String err="";

        if (Objects.equals(entity.getFirst_name(), "")) {
            err = err + "First name cannot be empty.\n";
        }
        if (Objects.equals(entity.getLast_name(), "")) {
            err = err + "First name cannot be empty.\n";
        }
        if (err.length() > 0) {
            throw new ValidationException(err);
        }
    }
}
