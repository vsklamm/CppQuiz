package com.vsklamm.cppquiz;

import android.app.Application;

import com.vsklamm.cppquiz.di.module.AppModule;
import com.vsklamm.cppquiz.di.module.NetworkModule;
import com.vsklamm.cppquiz.di.scope.Scopes;

import toothpick.Scope;
import toothpick.Toothpick;
import toothpick.smoothie.module.SmoothieApplicationModule;

public class App extends Application {

    private static Scope appScope;

    @Override
    public void onCreate() {
        super.onCreate();

        // Toothpick.setConfiguration(Configuration.forDevelopment());

        appScope = Toothpick.openScope(Scopes.APP);
        appScope.installTestModules(new SmoothieApplicationModule(this), new NetworkModule(), new AppModule(this)); // TODO: getAppContext
    }

    public static Scope getAppScope() {
        return appScope;
    }
}
