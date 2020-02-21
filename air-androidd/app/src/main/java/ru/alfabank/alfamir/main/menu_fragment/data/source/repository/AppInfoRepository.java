package ru.alfabank.alfamir.main.menu_fragment.data.source.repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import ru.alfabank.alfamir.di.qualifiers.Remote;
import ru.alfabank.alfamir.main.menu_fragment.data.dto.Version;

@Singleton
public class AppInfoRepository implements AppInfoDataSource {

    private AppInfoDataSource mAppInfoRemoteDataSource;
    private Version mVersion;

    @Inject
    AppInfoRepository(@Remote AppInfoDataSource appInfoRemoteDataSource){
        mAppInfoRemoteDataSource = appInfoRemoteDataSource;
    }

    @Override
    public Observable<Version> getVersionInfo() {
        if(mVersion != null) return Observable.just(mVersion);
        return mAppInfoRemoteDataSource.getVersionInfo().flatMap(versionRaw -> {
            saveToCache(versionRaw);
            return Observable.just(mVersion);
        });
    }

    private void saveToCache(Version version){
        mVersion = version;
    }

}
