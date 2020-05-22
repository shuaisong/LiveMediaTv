package com.tangmu.app.TengKuTV.component;

import android.content.Context;

import dagger.Module;
import dagger.Provides;


@Module
public class AppModule {
    private Context context;

    public AppModule(Context context) {
        this.context = context;
    }

    @Provides
    Context getContext() {
        return context;
    }

}