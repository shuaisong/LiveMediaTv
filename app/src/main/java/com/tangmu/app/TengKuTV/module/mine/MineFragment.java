package com.tangmu.app.TengKuTV.module.mine;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.adapter.CollectAdapter;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.base.BaseListResponse;
import com.tangmu.app.TengKuTV.bean.CollectBean;
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.db.PlayHistoryInfo;
import com.tangmu.app.TengKuTV.db.PlayHistoryManager;
import com.tangmu.app.TengKuTV.module.book.PlayBookActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.module.playhistory.PlayHistoryActivity;
import com.tangmu.app.TengKuTV.module.vip.MiGuActivity;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.HLoadMoreView;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.LogUtils;
import me.jessyan.autosize.utils.ScreenUtils;

/**
 * Created by lenovo on 2020/2/21.
 * auther:lenovo
 * Date：2020/2/21
 */
public class MineFragment extends BaseFragment {

    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.nickName)
    TextView nickName;
    @BindView(R.id.exprie_time)
    TextView exprieTime;
    @BindView(R.id.login_tv)
    TextView loginTv;
    @BindView(R.id.login_tip)
    TextView loginTip;
    @BindView(R.id.open_vip)
    TextView openVip;
    @BindView(R.id.history)
    TextView history;
    @BindView(R.id.collect)
    TextView collect;
    @BindView(R.id.setting)
    TextView setting;
    @BindView(R.id.tv_play_record)
    TextView tvPlayRecord;
    @BindView(R.id.recyclerview_record)
    RecyclerView recyclerviewRecord;
    @BindView(R.id.tv_collect)
    TextView tvCollect;
    @BindView(R.id.video_collect)
    RecyclerView videoCollect;
    @BindView(R.id.book_collect)
    RecyclerView bookCollect;
    @BindView(R.id.dubbing_collect)
    RecyclerView dubbingCollect;
    private CollectAdapter videoCollectAdapter;
    private CollectAdapter bookCollectAdapter;
    private CollectAdapter dubbingCollectAdapter;
    private int bookPage = 1;
    private int videoPage = 1;
    private int dubbingPage = 1;

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        if (isLogin()) {
            getCollect();
        }
    }

    private void getCollect() {
        getCollect(videoPage, 1);
        getCollect(bookPage, 3);
        getCollect(dubbingPage, 4);
    }

    private void getCollect(int page, int type) {
        OkGo.<BaseListResponse<CollectBean>>post(Constant.IP + Constant.collectionList)
                .tag(this)
                .params("type", type)
                .params("page", page)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("size", 20)
                .execute(new JsonCallback<BaseListResponse<CollectBean>>() {
                    @Override
                    public void onVerifySuccess(Response<BaseListResponse<CollectBean>> response) {
                        if (response.body().getStatus() == 0) {
                            showCollect(type, page, response.body().getResult());
                        }
                    }

                    @Override
                    public void onError(Response<BaseListResponse<CollectBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    private void showCollect(int type, int page, List<CollectBean> result) {
        switch (type) {
            case 1:
                if (videoPage == 1 && result.isEmpty()) {
                    videoCollect.setFocusable(false);
                } else videoCollect.setFocusable(true);
                setCollectData(videoCollectAdapter, page, result);
                videoPage++;
                break;
            case 3:
                if (bookPage == 1 && result.isEmpty()) {
                    bookCollect.setFocusable(false);
                } else bookCollect.setFocusable(true);
                setCollectData(bookCollectAdapter, page, result);
                bookPage++;
                break;
            case 4:
                if (dubbingPage == 1 && result.isEmpty()) {
                    dubbingCollect.setFocusable(false);
                } else dubbingCollect.setFocusable(true);
                setCollectData(dubbingCollectAdapter, page, result);
                dubbingPage++;
                break;
        }
    }

    private void setCollectData(CollectAdapter collectAdapter, int page, List<CollectBean> result) {
        if (page == 1) {
            collectAdapter.setNewData(result);
        } else {
            collectAdapter.getData().addAll(result);
            if (result.size() < 20) {
                collectAdapter.loadMoreEnd();
            } else collectAdapter.loadMoreComplete();
        }
    }

    /**
     * 初始化view
     */
    @Override
    protected void initView() {
        initRecordList();
        initCollectList();
        loginTv.requestFocus();
    }

    private void initCollectList() {
        videoCollectAdapter = new CollectAdapter();
        HLoadMoreView customLoadMoreView = new HLoadMoreView();
        customLoadMoreView.setEndVisible(false);
        videoCollectAdapter.setLoadMoreView(customLoadMoreView);
        videoCollectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getCollect(videoPage, 1);
            }
        }, videoCollect);
        videoCollectAdapter.setFocusChangeListener(new MineCollectFocusChangeListener(videoCollect));
        videoCollect.setAdapter(videoCollectAdapter);

        bookCollectAdapter = new CollectAdapter();
        customLoadMoreView = new HLoadMoreView();
        customLoadMoreView.setEndVisible(false);
        bookCollectAdapter.setLoadMoreView(customLoadMoreView);
        bookCollectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getCollect(bookPage, 3);
            }
        }, bookCollect);
        bookCollectAdapter.setFocusChangeListener(new MineCollectFocusChangeListener(bookCollect));
        bookCollect.setAdapter(bookCollectAdapter);

        dubbingCollectAdapter = new CollectAdapter();
        customLoadMoreView = new HLoadMoreView();
        customLoadMoreView.setEndVisible(false);
        dubbingCollectAdapter.setLoadMoreView(customLoadMoreView);
        dubbingCollectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getCollect(dubbingPage, 4);
            }
        }, dubbingCollect);
        dubbingCollectAdapter.setFocusChangeListener(new MineCollectFocusChangeListener(dubbingCollect));
        dubbingCollect.setAdapter(dubbingCollectAdapter);
    }

    private void initRecordList() {
        List<PlayHistoryInfo> playHistoryInfos = PlayHistoryManager.getAll();
        int i = AutoSizeUtils.dp2px(getContext(), 175);
        int itemWidth = (ScreenUtils.getScreenSize(getContext())[0] - i) / 5;
        BaseQuickAdapter<PlayHistoryInfo, BaseViewHolder> recordAdapter =
                new BaseQuickAdapter<PlayHistoryInfo, BaseViewHolder>(R.layout.item_record, playHistoryInfos) {
                    @Override
                    protected void convert(BaseViewHolder helper, PlayHistoryInfo item) {
                        ViewGroup.LayoutParams layoutParams = helper.itemView.getLayoutParams();
                        layoutParams.width = itemWidth;
                        helper.itemView.setLayoutParams(layoutParams);
                        helper.itemView.setOnFocusChangeListener(new MineCollectFocusChangeListener(recyclerviewRecord));
                        helper.setText(R.id.title, Util.showText(item.getB_title(), item.getB_title_z()));
                        GlideUtils.getRequest(getActivity(), Util.convertImgPath(item.getB_img()))
                                .override(167,255).centerCrop().into((ImageView) helper.getView(R.id.image));
                        if (item.getVm_type() == 1) {
                            helper.setImageResource(R.id.isVip, R.mipmap.icon_fufei);
                        } else {
                            helper.setImageResource(R.id.isVip, R.mipmap.vip_tag_bg);
                        }
                        if (item.getIs_vip() == 2) {
                            helper.setVisible(R.id.isVip, true);
                        } else {
                            helper.setVisible(R.id.isVip, false);
                        }
                        if (item.getUpdate_status() == 2) {
                            helper.setText(R.id.update_status, mContext.getResources().getString(R.string.update_done));
                        } else if (item.getUpdate_status() == 1)
                            helper.setText(R.id.update_status, String.format(mContext.getResources().getString(R.string.update_status), item.getUpdate_num()));

                    }
                };
        recordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PlayHistoryInfo item = recordAdapter.getItem(position);
                if (item != null) {
                    Intent intent;
                    if (item.getB_type() == 1) {//影音
                        if (item.getVm_type() == 2) {
                            intent = new Intent(getActivity(), TVDetailActivity.class);
                        } else
                            intent = new Intent(getActivity(), MovieDetailActivity.class);
                        intent.putExtra("c_id", item.getB_id_one());
                    } else {
                        intent = new Intent(getActivity(), PlayBookActivity.class);
                    }
                    intent.putExtra("id", item.getB_id());
                    startActivity(intent);

                }
            }
        });
        recyclerviewRecord.setAdapter(recordAdapter);
    }

    /**
     * @return xml id
     */
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_mine;
    }

    /**
     * mvp 关联
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupFragComponent(AppComponent appComponent) {

    }


    @Override
    public void onResume() {
        super.onResume();
        setInfo();
    }

    private void setInfo() {
        MiguLoginBean login = PreferenceManager.getInstance().getLogin();
        if (login != null) {
            loginTv.setText(getString(R.string.tv_exit));
            nickName.setText(PreferenceManager.getInstance().getUserName());
            if (login.getTu_vip_status() == 1) {
                exprieTime.setVisibility(View.VISIBLE);

                    String s = Util.addTimeYear(login.getTu_vip_expire());
                    LogUtils.e(s);
                    boolean dateOneBigger = Util.isDateOneBigger(s, TextUtils.isEmpty(login.getTu_vip_expire())? Util.getTime():login.getTu_vip_expire());
                    if (dateOneBigger) {
                        exprieTime.setText(String.format("%s%s", getString(R.string.exprie_time), s)
                        );
                    } else {
                        exprieTime.setText(String.format("%s%s", getString(R.string.exprie_time),
                                login.getTu_vip_expire())
                        );
                    }

                openVip.setText(getString(R.string.vip_show));
            } else {
                exprieTime.setVisibility(View.INVISIBLE);
                openVip.setText(getString(R.string.open_video_vip));
            }
            getinfo();

        } else {
//            head.setImageResource(R.mipmap.default_head);
            loginTv.setText(getString(R.string.login));
            exprieTime.setVisibility(View.INVISIBLE);
            nickName.setText(getString(R.string.login_tip));
            loginTip.setText(getString(R.string.login_support_tip));
        }
    }

    private void getinfo() {
        // TODO: 2021/10/7
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            setInfo();
            videoPage = 1;
            bookPage = 1;
            dubbingPage = 1;
            getCollect();
        }
    }

    @OnClick({R.id.history, R.id.collect, R.id.setting, R.id.login_tv, R.id.open_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.history:
                startActivity(new Intent(getActivity(), PlayHistoryActivity.class));
                break;
            case R.id.open_vip:
                if (PreferenceManager.getInstance().getLogin().getTu_vip_status()==0)
                    startActivityForResult(new Intent(getActivity(), MiGuActivity.class), 1001);
                break;
            case R.id.login_tv:
                    PreferenceManager.getInstance().exit();
                    setInfo();
                    if (getActivity() != null)
                        ((MineActivity) getActivity()).updateTitle();
                    videoCollectAdapter.getData().clear();
                    videoCollectAdapter.notifyDataSetChanged();
                    bookCollectAdapter.getData().clear();
                    bookCollectAdapter.notifyDataSetChanged();
                    dubbingCollectAdapter.getData().clear();
                    dubbingCollectAdapter.notifyDataSetChanged();
                break;
            case R.id.collect:
                if (isClickLogin()) {
                    Intent intent = new Intent(getActivity(), PlayHistoryActivity.class);
                    intent.putExtra("position", 1);
                    startActivity(intent);
                }
                break;
            case R.id.setting:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
        }
    }
}
