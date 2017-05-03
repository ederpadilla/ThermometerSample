package com.eder.padilla.alanproject;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by ederpadilla on 02/05/17.
 */

public class AlanProjectApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder().
                deleteRealmIfMigrationNeeded().
                build();
        Realm.setDefaultConfiguration(config);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
