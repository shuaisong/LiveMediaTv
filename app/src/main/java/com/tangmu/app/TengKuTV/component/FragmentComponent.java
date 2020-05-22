package com.tangmu.app.TengKuTV.component;

import com.tangmu.app.TengKuTV.module.book.BookChildFragment;
import com.tangmu.app.TengKuTV.module.home.HomeChildFragment;
import com.tangmu.app.TengKuTV.module.home.HomeDubbingFragment;
import com.tangmu.app.TengKuTV.module.home.HomeFragment;
import com.tangmu.app.TengKuTV.module.home.HomeVipFragment;
import com.tangmu.app.TengKuTV.module.live.LiveFragment;
import com.tangmu.app.TengKuTV.module.mine.collect.CollectFragment;

import dagger.Component;

@Component(dependencies = AppComponent.class)
public interface FragmentComponent {

    void inject(HomeChildFragment homeChildFragment);

    void inject(HomeDubbingFragment homeDubbingFragment);

    void inject(HomeVipFragment homeVipFragment);

    void inject(BookChildFragment bookChildFragment);

    void inject(LiveFragment liveFragment);

    void inject(CollectFragment collectFragment);

    void inject(HomeFragment homeFragment);

}