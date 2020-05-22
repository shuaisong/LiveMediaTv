package com.tangmu.app.TengKuTV.component;


import com.tangmu.app.TengKuTV.module.dubbing.ShowDubbingVideoActivity;
import com.tangmu.app.TengKuTV.module.live.HistoryLiveActivity;
import com.tangmu.app.TengKuTV.module.live.LivingActivity;
import com.tangmu.app.TengKuTV.module.login.LoginActivity;
import com.tangmu.app.TengKuTV.module.login.ThirdBindActivity;
import com.tangmu.app.TengKuTV.module.main.MainActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.module.search.BookSearchActivity;
import com.tangmu.app.TengKuTV.module.search.VideoSearchActivity;
import com.tangmu.app.TengKuTV.module.vip.VIPActivity;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface ActivityComponent {


    void inject(ThirdBindActivity thirdBindActivity);

    void inject(TVDetailActivity TVDetailActivity);

    void inject(VideoSearchActivity videoSearchActivity);


    void inject(LivingActivity livingActivity);

    void inject(HistoryLiveActivity historyLiveActivity);

    void inject(ShowDubbingVideoActivity showDubbingVideoActivity);


    void inject(MovieDetailActivity movieDetailActivity);

    void inject(VIPActivity vipActivity);

    void inject(MainActivity mainActivity);

    void inject(LoginActivity loginActivity);

    void inject(BookSearchActivity bookSearchActivity);

}