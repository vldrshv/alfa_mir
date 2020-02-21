package ru.alfabank.alfamir.image.domain.utility;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
abstract public class LinkParserModule {
    @Singleton
    @Binds
    abstract LinkParser provideImageRemoteDataSource(LinkParserImp dataSource);
}