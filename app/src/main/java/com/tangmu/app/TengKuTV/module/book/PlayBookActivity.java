package com.tangmu.app.TengKuTV.module.book;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.tangmu.app.TengKuTV.Constant;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.base.BaseResponse;
import com.tangmu.app.TengKuTV.bean.AdBean;
import com.tangmu.app.TengKuTV.bean.BookDetailDataBean;
import com.tangmu.app.TengKuTV.bean.LoginBean;
import com.tangmu.app.TengKuTV.bean.MiguLoginBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.db.PlayHistoryInfo;
import com.tangmu.app.TengKuTV.db.PlayHistoryManager;
import com.tangmu.app.TengKuTV.utils.JsonCallback;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.TopMiddleDecoration;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.SuperBookPlayerView;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerModel;
import com.tencent.liteav.demo.play.SuperPlayerVideoId;

import butterknife.BindView;
import me.jessyan.autosize.utils.AutoSizeUtils;
import me.jessyan.autosize.utils.ScreenUtils;

public class PlayBookActivity extends BaseActivity implements View.OnFocusChangeListener, View.OnClickListener {


    @BindView(R.id.anthologyList)
    RecyclerView anthologyList;
    @BindView(R.id.superPlayer)
    SuperBookPlayerView superPlayer;
    private BaseQuickAdapter<BookDetailDataBean.BookDetailBean, BaseViewHolder> anthologyAdapter;
    private BookDetailDataBean bookDetailDataBean;
    private SuperPlayerModel superPlayerModel;
    private int currentPosition = 0;
    private AdBean adBean;
    private boolean showAd = true;
    private ImageView ivCollect;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        getDetail(getIntent().getIntExtra("id", 0));
    }

    private void getAd(int p_id) {
        OkGo.<BaseResponse<AdBean>>post(Constant.IP + Constant.videoAd)
                .tag(this)
                .params("type", 2)
                .params("p1_id", p_id)
                .execute(new JsonCallback<BaseResponse<AdBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<AdBean>> response) {
                        if (response.body().getStatus() == 0) {
                            adBean = response.body().getResult();
                            if (adBean!=null)
                                superPlayer.setBookAd(adBean.getVa_url());
                        } else {
                            showAd = false;
                        }
                        startPlay();
                    }

                    @Override
                    public void onError(Response<BaseResponse<AdBean>> response) {
                        super.onError(response);
                        showAd = false;
                        startPlay();
                    }
                });
    }

    @Override
    public void finish() {
        if (bookDetailDataBean != null) {
            bookDetailDataBean.setProgress(superPlayer.getProgress());
            PlayHistoryManager.save(bookDetailDataBean, currentPosition);
        }
        super.finish();
    }

    @Override
    protected void initView() {
        initAnthologyList();
        superPlayer.setBookSelectedCallback(new SuperBookPlayerView.BookSelectedCallback() {
            @Override
            public void onBookSelected(int position) {
                int prePosition = currentPosition;
                currentPosition = position;
                anthologyAdapter.notifyItemChanged(prePosition, "");
                anthologyAdapter.notifyItemChanged(currentPosition, "");
            }
        });
        ivCollect = superPlayer.findViewById(R.id.collect);
        ivCollect.setOnClickListener(this);
    }

    private void initAnthologyList() {
        anthologyList.addItemDecoration(new TopMiddleDecoration(AutoSizeUtils.dp2px(this, 31)));
        anthologyAdapter = new BaseQuickAdapter<BookDetailDataBean.BookDetailBean, BaseViewHolder>(R.layout.item_book_anthology) {
            @Override
            protected void convert(BaseViewHolder helper, BookDetailDataBean.BookDetailBean item) {
                CheckedTextView tvTitle = helper.getView(R.id.title);
                tvTitle.setOnFocusChangeListener(PlayBookActivity.this);
                tvTitle.setText(Util.showText(bookDetailDataBean.getB_title() + "—" + item.getBd_name(), bookDetailDataBean.getB_title_z() + "—" + item.getBd_name_z()));
                helper.setText(R.id.index, String.valueOf(item.getBd_number()));
                if (currentPosition == bookDetailDataBean.getBook_detail().indexOf(item)) {
                    tvTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                    tvTitle.setChecked(true);
                    helper.setVisible(R.id.index, false);
                } else {
                    tvTitle.setEllipsize(TextUtils.TruncateAt.END);
                    tvTitle.setChecked(false);
                    helper.setVisible(R.id.index, true);
                }
                helper.setNestView(R.id.title);
            }
        };
        anthologyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BookDetailDataBean.BookDetailBean item = anthologyAdapter.getItem(position);
                if (item == null) return;
                if (showAd && adBean != null) {
                    superPlayerModel.url = Util.convertVideoPath(adBean.getVa_url());
                } else {
                    superPlayerModel.videoId = new SuperPlayerVideoId();
                    superPlayerModel.videoId.fileId = item.getBd_fileid(); // 配置 FileId
                }
                superPlayer.playWithModel(superPlayerModel);
                int prePosition = currentPosition;
                currentPosition = position;
                anthologyAdapter.notifyItemChanged(prePosition, "");
                anthologyAdapter.notifyItemChanged(currentPosition, "");
            }
        });
        anthologyList.setAdapter(anthologyAdapter);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_play_book;
    }


    @Override
    protected void onPause() {
        super.onPause();
        superPlayer.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (superPlayer.getPlayState() == SuperPlayerConst.PLAYSTATE_PAUSE) {
            superPlayer.onResume();
        }
    }

    public void getDetail(int b_id) {
        PostRequest<BaseResponse<BookDetailDataBean>> postRequest = OkGo.<BaseResponse<BookDetailDataBean>>post(Constant.IP + Constant.bookDetail)
                .params("b_id", b_id);
        if (PreferenceManager.getInstance().getLogin() != null) {
            postRequest.params("u_id", PreferenceManager.getInstance().getLogin().getU_id());
        }
        postRequest.tag(this)
                .execute(new JsonCallback<BaseResponse<BookDetailDataBean>>() {
                    @Override
                    public void onSuccess(Response<BaseResponse<BookDetailDataBean>> response) {
                        if (response.body().getStatus() == 0) {
                            BookDetailDataBean result = response.body().getResult();
                            showBook(result);
                            MiguLoginBean login = PreferenceManager.getInstance().getLogin();
                            if (login != null) {
                                if (login.getTu_vip_status() == 1) {
                                    showAd = false;
                                    startPlay();
                                }else {
                                    getAd(result.getVt_id_one());
                                }
                            } else {
                                getAd(result.getVt_id_one());
                            }
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<BookDetailDataBean>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (bookDetailDataBean != null) {
            bookDetailDataBean.setProgress(superPlayer.getProgress());
            PlayHistoryManager.save(bookDetailDataBean, currentPosition);
        }
        super.onBackPressed();
    }

    private void showBook(BookDetailDataBean bookDetailDataBean) {
        this.bookDetailDataBean = bookDetailDataBean;
        PlayHistoryInfo book = PlayHistoryManager.getHistory(bookDetailDataBean.getB_id(), 2);
        if (book != null) {
            int b_progress = book.getB_progress();
            superPlayer.setCurrent(b_progress);
            currentPosition = book.getB_position();
        }
        superPlayer.setInfo(Util.showText(bookDetailDataBean.getB_title(), bookDetailDataBean.getB_title_z()),
                Util.showText(bookDetailDataBean.getB_author(), bookDetailDataBean.getB_author())
                , Util.showText(bookDetailDataBean.getB_des(), bookDetailDataBean.getB_des_z()));
        superPlayer.setData(bookDetailDataBean.getBook_detail(), currentPosition);
        superPlayer.setCover(Util.convertImgPath(bookDetailDataBean.getB_img()));
        anthologyAdapter.setNewData(bookDetailDataBean.getBook_detail());
        if (bookDetailDataBean.getIs_collec_status() == 1)
            ivCollect.setImageResource(R.mipmap.ic_book_clollected);

        anthologyList.postDelayed(new Runnable() {
            @Override
            public void run() {
                View viewByPosition = anthologyAdapter.getViewByPosition(anthologyList, currentPosition, R.id.title);
                if (viewByPosition != null) viewByPosition.requestFocus();
            }
        }, 100);
        superPlayer.findViewById(R.id.seekbar_progress).setFocusable(true);
    }

    private void startPlay() {
        if (superPlayerModel == null)
            superPlayerModel = new SuperPlayerModel();
        superPlayerModel.appId = Constant.PLAYID;// 配置 AppId
        superPlayerModel.title = Util.showText(bookDetailDataBean.getB_title(), bookDetailDataBean.getB_title_z());
        if (showAd && adBean != null) {
            superPlayerModel.url = Util.convertVideoPath(adBean.getVa_url());
        } else {
            superPlayerModel.videoId = new SuperPlayerVideoId();
            superPlayerModel.videoId.fileId = bookDetailDataBean.getBook_detail().get(currentPosition).getBd_fileid(); // 配置 FileId
        }
        superPlayer.playWithModel(superPlayerModel);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            int[] amount = getScrollAmount(v);//计算需要滑动的距离
            anthologyList.scrollBy(0, amount[1]);
        }
    }

    /**
     * 计算需要滑动的距离,使焦点在滑动中始终居中
     *
     * @param view
     */
    private int[] getScrollAmount(View view) {
        int[] out = new int[2];
        view.getLocationOnScreen(out);
        int i = ScreenUtils.getScreenSize(view.getContext())[1];
        out[1] = out[1] - i / 2;
        return out;
    }


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            View currentFocus = getCurrentFocus();
            if (currentFocus != null && currentFocus.getId() == R.id.seekbar_progress) {
                superPlayer.showProgress(keyCode);
                return true;
            }
        }
        return super.onKeyLongPress(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT || keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            View currentFocus = getCurrentFocus();
            if (currentFocus != null && currentFocus.getId() == R.id.seekbar_progress) {
                superPlayer.showProgress(keyCode);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        if (bookDetailDataBean != null && isClickLogin()) {
            if (bookDetailDataBean.getIs_collec_status() == 0) {
                collectBook(bookDetailDataBean.getB_id());
            } else unCollectBook(bookDetailDataBean.getUc_id());
        }
    }

    private void collectBook(int b_id) {
        OkGo.<BaseResponse<Integer>>post(Constant.IP + Constant.collect)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("audio_id", b_id)
                .params("type", 3)
                .tag(this)
                .execute(new JsonCallback<BaseResponse<Integer>>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse<Integer>> response) {
                        if (response.body().getStatus() == 0) {
                            bookDetailDataBean.setIs_collec_status(1);
                            ivCollect.setImageResource(R.mipmap.ic_book_clollected);
                            bookDetailDataBean.setUc_id(response.body().getResult());
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse<Integer>> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    private void unCollectBook(int uc_id) {
        OkGo.<BaseResponse>post(Constant.IP + Constant.unCollect)
                .params("token", PreferenceManager.getInstance().getToken())
                .params("uc_id", uc_id)
                .tag(this)
                .execute(new JsonCallback<BaseResponse>() {
                    @Override
                    public void onVerifySuccess(Response<BaseResponse> response) {
                        if (response.body().getStatus() == 0) {
                            bookDetailDataBean.setIs_collec_status(0);
                            ivCollect.setImageResource(R.mipmap.ic_book_unclollect);
                        } else {
                            ToastUtil.showText(response.body().getMsg());
                        }

                    }

                    @Override
                    public void onError(Response<BaseResponse> response) {
                        super.onError(response);
                        ToastUtil.showText(handleError(response.getException()));
                    }
                });
    }

    @Override
    protected void onDestroy() {
        OkGo.getInstance().cancelTag(this);
        if (superPlayer != null)
            superPlayer.resetPlayer();
        super.onDestroy();
    }
}
