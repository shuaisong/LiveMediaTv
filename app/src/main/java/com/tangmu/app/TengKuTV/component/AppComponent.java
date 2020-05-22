package com.tangmu.app.TengKuTV.component;

import android.content.Context;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
    Context getContext();
}