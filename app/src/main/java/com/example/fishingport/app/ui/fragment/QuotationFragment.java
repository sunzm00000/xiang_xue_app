package com.example.fishingport.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fishingport.app.R;
import com.github.library.BaseRecyclerAdapter;
import com.github.library.BaseViewHolder;
import com.github.library.animation.AnimationType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wushixin on 2017/4/18.
 * 行情
 */

public class QuotationFragment extends Fragment {
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<String> mAdapter;
    public static RecommendFragment newInstance(String info) {
        Bundle args = new Bundle();
        args.putString("info", info);
        RecommendFragment detailFragment = new RecommendFragment();
        detailFragment.setArguments(args);
        return detailFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, null);
        ButterKnife.bind(this, view);
        List<String> list=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            list.add(i+"");
        }
        mAdapter=new BaseRecyclerAdapter<String>(getActivity(), null, R.layout.item_exchange) {
            @Override
            protected void convert(final BaseViewHolder helper, final String item) {


            }
        };
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter.openLoadAnimation(AnimationType.ALPHA);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setData(list);
        return view;
    }
}