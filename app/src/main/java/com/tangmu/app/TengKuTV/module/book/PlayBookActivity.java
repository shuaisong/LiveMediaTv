package com.tangmu.app.TengKuTV.module.book;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.CheckedTextView;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
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
import com.tangmu.app.TengKuTV.bean.BookDetailDataBean;
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

public class PlayBookActivity extends BaseActivity implements View.OnFocusChangeListener {


    @BindView(R.id.anthologyList)
    RecyclerView anthologyList;
    @BindView(R.id.superPlayer)
    SuperBookPlayerView superPlayer;
    private BaseQuickAdapter<BookDetailDataBean.BookDetailBean, BaseViewHolder> anthologyAdapter;
    private BookDetailDataBean bookDetailDataBean;
    private SuperPlayerModel superPlayerModel;
    private int currentPosition = 0;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {

    }

    @Override
    protected void initData() {
        getDetail(getIntent().getIntExtra("id", 0));
    }

    @Override
    public void finish() {
        if (bookDetailDataBean != null) {
            bookDetailDataBean.setProgress(superPlayer.getProgress());
            PlayHistoryManager.save(bookDetailDataBean, superPlayer.getPosition());
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
                superPlayerModel.videoId.fileId = item.getBd_fileid(); // 配置 FileId
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
                            showBook(response.body().getResult());
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

    private void showBook(BookDetailDataBean bookDetailDataBean) {
        this.bookDetailDataBean = bookDetailDataBean;
        PlayHistoryInfo book = PlayHistoryManager.getHistory(bookDetailDataBean.getB_id(), 2);
        if (book != null) {
            currentPosition = book.getB_position();
        }
        superPlayerModel = new SuperPlayerModel();
        superPlayerModel.appId = Constant.PLAYID;// 配置 AppId
        superPlayerModel.videoId = new SuperPlayerVideoId();
        superPlayerModel.title = Util.showText(bookDetailDataBean.getB_title(), bookDetailDataBean.getB_title_z());
        superPlayerModel.videoId.fileId = bookDetailDataBean.getBook_detail().get(currentPosition).getBd_fileid(); // 配置 FileId
        superPlayer.playWithModel(superPlayerModel);
        superPlayer.setInfo(Util.showText(bookDetailDataBean.getB_title(), bookDetailDataBean.getB_title_z()),
                Util.showText(bookDetailDataBean.getB_author(), bookDetailDataBean.getB_author())
                , Util.showText(bookDetailDataBean.getB_des(), bookDetailDataBean.getB_des_z()));
        superPlayer.setData(bookDetailDataBean.getBook_detail(), currentPosition);
        superPlayer.setCover(Util.convertImgPath(bookDetailDataBean.getB_img()));
        anthologyAdapter.setNewData(bookDetailDataBean.getBook_detail());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        View currentFocus = getCurrentFocus();
        if (currentFocus != null) {
            LogUtil.e(currentFocus.toString());
        }
        return super.onKeyDown(keyCode, event);
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
}
