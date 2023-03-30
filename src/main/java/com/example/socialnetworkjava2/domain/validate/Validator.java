package com.example.socialnetworkjava2.domain.validate;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
