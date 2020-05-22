package com.tangmu.app.TengKuTV.module.home;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tangmu.app.TengKuTV.R;
import com.tangmu.app.TengKuTV.base.BaseFragment;
import com.tangmu.app.TengKuTV.bean.CategoryBean;
import com.tangmu.app.TengKuTV.component.AppComponent;
import com.tangmu.app.TengKuTV.module.book.BookActivity;
import com.tangmu.app.TengKuTV.utils.GlideApp;
import com.tangmu.app.TengKuTV.utils.GlideUtils;
import com.tangmu.app.TengKuTV.utils.Util;

import java.util.List;

import butterknife.BindView;

public class ChannelFragment extends BaseFragment {


    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerview;
    private BaseQuickAdapter<CategoryBean, BaseViewHolder> channelAdapter;

    @Override
    protected void initData() {
        List<CategoryBean> category = (List<CategoryBean>) getArguments().getSerializable("Category");
        if (category != null) {
            category.remove(category.size() - 1);
            CategoryBean categoryBean = new CategoryBean();
            categoryBean.setVt_title("有声读物");
            categoryBean.setVt_title_z("སྒྲ་ལྡན་ཀློག་དེབ།");
            category.add(categoryBean);
            channelAdapter.setNewData(category);
        }
    }

    @Override
    protected void initView() {
        channelAdapter = new BaseQuickAdapter<CategoryBean, BaseViewHolder>(R.layout.item_channel) {
            @Override
            protected void convert(BaseViewHolder helper, CategoryBean item) {
                if (channelAdapter.getData().indexOf(item) == channelAdapter.getItemCount() - 1) {
                    GlideApp.with(ChannelFragment.this).load(R.mipmap.ic_live).into((ImageView) helper.getView(R.id.image));
                } else if (item.getVt_pid() == -2) {
                    GlideApp.with(ChannelFragment.this).load(R.mipmap.ic_book).into((ImageView) helper.getView(R.id.image));
                } else
                    GlideUtils.getRequest(ChannelFragment.this, Util.convertImgPath(item.getVt_icon_img()))
                            .into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.name, Util.showText(item.getVt_title(), item.getVt_title_z()));
            }
        };
        channelAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (position == channelAdapter.getItemCount() - 1) {
                    startActivity(new Intent(getActivity(), BookActivity.class));
                } else {
                    Fragment fragment = getParentFragment();
                    if (fragment instanceof BaseFragment) {
                        ((HomeFragment) fragment).setFragment(position);
                    }
                }
            }
        });
        mRecyclerview.setAdapter(channelAdapter);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_channel;
    }

    @Override
    protected void setupFragComponent(AppComponent appComponent) {

    }

}
