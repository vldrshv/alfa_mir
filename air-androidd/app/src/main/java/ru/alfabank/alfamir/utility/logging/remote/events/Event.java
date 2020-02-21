package ru.alfabank.alfamir.utility.logging.remote.events;

/**
 * Created by U_M0WY5 on 05.03.2018.
 */

public interface Event {
    void setState(int state);
    int getState();
}
