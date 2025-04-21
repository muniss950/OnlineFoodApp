package com.fooddelivery.notifications;

public interface Subject {
    void notifyObservers(EventType type);
}
