package com.example.socialnetworkjava2.utils.observer;

import com.example.socialnetworkjava2.utils.Event;

public interface Observable<E extends Event> {
    void addObserver(Observer<E> e);

    void notifyObserver(E t);
}
