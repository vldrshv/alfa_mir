package ru.alfabank.alfamir.main.menu_fragment.domain.usecase;

import io.reactivex.Observable;
import ru.alfabank.alfamir.main.menu_fragment.data.dto.Version;
import ru.alfabank.alfamir.main.menu_fragment.data.source.repository.AppInfoRepository;

import javax.inject.Inject;

public class CheckVersion  {

    private AppInfoRepository mAppInfoRepository;

    @Inject
    CheckVersion(AppInfoRepository appInfoRepository) {
        mAppInfoRepository = appInfoRepository;
    }

    public Observable<Version> execute() {
        return mAppInfoRepository.getVersionInfo();
    }
}