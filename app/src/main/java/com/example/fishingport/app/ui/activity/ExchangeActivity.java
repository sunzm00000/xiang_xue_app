package com.example.fishingport.app.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.fishingport.app.R;
import com.example.fishingport.app.base.BaseAppCompatActivity;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/14.
 * 兑换好礼
 */

public class ExchangeActivity extends BaseAppCompatActivity {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_exchange;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        setToolBarTitle("兑换好礼");

        List<String> list=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            list.add(i+"");
        }
        mAdapter=new BaseRecyclerAdapter<String>(this, null, R.layout.item_exchange) {
            @Override
            protected void convert(final BaseViewHolder helper, final String item) {


            }


        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(list);

    }
}
