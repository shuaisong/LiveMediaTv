package com.tangmu.app.TengKuTV.module.search;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseActivity;
import com.tangmu.app.TengKuTV.bean.HomeChildRecommendBean;
import com.tangmu.app.TengKuTV.bean.T9KeyBean;
import com.tangmu.app.TengKuTV.bean.VideoRecmendSeachBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerActivityComponent;
import com.tangmu.app.TengKuTV.contact.VideoSearchContact;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.presenter.VideoSearchPresenter;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.LogUtil;
import com.tangmu.app.TengKuTV.utils.PreferenceManager;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoSearchActivity extends BaseActivity implements VideoSearchContact.View, View.OnFocusChangeListener, RadioGroup.OnCheckedChangeListener {
    @Inject
    VideoSearchPresenter presenter;
    @BindView(R.id.tv_search)
    EditText tvSearch;
    @BindView(R.id.key_clear)
    TextView keyClear;
    @BindView(R.id.key_back)
    TextView keyBack;
    @BindView(R.id.full_key)
    RecyclerView fullKey;
    @BindView(R.id.t9_key)
    RecyclerView t9Key;
    @BindView(R.id.radio_full)
    RadioButton radioFull;
    @BindView(R.id.radio_t9)
    RadioButton radioT9;
    @BindView(R.id.radio_key)
    RadioGroup radioKey;
    @BindView(R.id.hot_recycler)
    RecyclerView hotRecycler;
    @BindView(R.id.line_recommend)
    LinearLayout lineRecommend;
    @BindView(R.id.search_result)
    RecyclerView searchResult;
    @BindView(R.id.swip)
    SwipeRefreshLayout swipeRefreshLayout;
    private int currentT9KeyPosition = -1;
    private BaseQuickAdapter<T9KeyBean, BaseViewHolder> t9KeyAdapter;
    private BaseQuickAdapter<VideoRecmendSeachBean, BaseViewHolder> hotAdapter;
    private BaseQuickAdapter<HomeChildRecommendBean.VideoBean, BaseViewHolder> resultAdapter;

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerActivityComponent.builder().appComponent(appComponent).build().inject(this);
    }

    @Override
    protected void initData() {
        presenter.getVideoRecommend();
        tvSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    swipeRefreshLayout.setRefreshing(true);
                    presenter.getVideos(s.toString());
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    protected void initView() {
        presenter.attachView(this);
        initFullKey();
        initT9Key();
        initHotList();
        initResult();
        tvSearch.setInputType(InputType.TYPE_NULL);
        radioKey.setOnCheckedChangeListener(this);
        radioT9.setOnFocusChangeListener(this);
        radioFull.setOnFocusChangeListener(this);
        if (PreferenceManager.getInstance().isDefaultLanguage()) {
            String string = getString(R.string.search_hint);
            SpannableString spannableString = new SpannableString(string);
            ForegroundColorSpan colorSpan_white = new ForegroundColorSpan(Color.WHITE);
            ForegroundColorSpan colorSpan_white1 = new ForegroundColorSpan(Color.WHITE);
            spannableString.setSpan(colorSpan_white, 6, 9, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            spannableString.setSpan(colorSpan_white1, 10, spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tvSearch.setHint(spannableString);
        }
        swipeRefreshLayout.setEnabled(false);
    }

    private void initResult() {
        resultAdapter = new BaseQuickAdapter<HomeChildRecommendBean.VideoBean, BaseViewHolder>(R.layout.item_movie_list) {
            @Override
            protected void convert(BaseViewHolder helper, HomeChildRecommendBean.VideoBean item) {
                helper.setText(R.id.title, Util.showText(item.getVm_title(), item.getVm_title_z()))
                        .setGone(R.id.vip, item.getVm_is_pay() == 2);
                GlideUtils.getRequest(VideoSearchActivity.this, Util.convertImgPath(item.getVm_img())).placeholder(R.mipmap.img_default)
                        .centerCrop().into((ImageView) helper.getView(R.id.image));
                if (item.getVm_update_status() == 2) {
                    helper.setText(R.id.update_status, getResources().getString(R.string.update_done));
                } else if (item.getVm_update_status() == 1)
                    helper.setText(R.id.update_status, String.format(getResources().getString(R.string.update_status), item.getCount()));
            }
        };
        resultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeChildRecommendBean.VideoBean item = resultAdapter.getItem(position);
                if (item == null) return;
                Intent intent;
                if (item.getVm_type() == 2) {
                    intent = new Intent(VideoSearchActivity.this, TVDetailActivity.class);
                } else
                    intent = new Intent(VideoSearchActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", item.getVm_id());
                intent.putExtra("c_id", item.getVt_id_one());
                startActivity(intent);
                presenter.addSearchNum(item.getVm_id());
                finish();
            }
        });
        TextView textView = new TextView(this);
        textView.setTextColor(Color.WHITE);
        textView.setGravity(Gravity.CENTER);
        textView.setText(getString(R.string.nodata));
        resultAdapter.setEmptyView(textView);
        searchResult.setAdapter(resultAdapter);
    }

    @Override
    public void onBackPressed() {
        if (swipeRefreshLayout.getVisibility() == View.VISIBLE) {
            resultAdapter.getData().clear();
            resultAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setVisibility(View.GONE);
            lineRecommend.setVisibility(View.VISIBLE);
        } else
            super.onBackPressed();
    }

    private void initHotList() {
        hotAdapter = new BaseQuickAdapter<VideoRecmendSeachBean, BaseViewHolder>(R.layout.item_search_list) {
            @Override
            protected void convert(BaseViewHolder helper, VideoRecmendSeachBean item) {
                helper.setText(R.id.content, Util.showText(item.getVm_title(), item.getVm_title_z()));
                int parentPosition = hotAdapter.getData().indexOf(item);
                if (parentPosition == 0 || parentPosition == 1) {
                    helper.setImageResource(R.id.img, R.mipmap.ic_hot1);
                } else if (parentPosition == 2 || parentPosition == 3) {
                    helper.setImageResource(R.id.img, R.mipmap.ic_hot2);
                } else helper.setImageResource(R.id.img, R.mipmap.ic_hot3);
                helper.setText(R.id.content, Util.showText(item.getVm_title(), item.getVm_title_z()));
            }
        };
        hotAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                VideoRecmendSeachBean item = hotAdapter.getItem(position);
                if (item == null) return;
                Intent intent;
                if (item.getVm_type() == 2) {
                    intent = new Intent(VideoSearchActivity.this, TVDetailActivity.class);
                } else
                    intent = new Intent(VideoSearchActivity.this, MovieDetailActivity.class);
                intent.putExtra("id", item.getVm_id());
                intent.putExtra("c_id", 1);
                startActivity(intent);
                finish();
            }
        });
        hotRecycler.setAdapter(hotAdapter);
    }

    private void initT9Key() {
        ArrayList<T9KeyBean> t9KeyBeans = new ArrayList<>();
        t9KeyBeans.add(new T9KeyBean(0, 1));
        t9KeyBeans.add(new T9KeyBean(2, "A", "B", "C"));
        t9KeyBeans.add(new T9KeyBean(3, "D", "E", "F"));
        t9KeyBeans.add(new T9KeyBean(4, "G", "H", "I"));
        t9KeyBeans.add(new T9KeyBean(5, "J", "K", "L"));
        t9KeyBeans.add(new T9KeyBean(6, "M", "N", "O"));
        t9KeyBeans.add(new T9KeyBean(7, "P", "Q", "R", "S"));
        t9KeyBeans.add(new T9KeyBean(8, "T", "U", "V"));
        t9KeyBeans.add(new T9KeyBean(9, "W", "X", "Y", "Z"));
        t9KeyAdapter = new BaseQuickAdapter<T9KeyBean, BaseViewHolder>(R.layout.item_t9_key, t9KeyBeans) {
            @Override
            protected void convert(BaseViewHolder helper, T9KeyBean item) {
                helper.getView(R.id.line_uncheck).setOnFocusChangeListener(VideoSearchActivity.this);
                helper.getView(R.id.item_t9_key).setOnFocusChangeListener(VideoSearchActivity.this);
                helper.getView(R.id.line1).setOnFocusChangeListener(VideoSearchActivity.this);
                if (item.getNum2() == 0) {
                    helper.setVisible(R.id.line1, false)
                            .setVisible(R.id.line_uncheck, true)
                            .setVisible(R.id.line_checked, false)
                            .setGone(R.id.tv_top, true)
                            .setVisible(R.id.letter3, true);
                    if (TextUtils.isEmpty(item.getLetter4())) {
                        helper.setGone(R.id.letter4, false);
                    } else {
                        helper.setGone(R.id.letter4, true);
                    }
                    helper.setText(R.id.tv_num, String.valueOf(item.getNum1()))
                            .setText(R.id.tv_top, String.valueOf(item.getNum1()))
                            .setText(R.id.letter1, item.getLetter1())
                            .setText(R.id.letter2, item.getLetter2())
                            .setText(R.id.letter3, item.getLetter3())
                            .setText(R.id.letter4, item.getLetter4())
                            .setText(R.id.tv_letters, item.getLetter1() + item.getLetter2()
                                    + item.getLetter3() + item.getLetter4());
                } else {
                    View line_checked = helper.getView(R.id.line_checked);
                    FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) line_checked.getLayoutParams();
                    layoutParams.gravity = Gravity.CENTER_VERTICAL;
                    line_checked.setLayoutParams(layoutParams);
                    helper.setVisible(R.id.line1, true)
                            .setGone(R.id.tv_top, false)
                            .setVisible(R.id.line_checked, false)
                            .setVisible(R.id.line_uncheck, false)
                            .setText(R.id.letter1, "0")
                            .setText(R.id.letter2, "1")
                            .setGone(R.id.letter3, false)
                            .setGone(R.id.letter4, false);
                }
                helper.setNestView(R.id.letter1, R.id.letter2, R.id.letter3, R.id.letter4, R.id.tv_top, R.id.line_uncheck, R.id.line1);
            }
        };
//        t9KeyAdapter.setOnItemChildLongClickListener(new BaseQuickAdapter.OnItemChildLongClickListener() {
//            @Override
//            public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
//                View line_checked = t9KeyAdapter.getViewByPosition(t9Key, currentT9KeyPosition, R.id.line_checked);
//                View line1 = t9KeyAdapter.getViewByPosition(t9Key, currentT9KeyPosition, R.id.line1);
//                View line_uncheck = t9KeyAdapter.getViewByPosition(t9Key, currentT9KeyPosition, R.id.line_uncheck);
//                if (line1 != null) {
//                    if (currentT9KeyPosition == 0)
//                        line1.setVisibility(View.VISIBLE);
//                    else {
//                        line1.setVisibility(View.INVISIBLE);
//                    }
//                }
//                if (line_uncheck != null) {
//                    if (currentT9KeyPosition == 0)
//                        line_uncheck.setVisibility(View.INVISIBLE);
//                    else {
//                        line_uncheck.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (line_checked != null) {
//                    line_checked.setVisibility(View.INVISIBLE);
//                }
//                View tv_top = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.tv_top);
//                View letter1 = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.letter1);
//                line_checked = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.line_checked);
//                line1 = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.line1);
//                line_uncheck = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.line_uncheck);
//                if (line1 != null) {
//                    line1.setVisibility(View.INVISIBLE);
//                }
//                if (line_uncheck != null) {
//                    line_uncheck.setVisibility(View.INVISIBLE);
//                }
//                if (line_checked != null) {
//                    line_checked.setVisibility(View.VISIBLE);
//                }
//                if (position != 0) {
//                    if (tv_top != null) {
//                        tv_top.requestFocus();
//                    }
//                } else {
//                    if (letter1 != null) {
//                        letter1.setVisibility(View.VISIBLE);
//                        letter1.requestFocus();
//                    }
//                }
//                currentT9KeyPosition = position;
//                return true;
//            }
//        });
//        t9KeyAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//                View line_checked = t9KeyAdapter.getViewByPosition(t9Key, currentT9KeyPosition, R.id.line_checked);
//                View line1 = t9KeyAdapter.getViewByPosition(t9Key, currentT9KeyPosition, R.id.line1);
//                View line_uncheck = t9KeyAdapter.getViewByPosition(t9Key, currentT9KeyPosition, R.id.line_uncheck);
//                if (line1 != null) {
//                    if (currentT9KeyPosition == 0)
//                        line1.setVisibility(View.VISIBLE);
//                    else {
//                        line1.setVisibility(View.INVISIBLE);
//                    }
//                }
//                if (line_uncheck != null) {
//                    if (currentT9KeyPosition == 0)
//                        line_uncheck.setVisibility(View.INVISIBLE);
//                    else {
//                        line_uncheck.setVisibility(View.VISIBLE);
//                    }
//                }
//                if (line_checked != null) {
//                    line_checked.setVisibility(View.INVISIBLE);
//                }
//                View tv_top = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.tv_top);
//                View letter1 = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.letter1);
//                line_checked = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.line_checked);
//                line1 = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.line1);
//                line_uncheck = t9KeyAdapter.getViewByPosition(t9Key, position, R.id.line_uncheck);
//                if (line1 != null) {
//                    line1.setVisibility(View.INVISIBLE);
//                }
//                if (line_uncheck != null) {
//                    line_uncheck.setVisibility(View.INVISIBLE);
//                }
//                if (line_checked != null) {
//                    line_checked.setVisibility(View.VISIBLE);
//                }
//                if (position != 0) {
//                    if (tv_top != null) {
//                        tv_top.requestFocus();
//                    }
//                } else {
//                    if (letter1 != null) {
//                        letter1.requestFocus();
//                    }
//                }
//                currentT9KeyPosition = position;
//                return true;
//            }
//        });
        t9KeyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int selectionStart = tvSearch.getSelectionStart();
                Editable editable = tvSearch.getText();
                switch (view.getId()) {
                    case R.id.line_uncheck:
                        T9KeyBean item = t9KeyAdapter.getItem(position);
                        editable.insert(selectionStart, String.valueOf(item.getNum1()));
                        break;
                    case R.id.tv_top:
                    case R.id.letter1:
                    case R.id.letter2:
                    case R.id.letter3:
                    case R.id.letter4:
                        if (view instanceof TextView)
                            editable.insert(selectionStart, ((TextView) view).getText());
                        break;

                }
            }
        });
        t9KeyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int selectionStart = tvSearch.getSelectionStart();
                Editable editable = tvSearch.getText();
                if (position == 0) {
                    editable.insert(selectionStart, "0");
                } else {
                    T9KeyBean item = t9KeyAdapter.getItem(position);
                    editable.insert(selectionStart, String.valueOf(item.getNum1()));
                }
            }
        });

        t9Key.setAdapter(t9KeyAdapter);
    }

    private void initFullKey() {
        String[] stringArray = getResources().getStringArray(R.array.full_key_array);
        List<String> strings_full = Arrays.asList(stringArray);
        BaseQuickAdapter<String, BaseViewHolder> fullKeyAdapter =
                new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_full_key, strings_full) {
                    @Override
                    protected void convert(BaseViewHolder helper, String item) {
                        helper.setText(R.id.tv_key, item);
                    }
                };
        fullKeyAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int selectionStart = tvSearch.getSelectionStart();
                Editable editable = tvSearch.getText();
                editable.insert(selectionStart, fullKeyAdapter.getItem(position));
            }
        });
        fullKey.setAdapter(fullKeyAdapter);
    }

    @Override
    public int setLayoutId() {
        return R.layout.activity_search;
    }

    @OnClick({R.id.key_clear, R.id.key_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.key_clear:
                tvSearch.setText("");
                break;
            case R.id.key_back:
                int selectionStart = tvSearch.getSelectionStart();
                Editable editable = tvSearch.getText();
                if (editable != null && editable.length() > 0) {
                    if (selectionStart > 0) {
                        editable.delete(selectionStart - 1, selectionStart);
                    }
                }
                break;
        }
    }

    @Override
    public void showVideoOrders(List<VideoRecmendSeachBean> serachVideoBeans) {
        hotAdapter.setNewData(serachVideoBeans);
    }

    @Override
    public void showVideos(List<HomeChildRecommendBean.VideoBean> videoBeans) {
        lineRecommend.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(false);
        resultAdapter.setNewData(videoBeans);
    }

    @Override
    public void showError(String msg) {
        ToastUtil.showText(msg);
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            if (v.getId() == R.id.radio_t9) {
                radioT9.setChecked(true);
            }
            if (v.getId() == R.id.radio_full) {
                radioFull.setChecked(true);
            }
            LogUtil.e(v.getClass().getSimpleName() + v.getId() + v.toString());
            if (v.getId() == R.id.item_t9_key || v.getId() == R.id.line_uncheck || v.getId() == R.id.line1) {
                View line_checked = t9KeyAdapter.getViewByPosition(t9Key, currentT9KeyPosition, R.id.line_checked);
                View line1 = t9KeyAdapter.getViewByPosition(t9Key, currentT9KeyPosition, R.id.line1);
                View line_uncheck = t9KeyAdapter.getViewByPosition(t9Key, currentT9KeyPosition, R.id.line_uncheck);
                if (line1 != null) {
                    if (currentT9KeyPosition == 0)
                        line1.setVisibility(View.VISIBLE);
                    else {
                        line1.setVisibility(View.INVISIBLE);
                    }
                }
                if (line_uncheck != null) {
                    if (currentT9KeyPosition == 0)
                        line_uncheck.setVisibility(View.INVISIBLE);
                    else {
                        line_uncheck.setVisibility(View.VISIBLE);
                    }
                }
                if (line_checked != null) {
                    line_checked.setVisibility(View.INVISIBLE);
                }
                ViewParent parent = v.getParent();
                int childAdapterPosition = t9Key.getChildAdapterPosition((View) parent);
                View tv_top = t9KeyAdapter.getViewByPosition(t9Key, childAdapterPosition, R.id.tv_top);
                View letter1 = t9KeyAdapter.getViewByPosition(t9Key, childAdapterPosition, R.id.letter1);
                line_checked = t9KeyAdapter.getViewByPosition(t9Key, childAdapterPosition, R.id.line_checked);
                line1 = t9KeyAdapter.getViewByPosition(t9Key, childAdapterPosition, R.id.line1);
                line_uncheck = t9KeyAdapter.getViewByPosition(t9Key, childAdapterPosition, R.id.line_uncheck);
                if (line1 != null) {
                    line1.setVisibility(View.INVISIBLE);
                }
                if (line_uncheck != null) {
                    line_uncheck.setVisibility(View.INVISIBLE);
                }
                if (line_checked != null) {
                    line_checked.setVisibility(View.VISIBLE);
                }
                if (childAdapterPosition != 0) {
                    if (tv_top != null) {
                        tv_top.requestFocus();
                    }
                } else {
                    if (letter1 != null) {
                        letter1.requestFocus();
                    }
                }
                currentT9KeyPosition = childAdapterPosition;
            }
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio_t9) {
            t9Key.setVisibility(View.VISIBLE);
            fullKey.setVisibility(View.INVISIBLE);
        } else {
            t9Key.setVisibility(View.INVISIBLE);
            fullKey.setVisibility(View.VISIBLE);
        }
    }
}
