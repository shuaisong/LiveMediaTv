package com.tangmu.app.TengKuTV.module.mine.collect;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.CollectBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.component.DaggerFragmentComponent;
import com.tangmu.app.TengKuTV.contact.CollectContact;
import com.tangmu.app.TengKuTV.module.book.PlayBookActivity;
import com.tangmu.app.TengKuTV.module.dubbing.ShowDubbingVideoActivity;
import com.tangmu.app.TengKuTV.module.live.HistoryLiveActivity;
import com.tangmu.app.TengKuTV.module.live.LivingActivity;
import com.tangmu.app.TengKuTV.module.movie.MovieDetailActivity;
import com.tangmu.app.TengKuTV.module.movie.TVDetailActivity;
import com.tangmu.app.TengKuTV.presenter.CollectPresenter;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.ToastUtil;
import com.tangmu.app.TengKuTV.utils.Util;
import com.tangmu.app.TengKuTV.view.LoadingDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by lenovo on 2020/2/26.
 * auther:lenovo
 * 视频收藏
 * Date：2020/2/26
 */
public class CollectFragment extends BaseFragment implements CollectContact.View {
    @Inject
    CollectPresenter presenter;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swip)
    SwipeRefreshLayout mSwip;
    private BaseQuickAdapter<CollectBean, BaseViewHolder> collectAdapter;
    private int page = 1;
    private int type;
    private LoadingDialog loadingDialog;
    private View noData;
    private View noNet;

    public CollectFragment() {
    }

    public static CollectFragment getInstance(int type) {
        CollectFragment collectFragment = new CollectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        collectFragment.setArguments(bundle);
        return collectFragment;
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        type = getArguments().getInt("type");
        mSwip.setRefreshing(true);
        presenter.getCollects(page, type);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    /**
     * 初始化view
     */
    @Override
    protected void initView() {
        presenter.attachView(this);
        mSwip.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page = 1;
                presenter.getCollects(page, type);
            }
        });
        collectAdapter = new BaseQuickAdapter<CollectBean, BaseViewHolder>(R.layout.item_collect) {
            @Override
            protected void convert(BaseViewHolder helper, CollectBean item) {
                helper.setText(R.id.title, Util.showText(item.getTitle(), item.getTitle_z()));
                if (type == 4) {
                    GlideUtils.getRequest(CollectFragment.this, Util.convertVideoPath(item.getImg())).centerCrop().into((ImageView) helper.getView(R.id.image));
                } else
                    GlideUtils.getRequest(CollectFragment.this, Util.convertImgPath(item.getImg())).centerCrop().into((ImageView) helper.getView(R.id.image));
            }
        };
        View view = LayoutInflater.from(getContext()).inflate(R.layout.list_no_data, null);
        noData = view.findViewById(R.id.no_data);
        noNet = view.findViewById(R.id.no_net);
        collectAdapter.setEmptyView(view);
        collectAdapter.isUseEmpty(false);
        collectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                presenter.getCollects(page, type);
            }
        }, mRecyclerview);
        collectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CollectBean item = collectAdapter.getItem(position);
                if (item == null) return;
                Intent intent = null;
                switch (type) {
                    case 1:
                        if (item.getVm_type() == 2) {
                            intent = new Intent(getActivity(), TVDetailActivity.class);
                        } else
                            intent = new Intent(getActivity(), MovieDetailActivity.class);
                        intent.putExtra("id", item.getUc_audio_id());
                        intent.putExtra("c_id", item.getVt_id_one());
                        break;
                    case 3:
                        intent = new Intent(getActivity(), PlayBookActivity.class);
                        intent.putExtra("id", item.getUc_audio_id());
                        break;
                    case 4:
                        intent = new Intent(getActivity(), ShowDubbingVideoActivity.class);
                        intent.putExtra("id", item.getUc_audio_id());
                        break;
                }
                if (intent != null) startActivity(intent);
            }
        });
        mRecyclerview.setAdapter(collectAdapter);
        loadingDialog = new LoadingDialog(getContext());
    }


    /**
     * @return xml id
     */
    @Override
    protected int getLayoutID() {
        return R.layout.fragment_collect;
    }

    /**
     * mvp 关联
     *
     * @param appComponent appComponent
     */
    @Override
    protected void setupFragComponent(AppComponent appComponent) {
        DaggerFragmentComponent.builder().appComponent(appComponent).build().inject(this);
    }


    @Override
    public void unCollectSuccess() {
        loadingDialog.dismiss();
        mSwip.setRefreshing(true);
        page = 1;
        presenter.getCollects(page, type);
    }

    @Override
    public void showCollects(List<CollectBean> collectBeans) {
        if (mSwip.isRefreshing()) mSwip.setRefreshing(false);
        if (page == 1) {
            Util.setNodata(noData, noNet, false);
            collectAdapter.isUseEmpty(true);
            collectAdapter.setNewData(collectBeans);
        } else {
            collectAdapter.getData().addAll(collectBeans);
            if (collectBeans.size() < 20) {
                collectAdapter.loadMoreEnd();
            } else collectAdapter.loadMoreComplete();
        }
        page++;
    }

    @Override
    public void showCollectsFail(String msg) {
        if (mSwip.isRefreshing()) mSwip.setRefreshing(false);
        if (page == 1) {
            Util.setNodata(noData, noNet, true);
            collectAdapter.isUseEmpty(true);
            collectAdapter.notifyDataSetChanged();
        } else {
            collectAdapter.loadMoreFail();
        }
    }

    @Override
    public void showError(String msg) {
        loadingDialog.dismiss();
        ToastUtil.showText(msg);
    }

}
