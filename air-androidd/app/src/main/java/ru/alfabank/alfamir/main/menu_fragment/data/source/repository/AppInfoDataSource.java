package ru.alfabank.alfamir.main.menu_fragment.data.source.repository;


import io.reactivex.Observable;
import ru.alfabank.alfamir.main.menu_fragment.data.dto.Version;

public interface AppInfoDataSource {

    Observable<Version> getVersionInfo();

}
