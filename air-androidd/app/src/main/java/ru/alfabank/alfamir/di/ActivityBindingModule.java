package ru.alfabank.alfamir.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import ru.alfabank.alfamir.alfa_tv.presentation.show.ShowActivity;
import ru.alfabank.alfamir.alfa_tv.presentation.show.dagger_module.ShowModule;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.ShowListFragment;
import ru.alfabank.alfamir.alfa_tv.presentation.show_list.dagger_module.ShowListModule;
import ru.alfabank.alfamir.base_elements.BaseFragment;
import ru.alfabank.alfamir.calendar_event.presentation.dagger_module.CalendarEventModule;
import ru.alfabank.alfamir.di.scopes.ActivityScoped;
import ru.alfabank.alfamir.di.scopes.FragmentScoped;
import ru.alfabank.alfamir.favorites.presentation.favorite_post.FavoritePostFragment;
import ru.alfabank.alfamir.favorites.presentation.favorite_post.dagger_module.FavoritePostModule;
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.FavoriteProfileFragment;
import ru.alfabank.alfamir.favorites.presentation.favorite_profile.dagger_module.FavoriteProfileModule;
import ru.alfabank.alfamir.feed.presentation.feed.FeedActivity;
import ru.alfabank.alfamir.feed.presentation.feed.FeedFragment;
import ru.alfabank.alfamir.feed.presentation.feed.dagger_modules.FeedFragmentModule;
import ru.alfabank.alfamir.feed.presentation.feed_with_header.FeedWithHeaderFragment;
import ru.alfabank.alfamir.feed.presentation.feed_with_header.FeedWithHeaderModule;
import ru.alfabank.alfamir.initialization.presentation.initialization.InitializationActivity;
import ru.alfabank.alfamir.initialization.presentation.initialization.dagger_module.InitializationActivityModule;
import ru.alfabank.alfamir.main.ExtendedSplashActivity;
import ru.alfabank.alfamir.main.home_fragment.presentation.HomeFragment;
import ru.alfabank.alfamir.main.home_fragment.presentation.dagger_module.HomeFragmentModule;
import ru.alfabank.alfamir.main.main_activity.MainActivity;
import ru.alfabank.alfamir.main.main_activity.dagger_module.MainActivityModule;
import ru.alfabank.alfamir.main.main_feed_fragment.MainFeedFragment;
import ru.alfabank.alfamir.main.main_feed_fragment.dagger_module.MainFeedFragmentModule;
import ru.alfabank.alfamir.main.media_fragment.MediaFragment;
import ru.alfabank.alfamir.main.media_fragment.MediaFragmentModule;
import ru.alfabank.alfamir.main.menu_fragment.presentation.MenuFragment;
import ru.alfabank.alfamir.main.menu_fragment.presentation.dagger_module.MenuFragmentModule;
import ru.alfabank.alfamir.messenger.presentation.chat_activity.MessengerActivity;
import ru.alfabank.alfamir.messenger.presentation.chat_activity.MessengerModule;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.ChatFragment;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.FullScreenFragment;
import ru.alfabank.alfamir.messenger.presentation.chat_fragment.dagger_module.ChatFragmentModule;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.ChatListFragment;
import ru.alfabank.alfamir.messenger.presentation.chat_list_fragment.dagger_module.ChatListFragmentModule;
import ru.alfabank.alfamir.notification.old.ActivityNotifications;
import ru.alfabank.alfamir.notification.presentation.NotificationFragment;
import ru.alfabank.alfamir.notification.presentation.dagger_module.NotificationFragmentModule;
import ru.alfabank.alfamir.people.presentation.PeopleActivity;
import ru.alfabank.alfamir.people.presentation.dagger_module.PeopleActivityModule;
import ru.alfabank.alfamir.post.presentation.post.PostActivity;
import ru.alfabank.alfamir.post.presentation.post.PostFragment;
import ru.alfabank.alfamir.post.presentation.post.dagger_module.PostActivityModule;
import ru.alfabank.alfamir.post.presentation.post.dagger_module.PostFragmentModule;
import ru.alfabank.alfamir.poster.data.PosterActivityModule;
import ru.alfabank.alfamir.poster.data.PosterFragmentModule;
import ru.alfabank.alfamir.poster.presentation.PosterActivity;
import ru.alfabank.alfamir.poster.presentation.PosterFragment;
import ru.alfabank.alfamir.poster.presentation.WriteQuestionFragment;
import ru.alfabank.alfamir.profile.presentation.absence.AbsenceActivity;
import ru.alfabank.alfamir.profile.presentation.absence.AbsencePresenterModule;
import ru.alfabank.alfamir.profile.presentation.address.AddressActivity;
import ru.alfabank.alfamir.profile.presentation.address.AddressPresenterModule;
import ru.alfabank.alfamir.profile.presentation.edit_info.EditInfoActivity;
import ru.alfabank.alfamir.profile.presentation.edit_info.EditInfoPresenterModule;
import ru.alfabank.alfamir.profile.presentation.personal_info.PersonalInfoActivity;
import ru.alfabank.alfamir.profile.presentation.personal_info.PersonalInfoPresenterModule;
import ru.alfabank.alfamir.profile.presentation.profile.ProfileActivity;
import ru.alfabank.alfamir.profile.presentation.profile.ProfilePresenterModule;
import ru.alfabank.alfamir.profile.presentation.skills.PersonSkillsActivity;
import ru.alfabank.alfamir.profile.presentation.skills.PersonSkillsPresenterModule;
import ru.alfabank.alfamir.search.presentation.SearchFragment;
import ru.alfabank.alfamir.search.presentation.dagger_module.SearchFragmentModule;
import ru.alfabank.alfamir.settings.presentation.settings.SettingsActivity;
import ru.alfabank.alfamir.settings.presentation.settings.SettingsActivityModule;
import ru.alfabank.alfamir.survey.presentation.SurveyActivity;
import ru.alfabank.alfamir.survey.presentation.dagger_module.SurveyPresenterModule;
import ru.alfabank.alfamir.test.TestActivity;
import ru.alfabank.alfamir.test.TestActivity2;
import ru.alfabank.alfamir.ui.activities.ActivityCreatePost;
import ru.alfabank.alfamir.ui.activities.ActivityPublishMedia;
import ru.alfabank.alfamir.ui.activities.ActivityTeam;

@Module
abstract class ActivityBindingModule {

    // Activity

    @ActivityScoped
    @ContributesAndroidInjector(modules = PersonalInfoPresenterModule.class)
    abstract PersonalInfoActivity personalInfoActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ProfilePresenterModule.class)
    abstract ProfileActivity profileActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = EditInfoPresenterModule.class)
    abstract EditInfoActivity editInfoActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AbsencePresenterModule.class)
    abstract AbsenceActivity absenceActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = AddressPresenterModule.class)
    abstract AddressActivity adressActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PersonSkillsPresenterModule.class)
    abstract PersonSkillsActivity personSkillsActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PeopleActivityModule.class)
    abstract PeopleActivity peopleActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = ShowModule.class)
    abstract ShowActivity showActivityHalfMonster();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PostActivityModule.class)
    abstract PostActivity postActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = PosterActivityModule.class)
    abstract PosterActivity posterActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SurveyPresenterModule.class)
    abstract SurveyActivity quizActivity();

    @ActivityScoped
    @ContributesAndroidInjector
    abstract FeedActivity getNewsActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract ActivityTeam activityTeam();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity mainActivity();

    @ActivityScoped
    @ContributesAndroidInjector(modules = SettingsActivityModule.class)
    abstract SettingsActivity settingsActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract ActivityCreatePost activityCreatePost();

    @ActivityScoped
    @ContributesAndroidInjector(modules = InitializationActivityModule.class)
    abstract InitializationActivity activityInitialization();

    @ActivityScoped
    @ContributesAndroidInjector(modules = InitializationActivityModule.class)
    abstract ExtendedSplashActivity extendedSplashActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract ActivityPublishMedia activityPublishMedia();

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract ActivityNotifications activityNotifications();

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract TestActivity testActivity();

    @ActivityScoped
    @ContributesAndroidInjector()
    abstract TestActivity2 testActivity2();

    @ActivityScoped
    @ContributesAndroidInjector(modules = MessengerModule.class)
    abstract MessengerActivity messengerActivity();

    // Fragment

    @FragmentScoped
    @ContributesAndroidInjector(modules = FeedWithHeaderModule.class)
    abstract FeedWithHeaderFragment feedWithHeaderFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = ChatFragmentModule.class)
    abstract ChatFragment chatFragmentModule();

    @FragmentScoped
    @ContributesAndroidInjector(modules = ChatListFragmentModule.class)
    abstract ChatListFragment chatListFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {HomeFragmentModule.class, CalendarEventModule.class})
    abstract HomeFragment getHomeFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {PosterFragmentModule.class})
    abstract PosterFragment getPosterFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = {PosterFragmentModule.class})
    abstract WriteQuestionFragment getQuestionFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = MenuFragmentModule.class)
    abstract MenuFragment getMenuFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = MainFeedFragmentModule.class)
    abstract MainFeedFragment newsFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = MediaFragmentModule.class)
    abstract MediaFragment mediaFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = ShowListModule.class)
    abstract ShowListFragment showListFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = PostFragmentModule.class)
    abstract PostFragment postFragment();

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract BaseFragment baseFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = SearchFragmentModule.class)
    abstract SearchFragment searchFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = FavoritePostModule.class)
    abstract FavoritePostFragment favoritePageFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = FavoriteProfileModule.class)
    abstract FavoriteProfileFragment favoriteProfileFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = FeedFragmentModule.class)
    abstract FeedFragment feedFragment();

    @FragmentScoped
    @ContributesAndroidInjector(modules = NotificationFragmentModule.class)
    abstract NotificationFragment getNotificationFragment();

    @FragmentScoped
    @ContributesAndroidInjector()
    abstract FullScreenFragment getFullScreenFragment();
}

