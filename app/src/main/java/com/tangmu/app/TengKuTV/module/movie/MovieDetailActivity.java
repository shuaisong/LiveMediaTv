package com.tangmu.app.TengKuTV.module.movie;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.bean.AdBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.bean.VideoDetailBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.VideoDetailContact;
import com.tangmu.app.TengKuTV.db.PlayHistoryManager;
import com.tangmu.app.TengKuTV.module.WebViewActivity;
import com.tangmu.app.TengKuTV.module.dubbing.ShowDubbingVideoActivity;
import com.tangmu.app.TengKuTV.module.login.LoginActivity;
import com.tangmu.app.TengKuTV.module.vip.VIPActivity;
import com.tangmu.app.TengKuTV.presenter.VideoDetailPresenter;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.MovieItemDecoration;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.CustomPraiseView;
import com.tangmu.app.TengKuTV.view.TitleView;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerVideoId;
import com.tencent.liteav.demo.play.SuperPlayerView;
import com.tencent.liteav.demo.play.bean.TCVideoQuality;
import com.tencent.liteav.demo.play.view.TCVodQualityView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;

public class MovieDetailActivity extends BaseActivity implements VideoDetailContact.View, View.OnFocusChangeListener {
    @Inject
    VideoDetailPresenter presenter;
    @BindView(R.id.superPlayer)
    SuperPlayerView superPlayer;
    @BindView(R.id.titleView)
    TitleView titleView;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.ic_vip)
    ImageView icVip;
    @BindView(R.id.ic_hd)
    ImageView icHd;
    @BindView(R.id.anthogy_num)
    TextView anthogyNum;
    @BindView(R.id.introl)
    TextView introl;
    @BindView(R.id.more)
    TextView more;
    @BindView(R.id.full_screen)
    TextView fullScreen;
    @BindView(R.id.collect)
    CustomPraiseView collect;
    @BindView(R.id.is_vip)
    TextView isVip;
    @BindView(R.id.tv_anthogy)
    TextView tvAnthogy;
    @BindView(R.id.recyclerview_anthogy)
    RecyclerView recyclerviewAnthogy;
    @BindView(R.id.recyclerview_anthogy1)
    RecyclerView recyclerviewAnthogy1;
    @BindView(R.id.video_recycler)
    RecyclerView videoRecycler;
    @BindView(R.id.ad1)
    ImageView ivAd1;
    @BindView(R.id.ad2)
    ImageView ivAd2;

    private BaseQuickAdapter<HomeChildRecommendBean.VideoBean, BaseViewHolder> recommendMovieAdapter;
    private VideoDetailBean videoDetailBean;
    private int id;
    private int c_id;
    private AdBean adBean;
    private boolean showAd;
    private Timer timer;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        id = getIntent().getIntExtra("id", 0);
        c_id = getIntent().getIntExtra("c_id", 0);
        LoginBean login = PreferenceManager.getInstance().getLogin();
        if (login == null || login.getU_vip_status() == 0) {
            showAd = true;
            superPlayer.setShowAd(true);
            presenter.getAd(c_id);
        } else
            presenter.getDetail(id);
        presenter.getTvAd(c_id);
        presenter.getRecommend(c_id);
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (superPlayer != null)
            superPlayer.resetPlayer();
        if (timer != null) timer.cancel();
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        superPlayer.onPause();
    }

    @OnClick({R.id.more, R.id.full_screen, R.id.collect})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.more:
                if (introl.getMaxLines() == 2) {
                    more.setText(getString(R.string.fold));
                    introl.setMaxLines(Integer.MAX_VALUE);
                } else {
                    more.setText(getString(R.string.more1));
                    introl.setMaxLines(2);
                }
                break;
            case R.id.full_screen:
                superPlayer.requestFullMode();
                break;
            case R.id.collect:
                if (videoDetailBean == null) {
                    collect.setChecked(false);
                } else {
                    if (videoDetailBean.getIs_colle_status() == 1) {
                        presenter.unCollect(videoDetailBean.getUc_id());
                    } else
                        presenter.collect(videoDetailBean.getVm_id());
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            superPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
        } else {
            if (videoDetailBean != null) {
                if (superPlayer.findViewById(R.id.adView).getVisibility() != View.VISIBLE) {
                    videoDetailBean.setProgress(superPlayer.getProgress());
                }
                PlayHistoryManager.save(videoDetailBean, superPlayer.getPosition());
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void initView() {
        presenter.attachView(this);
        initRecommendList();
        ivAd1.setOnFocusChangeListener(this);
        ivAd2.setOnFocusChangeListener(this);
        superPlayer.setRootId(R.id.rootView);
        recyclerviewAnthogy.setVisibility(View.GONE);
        recyclerviewAnthogy1.setVisibility(View.GONE);
//        superPlayer.setAdFreeClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (PreferenceManager.getInstance().getLogin() != null)
//                    startActivityForResult(new Intent(MovieDetailActivity.this, VIPActivity.class), 100);
//                else {
//                    if (!EventBus.getDefault().isRegistered(MovieDetailActivity.this))
//                        EventBus.getDefault().register(MovieDetailActivity.this);
//                }
//            }
//        });
//        superPlayer.setLoginClick(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (PreferenceManager.getInstance().getLogin() != null)
//                    startActivityForResult(new Intent(MovieDetailActivity.this, VIPActivity.class), 101);
//                else {
//                    if (!EventBus.getDefault().isRegistered(MovieDetailActivity.this))
//                        EventBus.getDefault().register(MovieDetailActivity.this);
//                }
//            }
//        });
        superPlayer.setVideoQualityCallback(new TCVodQualityView.VideoQualityCallback() {
            @Override
            public boolean onQualitySelect(TCVideoQuality quality) {
                if (quality.name.equals("HD")) {
                    if (isLogin()) {
                        return false;
                    } else {
                        startActivity(new Intent(MovieDetailActivity.this, LoginActivity.class));
                        return true;
                    }
                }
                if (quality.name.equals("2K") || quality.name.equals("4K")) {
                    if (isLogin()) {
                        LoginBean login = PreferenceManager.getInstance().getLogin();
                        if (login.getU_vip_status() == 1) {
                            return false;
                        } else {
                            startActivity(new Intent(MovieDetailActivity.this, VIPActivity.class));
                        }
                    } else {
                        startActivity(new Intent(MovieDetailActivity.this, LoginActivity.class));
                        return true;
                    }
                }
                return false;
            }
        });
        int defaultQuality = PreferenceManager.getInstance().getDefaultQuality();
        LoginBean login = PreferenceManager.getInstance().getLogin();
        PreferenceManager.getInstance().getLogin();
        if (defaultQuality > 4) {
            if (login != null && login.getU_vip_status() == 1) {
                superPlayer.setDefaultQualitySet(defaultQuality);
            }
        } else if (defaultQuality > 3) {
            if (login != null) {
                superPlayer.setDefaultQualitySet(defaultQuality);
            }
        } else {
            superPlayer.setDefaultQualitySet(defaultQuality);
        }
    }

    private void initRecommendList() {
        videoRecycler.addItemDecoration(new MovieItemDecoration(this));
        recommendMovieAdapter = new BaseQuickAdapter<HomeChildRecommendBean.VideoBean, BaseViewHolder>(R.layout.item_movie_rec) {
            @Override
            protected void convert(BaseViewHolder helper, HomeChildRecommendBean.VideoBean item) {
                TextView des = helper.getView(R.id.des);
                String desStr = Util.showText(item.getVm_des(), item.getVm_des_z());
                String titleStr = Util.showText(item.getVm_title(), item.getVm_title_z());
                if (Util.isellipsis(desStr, des)) {
                    helper.setGone(R.id.ellipsis, true);
                } else helper.setGone(R.id.ellipsis, false);
                des.setText(desStr);
                helper.setText(R.id.title, titleStr);
                if (item.getVm_is_pay() == 2) {
                    helper.setVisible(R.id.vip, true);
                } else {
                    helper.setVisible(R.id.vip, false);
                }
                if (item.getVm_update_status() == 2) {
                    helper.setText(R.id.endTime, getResources().getString(R.string.update_done));
                } else
                    helper.setText(R.id.endTime, String.format(getResources().getString(R.string.update_status), item.getCount()));
                GlideUtils.getRequest(MovieDetailActivity.this, Util.convertImgPath(item.getVm_img())).placeholder(R.mipmap.img_default)
                        .transform(new CenterCrop(), new RoundedCorners(AutoSizeUtils.dp2px(MovieDetailActivity.this, 5)))
                        .into((ImageView) helper.getView(R.id.image));
            }
        };
        recommendMovieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeChildRecommendBean.VideoBean item = recommendMovieAdapter.getItem(position);
                if (item == null) return;
                Intent intent;
                if (item.getVm_type() == 2) {
                    intent = new Intent(MovieDetailActivity.this, TVDetailActivity.class);
                } else
                    intent = new Intent(MovieDetailActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", item.getVm_id());
                intent.putExtra("c_id", item.getVt_id_one());
                startActivity(intent);
                finish();
            }
        });
        videoRecycler.setAdapter(recommendMovieAdapter);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_movie_detail;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (superPlayer != null) {
            if (superPlayer.getBuyAntholgyView().getVisibility() != View.VISIBLE)
                superPlayer.onResume();
        }
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            long currentTimeMillis = System.currentTimeMillis();
                            titleView.setTime(Util.convertSystemTime(currentTimeMillis));
                        }
                    });
                }
            }, 0, 1000);
        }
        titleView.updateTV_Vip();
    }

    @Override
    public void showDetail(VideoDetailBean videoDetailBean) {
        //不开防盗链
        this.videoDetailBean = videoDetailBean;
        String titleStr = Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z());
//            superPlayer.setWatermark(R.mipmap.logo);
        superPlayer.setIsNeedVip(videoDetailBean.getVm_is_pay() == 2);
        if (videoDetailBean.getVm_is_pay() == 2) {
            superPlayer.setShowAd(false);
            if (PreferenceManager.getInstance().getLogin() != null) {
                String expire_time = videoDetailBean.getExpire_time();
                if (videoDetailBean.getIs_pay_status() == 0) {//未购买
                    superPlayer.setBuyViewVisible(true);
                    superPlayer.setBuyClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivityForResult(new Intent(MovieDetailActivity.this, VIPActivity.class), 100);
                        }
                    });
                } else {//已购买
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
                    try {
                        Date date = simpleDateFormat.parse(expire_time);
                        if (date != null) {
                            if (date.getTime() > System.currentTimeMillis()) {//未到期
                                superPlayer.setBuyViewVisible(false);
                            } else {//到期
                                superPlayer.setBuyViewVisible(true);
                                superPlayer.setBuyClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivityForResult(new Intent(MovieDetailActivity.this, VIPActivity.class), 100);
                                    }
                                });
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                SuperPlayerModel model = new SuperPlayerModel();
                model.appId = Constant.PLAYID;// 配置 AppId
                model.videoId = new SuperPlayerVideoId();
                model.videoId.fileId = videoDetailBean.getV_fileid(); // 配置 FileId
                model.title = Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z());
                superPlayer.playWithModel(model);
            } else {
                superPlayer.resetPlayer();
                superPlayer.showLoginTip(true);
            }
        } else if (showAd && adBean != null) {
            superPlayer.setShowAd(true);
            superPlayer.setInfo(Constant.PLAYID, adBean.getVa_fileid(), videoDetailBean.getV_fileid(), Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z()));
        } else {
            superPlayer.setShowAd(false);
            SuperPlayerModel model = new SuperPlayerModel();
            model.appId = Constant.PLAYID;// 配置 AppId
            model.videoId = new SuperPlayerVideoId();
            model.videoId.fileId = videoDetailBean.getV_fileid(); // 配置 FileId
            model.title = Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z());
            superPlayer.playWithModel(model);
        }
        superPlayer.isDefaultLanguage(PreferenceManager.getInstance().isDefaultLanguage());
        title.setText(titleStr);
        isVip.setVisibility(videoDetailBean.getVm_is_pay() == 2 ? View.VISIBLE : View.GONE);//2付费显示vip
        introl.setText(Util.showText(videoDetailBean.getVm_des(), videoDetailBean.getVm_des_z()));
        icVip.setVisibility(videoDetailBean.getVm_is_pay() == 2 ? View.VISIBLE : View.GONE);//2付费显示vip
        isVip.setVisibility(videoDetailBean.getVm_is_pay() == 2 ? View.VISIBLE : View.GONE);//2付费显示vip
        collect.setChecked(videoDetailBean.getIs_colle_status() == 1);
    }

    @Override
    public void showRecommend(List<HomeChildRecommendBean.VideoBean> videoBeans) {
        recommendMovieAdapter.setNewData(videoBeans);
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showText(msg);
    }

    @Override
    public void collectSuccess(Integer uc_id) {
        videoDetailBean.setUc_id(uc_id);
        videoDetailBean.setIs_colle_status(1);
        collect.setChecked(true);
    }

    @Override
    public void unCollectSuccess() {
        videoDetailBean.setIs_colle_status(0);
        collect.setChecked(false);
    }

    @Override
    public void showAdError(String msg) {
        ToastUtil.showText(msg);
        presenter.getDetail(id);
    }

    @Override
    public void showTVAd(List<VideoAdBean> videoAdBeans) {
        if (!videoAdBeans.isEmpty()) {
            GlideUtils.getRequest(this, Util.convertImgPath(videoAdBeans.get(0).getTa_img()))
                    .centerCrop().into(ivAd1);
            GlideUtils.getRequest(this, Util.convertImgPath(videoAdBeans.get(0).getTa_img()))
                    .centerCrop().into(ivAd2);
        }
    }

    @Override
    public void showAd(AdBean adBean) {
        this.adBean = adBean;
        SuperPlayerModel model = new SuperPlayerModel();
        model.videoId = new SuperPlayerVideoId();
//        model.url = Constant.Video_IP + adBean.getVa_url();
/*        model.appId = Constant.PLAYID;
        model.videoId.fileId = adBean.getVa_fileid();
        superPlayer.playWithModel(model);*/
        presenter.getDetail(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (PreferenceManager.getInstance().getLogin() != null && PreferenceManager.getInstance().getLogin().getU_vip_status() == 1) {
                openVipSuccess();
            } else {
                ToastUtil.showText(getString(R.string.not_open_vip));
            }
        }
        if (requestCode == 101) {
            if (PreferenceManager.getInstance().getLogin() != null && PreferenceManager.getInstance().getLogin().getU_vip_status() == 1) {
                superPlayer.showLoginTip(false);
                openVipSuccess();
            } else {
                ToastUtil.showText(getString(R.string.not_open_vip));
            }
        }
    }

    private void openVipSuccess() {
        showAd = false;
        superPlayer.setShowAd(false);
        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE) {
            superPlayer.setBuyViewVisible(false);
            superPlayer.onResume();
        } else {
            SuperPlayerModel superPlayerModel = new SuperPlayerModel();
            superPlayerModel.appId = Constant.PLAYID;
            superPlayerModel.videoId = new SuperPlayerVideoId();
            superPlayerModel.videoId.fileId = videoDetailBean.getV_fileid();
            superPlayerModel.title = Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z());
            superPlayer.playWithModel(superPlayerModel);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(LoginBean message) {
        if (message.getU_vip_status() == 1) {
            openVipSuccess();
        } else {
            ToastUtil.showText(getString(R.string.not_open_vip));
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        LogUtil.e(keyCode + "");
        View currentFocus = getCurrentFocus();
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_MENU) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                superPlayer.showMenu();
            }
        }
        if (keyCode == KeyEvent.KEYCODE_ENTER || keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN && currentFocus == null) {
                if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE)
                    if (superPlayer.findViewById(R.id.pause_ad_view).getVisibility() == View.VISIBLE) {
                        superPlayer.findViewById(R.id.pause_ad_view).setVisibility(View.GONE);
                    } else
                        superPlayer.onResume();
                else {
                    superPlayer.onPause();
                }
            }
            if (currentFocus != null) {
                switch (currentFocus.getId()) {
                    case R.id.controller_small:
                        superPlayer.requestFullMode();
                        return true;
                    case R.id.adView:
                        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_WINDOW) {
                            superPlayer.requestFullMode();
                            return true;
                        }
                        if (PreferenceManager.getInstance().getLogin() != null)
                            startActivityForResult(new Intent(MovieDetailActivity.this, VIPActivity.class), 100);
                        else {
                            if (!EventBus.getDefault().isRegistered(MovieDetailActivity.this))
                                EventBus.getDefault().register(MovieDetailActivity.this);
                            startActivityForResult(new Intent(MovieDetailActivity.this, LoginActivity.class), 100);
                        }
                        return true;
                    case R.id.vipTipView:
                        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_WINDOW) {
                            superPlayer.requestFullMode();
                            return true;
                        }
                        if (PreferenceManager.getInstance().getLogin() != null)
                            startActivityForResult(new Intent(MovieDetailActivity.this, VIPActivity.class), 101);
                        else {
                            if (!EventBus.getDefault().isRegistered(MovieDetailActivity.this))
                                EventBus.getDefault().register(MovieDetailActivity.this);
                            startActivityForResult(new Intent(MovieDetailActivity.this, LoginActivity.class), 101);
                        }
                        return true;
                    case R.id.buyAntholgyView:
                        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_WINDOW) {
                            superPlayer.requestFullMode();
                            return true;
                        }
                        if (PreferenceManager.getInstance().getLogin() != null)
                            startActivityForResult(new Intent(MovieDetailActivity.this, VIPActivity.class), 101);
                        else {
                            startActivityForResult(new Intent(MovieDetailActivity.this, LoginActivity.class), 101);
                            if (!EventBus.getDefault().isRegistered(MovieDetailActivity.this))
                                EventBus.getDefault().register(MovieDetailActivity.this);
                        }
                        return true;
                    case R.id.pause_ad_view:
                        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_WINDOW) {
                            superPlayer.requestFullMode();
                            return true;
                        }
                        superPlayer.findViewById(R.id.pause_ad_view).setVisibility(View.GONE);
                        return true;
                }
            }

        }
        if (currentFocus != null)
            LogUtil.e(currentFocus.toString());
        return super.onKeyUp(keyCode, event);
    }

    private void tvAdClick(VideoAdBean videoAdBean) {
        Intent intent = null;
        switch (videoAdBean.getTa_type()) {
            case 1:
                if (videoAdBean.getVm_type() == 2)
                    intent = new Intent(MovieDetailActivity.this, TVDetailActivity.class);
                else intent = new Intent(MovieDetailActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                intent.putExtra("c_id", videoAdBean.getVt_id_one());
                break;
            case 2:
                intent = new Intent(MovieDetailActivity.this, ShowDubbingVideoActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                break;
            case 3:
                intent = new Intent(MovieDetailActivity.this, WebViewActivity.class);
                intent.putExtra("url", videoAdBean.getTa_url());
                break;
        }
        if (intent != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int dimension = (int) getResources().getDimension(R.dimen.d_1);
            v.setPadding(0, dimension, 0, dimension);
        } else {
            v.setPadding(0, 0, 0, 0);
        }
    }

    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                superPlayer.showProgress(keyCode);
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                superPlayer.showProgress(keyCode);
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
