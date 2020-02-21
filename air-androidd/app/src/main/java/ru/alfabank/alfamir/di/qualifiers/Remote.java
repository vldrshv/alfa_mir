package ru.alfabank.alfamir.di.qualifiers;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Qualifier;

/**
 * Created by mshvdvsk on 26/03/2018.
 */

@Qualifier
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {

}
