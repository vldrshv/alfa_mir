package ru.alfabank.alfamir.di;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import ru.alfabank.alfamir.App;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.password.PasswordRepositoryModule;
import ru.alfabank.alfamir.alfa_tv.data.source.repository.show.ShowRepositoryModule;
import ru.alfabank.alfamir.calendar_event.data.source.repository.CalendarEventRepositoryModule;
import ru.alfabank.alfamir.data.source.broadcast_receiver.networkBR.NetworkBRModule;
import ru.alfabank.alfamir.data.source.broadcast_receiver.сonnectionTracker.ConnectionTrackerModule;
import ru.alfabank.alfamir.data.source.repositories.favorite_pages.FavoritePagesRepositoryModule;
import ru.alfabank.alfamir.data.source.repositories.favorite_people.FavoritePeopleRepositoryModule;
import ru.alfabank.alfamir.favorites.data.source.repository.FavoriteRepositoryModule;
import ru.alfabank.alfamir.feed.data.source.repository.PostRepositoryModule;
import ru.alfabank.alfamir.image.data.source.repository.ImageRepositoryModule;
import ru.alfabank.alfamir.image.domain.utility.LinkParserModule;
import ru.alfabank.alfamir.main.home_fragment.data.source.repository.TopNewsRepositoryModule;
import ru.alfabank.alfamir.main.menu_fragment.data.source.repository.AppInfoRepositoryModule;
import ru.alfabank.alfamir.messenger.data.source.repository.MessengerRepositoryModule;
import ru.alfabank.alfamir.notification.data.source.repository.NotificationsRepositoryModule;
import ru.alfabank.alfamir.profile.data.source.repository.ProfileRepositoryModule;
import ru.alfabank.alfamir.search.data.source.repository.SearchRepositoryModule;
import ru.alfabank.alfamir.survey.data.source.repository.SurveyRepositoryModule;
import ru.alfabank.alfamir.utility.database.DbModule;
import ru.alfabank.alfamir.utility.date_formatter.DateFormatterModule;
import ru.alfabank.alfamir.utility.image_cropper.ImageCropperModule;
import ru.alfabank.alfamir.utility.initials.InitialsProviderModule;
import ru.alfabank.alfamir.utility.logging.firebase.FirebaseWrapperModule;
import ru.alfabank.alfamir.utility.logging.local.LogWrapperModule;
import ru.alfabank.alfamir.utility.logging.remote.LifeCycleTrackerModule;
import ru.alfabank.alfamir.utility.logging.remote.LoggerModule;
import ru.alfabank.alfamir.utility.logging.remote.ServiceModule;
import ru.alfabank.alfamir.utility.network.IpProviderModule;
import ru.alfabank.alfamir.utility.update_notifier.UpdateNotifierModule;

@Singleton
@Component(modules = {
        // repositories
        ImageRepositoryModule.class,
        ProfileRepositoryModule.class,
        FavoritePeopleRepositoryModule.class,
        FavoritePagesRepositoryModule.class,
        PostRepositoryModule.class,
        SurveyRepositoryModule.class,
        NotificationsRepositoryModule.class,
        ShowRepositoryModule.class,
        MessengerRepositoryModule.class,
        PasswordRepositoryModule.class,
        SearchRepositoryModule.class,
        TopNewsRepositoryModule.class,
        CalendarEventRepositoryModule.class,
        FavoriteRepositoryModule.class,
        PostRepositoryModule.class,
        AppInfoRepositoryModule.class,
        // presentation stuff
//        SettingsActivityModule.class,
        // utilities
        ServiceModule.class,
        LoggerModule.class,
        LogWrapperModule.class,
        ConnectionTrackerModule.class,
        UpdateNotifierModule.class,
        LifeCycleTrackerModule.class,
        DateFormatterModule.class,
        LinkParserModule.class,
        IpProviderModule.class,
        InitialsProviderModule.class,
//        FavoritePostModule.class,
        FirebaseWrapperModule.class,
        ImageCropperModule.class,
        // global stuff
        ApplicationModule.class,
        SharedPreferencesModule.class,
        DbModule.class,
        ResourcesModule.class,
        NetworkBRModule.class,
        ActivityBindingModule.class,
        AndroidSupportInjectionModule.class})
public interface AppComponent extends AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        AppComponent.Builder application(App application);
        AppComponent build();
    }

}
