package com.tangmu.app.TengKuTV.module.movie;

import android.content.Intent;
import android.text.TextPaint;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.bean.AdBean;
import com.tangmu.app.TengKuTV.bean.BodyBean;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.bean.OrderBean;
import com.tangmu.app.TengKuTV.bean.TVProductBean;
import com.tangmu.app.TengKuTV.bean.VideoAdBean;
import com.tangmu.app.TengKuTV.bean.VideoDetailBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.VideoDetailContact;
import com.tangmu.app.TengKuTV.db.PlayHistoryInfo;
import com.tangmu.app.TengKuTV.db.PlayHistoryManager;
import com.tangmu.app.TengKuTV.module.WebViewActivity;
import com.tangmu.app.TengKuTV.module.dubbing.ShowDubbingVideoActivity;
import com.tangmu.app.TengKuTV.module.vip.MiGuActivity;
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
import com.tencent.liteav.demo.play.bean.VideoBean;
import com.tencent.liteav.demo.play.bean.VideoSortBean;
import com.tencent.liteav.demo.play.utils.AnthologyItemDecoration;
import com.tencent.liteav.demo.play.view.TCVodAnthologyView;
import com.tencent.liteav.demo.play.view.TCVodQualityView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

public class TVDetailActivity extends BaseActivity implements VideoDetailContact.View, View.OnFocusChangeListener {
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
    @BindView(R.id.ad1)
    ImageView ivAd1;
    @BindView(R.id.ad2)
    ImageView ivAd2;
    @BindView(R.id.recyclerview_anthogy)
    RecyclerView recyclerviewAnthogy;
    @BindView(R.id.recyclerview_anthogy1)
    RecyclerView recyclerviewAnthogy1;
    @BindView(R.id.video_recycler)
    RecyclerView videoRecycler;
    private BaseQuickAdapter<HomeChildRecommendBean.VideoBean, BaseViewHolder> recommendMovieAdapter;
    private BaseQuickAdapter<VideoBean, BaseViewHolder> anthologyAdapter;
    private int screenWidth;
    private VideoDetailBean videoDetailBean;
    private int currentPosition = 0;
    private int id;
    private int c_id;
    private AdBean adBean;
    private String v_fileid;
    private boolean showAd;
    private Timer timer;

    private BaseQuickAdapter<VideoSortBean, BaseViewHolder> anthologyAdapter1;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        if (!EventBus.getDefault().isRegistered(TVDetailActivity.this))
            EventBus.getDefault().register(TVDetailActivity.this);
        id = getIntent().getIntExtra("id", 0);
        c_id = getIntent().getIntExtra("c_id", 0);
        loadeAd();
        presenter.getTvAd(c_id);
        presenter.getRecommend(c_id);
    }

    private void loadeAd() {
        MiguLoginBean login = PreferenceManager.getInstance().getLogin();
        if (login.getTu_vip_status() == 0) {
            showAd = true;
            superPlayer.setShowAd(true);
            presenter.getAd(c_id);
        } else
            presenter.getDetail(id);
    }

    @Override
    protected void onDestroy() {
        if (superPlayer != null)
            superPlayer.resetPlayer();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        if (timer != null) timer.cancel();
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        superPlayer.onPause();
        if (videoDetailBean != null) {
            if (superPlayer.findViewById(R.id.adView).getVisibility() != View.VISIBLE) {
                videoDetailBean.setProgress(superPlayer.getProgress());
            }
            PlayHistoryManager.save(videoDetailBean, superPlayer.getPosition());
        }
    }

    @Override
    public void finish() {
        if (videoDetailBean != null) {
            if (superPlayer.findViewById(R.id.adView).getVisibility() != View.VISIBLE) {
                videoDetailBean.setProgress(superPlayer.getProgress());
            }
            PlayHistoryManager.save(videoDetailBean, superPlayer.getPosition());
        }
        super.finish();
    }

    @Override
    protected void initView() {
        presenter.attachView(this);
        screenWidth = ScreenUtils.getScreenSize(this)[0];
        initRecommendList();
        initAnthologyList();
//        ivAd1.setOnFocusChangeListener(this);
//        ivAd2.setOnFocusChangeListener(this);
        superPlayer.setRootId(R.id.rootView);
        superPlayer.setOnAnthologySelect(new TCVodAnthologyView.Callback() {
            @Override
            public void onAnthologySelect(int position) {
                if (position < videoDetailBean.getVideo().size()) {
                    currentPosition = position;
                    VideoBean item = anthologyAdapter.getItem(position);
                    v_fileid = item.getV_fileid();
                    videoDetailBean.setProgress(0);
                    startPlay();
                }
            }

            @Override
            public void hide() {

            }
        });
        superPlayer.setPlayEndCallback(new SuperPlayerView.PlayEndCallback() {
            @Override
            public void palyEnd(int position) {
                if (position < videoDetailBean.getVideo().size()) {
                    currentPosition = position;
                    VideoBean item = anthologyAdapter.getItem(position);
                    v_fileid = item.getV_fileid();
                    videoDetailBean.setProgress(0);
                    startPlay();
                }
            }
        });

        superPlayer.setVideoQualityCallback(new TCVodQualityView.VideoQualityCallback() {
            @Override
            public boolean onQualitySelect(TCVideoQuality quality) {
                if (quality.name.equals("HD")) {
                    if (isLogin()) {
                        return false;
                    } else {
                        return true;
                    }
                }
                if (quality.name.equals("2K") || quality.name.equals("4K")) {
                    if (isLogin()) {
                        MiguLoginBean login = PreferenceManager.getInstance().getLogin();
                        if (login.getTu_vip_status() == 1) {
                            return false;
                        } else {
                            startActivity(new Intent(TVDetailActivity.this, MiGuActivity.class));
                        }
                    } else {
                        return true;
                    }
                }
                return false;
            }
        });
        int defaultQuality = PreferenceManager.getInstance().getDefaultQuality();
        MiguLoginBean login = PreferenceManager.getInstance().getLogin();
        PreferenceManager.getInstance().getLogin();
        if (defaultQuality > 4) {
            if (login.getTu_vip_status() == 1) {
                superPlayer.setDefaultQualitySet(defaultQuality);
            }
        } else if (defaultQuality > 3) {
            superPlayer.setDefaultQualitySet(defaultQuality);
        } else {
            superPlayer.setDefaultQualitySet(defaultQuality);
        }
        superPlayer.requestFocus();
    }


    private void initAnthologyList() {
        int leftDivider = AutoSizeUtils.dp2px(this, 12);
        int itemDivider = (screenWidth - leftDivider - 10 * AutoSizeUtils.dp2px(this, 75)
                - AutoSizeUtils.dp2px(this, 73)) / 9;
        recyclerviewAnthogy.addItemDecoration(new AnthologyItemDecoration(leftDivider, itemDivider / 2));
        anthologyAdapter = new BaseQuickAdapter<VideoBean, BaseViewHolder>(R.layout.item_anthology) {
            @Override
            protected void convert(BaseViewHolder helper, VideoBean item) {
                int v_episode_num = item.getV_episode_num();
                helper.setText(R.id.num, String.valueOf(v_episode_num));
                if (anthologyAdapter.getData().indexOf(item) == currentPosition) {
                    helper.setVisible(R.id.play, true).setVisible(R.id.num, false);
                } else helper.setVisible(R.id.play, false).setVisible(R.id.num, true);
                if (videoDetailBean.getVm_is_pay() == 2) {
                    helper.setVisible(R.id.ic_vip, true);
                    if (item.getV_is_pay() == 2) {
                        helper.setImageResource(R.id.ic_vip, R.mipmap.ic_vip_d);
                    } else
                        helper.setImageResource(R.id.ic_vip, R.mipmap.ic_xian_mian);
                } else {
                    if (item.getV_is_pay() == 2) {
                        helper.setVisible(R.id.ic_vip, true).setImageResource(R.id.ic_vip, R.mipmap.ic_vip_d);
                    } else
                        helper.setVisible(R.id.ic_vip, false);
                }
            }
        };
        anthologyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int prePosition = currentPosition;
                currentPosition = position;
                VideoBean item = anthologyAdapter.getItem(position);
                if (item == null) return;
                v_fileid = item.getV_fileid();
                videoDetailBean.setProgress(0);
                startPlay();
                anthologyAdapter.notifyItemChanged(prePosition, "");
                anthologyAdapter.notifyItemChanged(currentPosition, "");
                anthologyAdapter1.notifyDataSetChanged();
            }
        });
        anthologyAdapter.setHasStableIds(true);
        recyclerviewAnthogy.setItemAnimator(null);
        recyclerviewAnthogy.setAdapter(anthologyAdapter);

        recyclerviewAnthogy1.addItemDecoration(new AnthologyItemDecoration(leftDivider, itemDivider / 2, leftDivider));
        anthologyAdapter1 = new BaseQuickAdapter<VideoSortBean, BaseViewHolder>(R.layout.item_anthology) {
            @Override
            protected void convert(BaseViewHolder helper, VideoSortBean item) {
                VideoBean item1 = anthologyAdapter.getItem(currentPosition);
                if (item1 != null) {
                    if (item.getMaxVideo().getV_episode_num() >= item1.getV_episode_num() &&
                            item.getMinVideo().getV_episode_num() <= item1.getV_episode_num()) {
                        helper.setBackgroundRes(R.id.item_anthology, R.drawable.anthology_bg);
                    } else {
                        helper.setBackgroundRes(R.id.item_anthology, R.drawable.history4_bg);
                    }
                }
                helper.setVisible(R.id.ic_vip, false)
                        .setText(R.id.num, item.getMinVideo().getV_episode_num() + "-" + item.getMaxVideo().getV_episode_num());
            }
        };
        anthologyAdapter1.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoSortBean item = anthologyAdapter1.getItem(position);
                if (item != null) {
                    int index = anthologyAdapter.getData().indexOf(item.getMinVideo());
                    LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerviewAnthogy.getLayoutManager();
                    if (linearLayoutManager != null) {
                        linearLayoutManager.scrollToPositionWithOffset(index, 0);
                    }
                }
            }
        });
        recyclerviewAnthogy1.setAdapter(anthologyAdapter1);
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
                if (item.getVm_update_status() == 2) {
                    helper.setText(R.id.endTime, getResources().getString(R.string.update_done));
                } else
                    helper.setText(R.id.endTime, String.format(getResources().getString(R.string.update_status), item.getCount()));
                if (item.getVm_is_pay() == 2) {
                    helper.setVisible(R.id.vip, true);
                } else {
                    helper.setVisible(R.id.vip, false);
                }
                GlideUtils.getRequest(TVDetailActivity.this, Util.convertImgPath(item.getVm_img())).placeholder(R.drawable.default_img)
                        .override(218, 280).centerCrop().into((ImageView) helper.getView(R.id.image));
            }
        };
        recommendMovieAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeChildRecommendBean.VideoBean item = recommendMovieAdapter.getItem(position);
                if (item == null) return;
                Intent intent;
                if (item.getVm_type() == 2) {
                    intent = new Intent(TVDetailActivity.this, TVDetailActivity.class);
                } else
                    intent = new Intent(TVDetailActivity.this, MovieDetailActivity.class);
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
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN && superPlayer.getAdView().getVisibility() == View.GONE) {
                superPlayer.showProgress(keyCode);
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN && superPlayer.getAdView().getVisibility() == View.GONE) {
                superPlayer.showProgress(keyCode);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        View currentFocus = getCurrentFocus();
        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN
                || keyCode == KeyEvent.KEYCODE_MENU) {
            if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                superPlayer.showTVMenu();
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
                return true;
            }
            if (currentFocus != null) {
                Intent intent;
                switch (currentFocus.getId()) {
                    case R.id.controller_small:
                        superPlayer.requestFullMode();
                        return true;
                    case R.id.adView:
                        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_WINDOW) {
                            superPlayer.requestFullMode();
                            return true;
                        }
                        intent = new Intent(TVDetailActivity.this, MiGuActivity.class);
                        startActivityForResult(intent, 100);
                        return true;
                    case R.id.vipTipView:
                    case R.id.buyAntholgyView:
                        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_WINDOW) {
                            superPlayer.requestFullMode();
                            return true;
                        }
                        intent = new Intent(TVDetailActivity.this, MiGuActivity.class);
                        startActivityForResult(intent, 101);
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
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        if (superPlayer.getPlayMode() == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            superPlayer.requestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
            superPlayer.requestFocus();
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
    protected void onResume() {
        super.onResume();
        if (superPlayer != null && superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE) {
            superPlayer.onResume();
        }
        if (timer == null) {
            timer = new Timer();
            timer.schedule(new UpdateTimeTask(this, titleView), 0, 1000);
        }
        titleView.updateTV_Vip();
    }

    private void startPlay() {
        superPlayer.setStartAndEndTime(PreferenceManager.getInstance().getIsJump(), videoDetailBean.getVm_opening_time(),
                videoDetailBean.getVm_ending_time());
        VideoBean videoBean = videoDetailBean.getVideo().get(currentPosition);
        if (videoBean.getV_is_pay() == 2) {
            if (PreferenceManager.getInstance().getLogin().getTu_vip_status() == 0) {
                superPlayer.showLoginTip(true);
                superPlayer.resetPlayer();
            } else {
                SuperPlayerModel model = new SuperPlayerModel();
                model.appId = Constant.PLAYID;// 配置 AppId
                model.videoId = new SuperPlayerVideoId();
                model.videoId.fileId = v_fileid; // 配置 FileId
                model.title = Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z());
                superPlayer.playWithModel(model);
            }
        } else if (showAd && adBean != null) {
            superPlayer.showLoginTip(false);
            superPlayer.setShowAd(true);
            superPlayer.setInfo(Constant.PLAYID, adBean.getVa_fileid(), v_fileid, Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z()));
        } else {
            superPlayer.setShowAd(false);
            superPlayer.showLoginTip(false);
            SuperPlayerModel model = new SuperPlayerModel();
            model.appId = Constant.PLAYID;// 配置 AppId
            model.videoId = new SuperPlayerVideoId();
            model.videoId.fileId = v_fileid; // 配置 FileId
            model.title = Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z());
            superPlayer.playWithModel(model);
        }
        anthologyAdapter.notifyDataSetChanged();
        anthologyAdapter1.notifyDataSetChanged();
    }

    @Override
    public void showDetail(VideoDetailBean videoDetailBean) {
        PlayHistoryInfo videoHistoryInfo = PlayHistoryManager.getHistory(videoDetailBean.getVm_id(), 1);
        if (videoHistoryInfo != null) {
            superPlayer.setCurrent(videoHistoryInfo.getB_progress());
            videoDetailBean.setProgress(videoHistoryInfo.getB_progress());
        }
        currentPosition = videoHistoryInfo == null ? 0 : videoHistoryInfo.getB_position();
        //不开防盗链
        this.videoDetailBean = videoDetailBean;
        List<VideoBean> video = videoDetailBean.getVideo();
        anthologyAdapter.setNewData(video);
        int num = 0;
        VideoSortBean videoSortBean = null;

//        superPlayer.setStartAndEndTime(PreferenceManager.getInstance().getIsJump(), videoDetailBean.getVm_opening_time(),
//                videoDetailBean.getVm_ending_time());
        ArrayList<VideoSortBean> videoSortBeans = new ArrayList<>();
        if (video != null && !video.isEmpty())
            for (VideoBean videoBean : video) {
                if (num == 0) {
                    videoSortBean = new VideoSortBean();
                    videoSortBean.setMinVideo(videoBean);
                    videoSortBean.setMaxVideo(videoBean);
                    videoSortBeans.add(videoSortBean);
                    num++;
                } else if (num == 9) {
                    videoSortBean.setMaxVideo(videoBean);
                    num = 0;
                } else {
                    videoSortBean.setMaxVideo(videoBean);
                    num++;
                }
            }
        anthologyAdapter1.setNewData(videoSortBeans);
        String titleStr = Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z());
        if (!videoDetailBean.getVideo().isEmpty()) {
            superPlayer.setAnthologyData(videoDetailBean.getVideo());
            superPlayer.setIsNeedVip(videoDetailBean.getVm_is_pay() == 2);
            v_fileid = videoDetailBean.getVideo().get(currentPosition).getV_fileid();
            startPlay();
        }
        superPlayer.isDefaultLanguage(PreferenceManager.getInstance().isDefaultLanguage());
        title.setText(titleStr);
        String des = Util.showText(videoDetailBean.getVm_des(), videoDetailBean.getVm_des_z());
        TextPaint paint = introl.getPaint();
        float v = paint.measureText(des);
        introl.measure(0, 0);
        if (v <= 2 * introl.getMeasuredWidth()) {
            more.setVisibility(View.INVISIBLE);
        }
        introl.setText(des);
        icVip.setVisibility(videoDetailBean.getVm_is_pay() == 2 ? View.VISIBLE : View.GONE);//2付费显示vip
        isVip.setVisibility(videoDetailBean.getVm_is_pay() == 2 ? View.VISIBLE : View.GONE);//2付费显示vip
        if (!videoDetailBean.getVideo().isEmpty()) {
            VideoBean videoBean = videoDetailBean.getVideo().get(videoDetailBean.getVideo().size() - 1);
            if (videoDetailBean.getVm_update_status() == 2) {
                anthogyNum.setText(String.format(getResources().getString(R.string.sum_video), videoBean.getV_episode_num()));
            } else {
                anthogyNum.setText(String.format(getResources().getString(R.string.update_status), videoBean.getV_episode_num()));
            }
        }
        collect.setChecked(videoDetailBean.getIs_colle_status() == 1);
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerviewAnthogy.getLayoutManager();
        if (linearLayoutManager != null) {
            linearLayoutManager.scrollToPositionWithOffset(currentPosition, 0);
        }
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
    public void getPayResult() {

    }

    @Override
    public void showAdError(String msg) {
        ToastUtil.showText(msg);
        presenter.getDetail(id);
    }

    @Override
    public void showTVAd(List<VideoAdBean> videoAdBeans) {
        if (!videoAdBeans.isEmpty()) {
            ivAd1.setVisibility(View.VISIBLE);
            ivAd2.setVisibility(View.VISIBLE);

            for (int i = 0; i < videoAdBeans.size(); i++) {
                if (videoAdBeans.get(i).getTa_type1() == 1) {
                    GlideUtils.getRequest(this, Util.convertImgPath(videoAdBeans.get(i).getTa_img()))
                            .centerCrop().into(ivAd1);
                }
                if (videoAdBeans.get(i).getTa_type1() == 2) {
                    GlideUtils.getRequest(this, Util.convertImgPath(videoAdBeans.get(i).getTa_img()))
                            .centerCrop().into(ivAd2);
                }
            }

        }
    }

    @Override
    public void showOrder(OrderBean result) {

    }

    @Override
    public void showPayCode(String result) {

    }

    @Override
    public void showPayResult(boolean payResult, String msg) {

    }

    @Override
    public void AuthenticationFail(BodyBean productToOrderList) {

    }

    @Override
    public void showNetError(String msg) {

    }

    @Override
    public void showRechargeBeans(List<TVProductBean> result, String accountIdentify) {

    }

    @Override
    public void showMiguError(String resultDesc) {

    }

    @Override
    public void showAd(AdBean adBean) {
        this.adBean = adBean;
        presenter.getDetail(id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        ToastUtil.showText("openVipSuccess");
        if (requestCode == 100) {
            PreferenceManager.getInstance().getLogin();
            if (PreferenceManager.getInstance().getLogin().getTu_vip_status() == 1) {
                openVipSuccess();
            } else {
                ToastUtil.showText(getString(R.string.not_open_vip));
            }
        }
        if (requestCode == 101) {
            PreferenceManager.getInstance().getLogin();
            if (PreferenceManager.getInstance().getLogin().getTu_vip_status() == 1) {
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
        SuperPlayerModel superPlayerModel = new SuperPlayerModel();
        superPlayerModel.appId = Constant.PLAYID;
        superPlayerModel.videoId = new SuperPlayerVideoId();
        superPlayerModel.videoId.fileId = videoDetailBean.getVideo().get(currentPosition).getV_fileid();
        superPlayerModel.title = Util.showText(videoDetailBean.getVm_title(), videoDetailBean.getVm_title_z());
        superPlayer.playWithModel(superPlayerModel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetMessage(MiguLoginBean message) {
        if (message.getTu_vip_status() == 1) {
            superPlayer.showLoginTip(false);
            openVipSuccess();
        } else {
            ToastUtil.showText(getString(R.string.not_open_vip));
        }
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
                if (videoDetailBean != null && isClickLogin()) {
                    if (videoDetailBean.getIs_colle_status() == 1) {
                        presenter.unCollect(videoDetailBean.getUc_id());
                    } else
                        presenter.collect(videoDetailBean.getVm_id());
                }
                break;
        }
    }

    private void tvAdClick(VideoAdBean videoAdBean) {
        Intent intent = null;
        switch (videoAdBean.getTa_type()) {
            case 1:
                if (videoAdBean.getVm_type() == 2)
                    intent = new Intent(TVDetailActivity.this, TVDetailActivity.class);
                else intent = new Intent(TVDetailActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                intent.putExtra("c_id", videoAdBean.getVt_id_one());
                break;
            case 2:
                intent = new Intent(TVDetailActivity.this, ShowDubbingVideoActivity.class);
                intent.putExtra("id", Integer.valueOf(videoAdBean.getTa_url()));
                break;
            case 3:
                intent = new Intent(TVDetailActivity.this, WebViewActivity.class);
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
}
