package ru.alfabank.alfamir.utility.logging.firebase;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class FirebaseWrapperModule {

    @Binds
    abstract FirebaseWrapper logWrapper (FirebaseWrapperImp firebaseWrapperImp);
}