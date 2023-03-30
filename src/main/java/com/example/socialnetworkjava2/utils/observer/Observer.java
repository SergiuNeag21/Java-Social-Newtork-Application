package com.example.socialnetworkjava2.utils.observer;

import com.example.socialnetworkjava2.utils.Event;

public interface Observer<E extends Event>{
    void update(E e);
}
