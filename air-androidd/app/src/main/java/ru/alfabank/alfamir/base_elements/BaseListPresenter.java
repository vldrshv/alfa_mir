package ru.alfabank.alfamir.base_elements;

public interface BaseListPresenter<T> {
    int getListSize();
    int getItemViewType(int position);
    void takeListAdapter(T adapter);
    T getAdapter();
}
