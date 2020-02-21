package ru.alfabank.alfamir.base_elements;

/**
 * Created by mshvdvsk on 26/03/2018.
 */

public interface BasePresenter<T> {

    void takeView(T view);

    void dropView();

    T getView();

}
