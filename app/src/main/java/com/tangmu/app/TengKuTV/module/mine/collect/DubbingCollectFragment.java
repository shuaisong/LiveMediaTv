package com.tangmu.app.TengKuTV.module.mine.collect;

import android.view.View;
import android.widget.CheckBox;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.CollectBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.utils.CollectItemDecoration;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by lenovo on 2020/2/26.
 * auther:lenovo
 * 配音收藏
 * Date：2020/2/26
 */
public class DubbingCollectFragment extends BaseFragment {
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    @BindView(R.id.swip)
    SwipeRefreshLayout mSwip;
    private BaseQuickAdapter<CollectBean, BaseViewHolder> videoCollectAdapter;
    private boolean showCheck;

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {
        ArrayList<CollectBean> collectBeans = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            collectBeans.add(new CollectBean());
        }
        videoCollectAdapter.setNewData(collectBeans);
    }

    /**
     * 初始化view
     */
    @Override
    protected void initView() {
        mRecyclerview.addItemDecoration(new CollectItemDecoration());
        videoCollectAdapter = new BaseQuickAdapter<CollectBean, BaseViewHolder>(R.layout.item_collect) {

            @Override
            protected void convert(BaseViewHolder helper, CollectBean item) {
                if (showCheck) {
                    helper.setGone(R.id.check, true);
                } else
                    helper.setGone(R.id.check, false);
            }
        };
        videoCollectAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                videoCollectAdapter.loadMoreEnd();
            }
        }, mRecyclerview);
        videoCollectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (showCheck) {
                    CheckBox checkView = (CheckBox) videoCollectAdapter.getViewByPosition(position, R.id.check);
                    if (checkView != null) checkView.toggle();
                    return;
                }
                CollectBean item = videoCollectAdapter.getItem(position);
                if (item == null) return;
            }
        });
        mRecyclerview.setAdapter(videoCollectAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
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

    }

    public void showCheck(boolean showCheck) {
        this.showCheck = showCheck;
        videoCollectAdapter.notifyDataSetChanged();
    }

    public void delete() {
        // TODO: 2020/2/26
    }
}
