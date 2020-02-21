package ru.alfabank.alfamir.calendar_event.presentation.dagger_module;

import dagger.Binds;
import dagger.Module;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.calendar_event.presentation.CalendarEventPresenter;
import ru.alfabank.alfamir.calendar_event.presentation.contract.CalendarEventAdapterContract;

@Module
public abstract class CalendarEventModule {
    @FragmentScoped
    @Binds
    abstract CalendarEventAdapterContract.Presenter peoplePresenter(CalendarEventPresenter presenter);

}