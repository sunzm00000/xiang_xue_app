package com.example.fishingport.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.fishingport.app.R;


/**
 * Created by Administrator on 2016/4/29.
 */
public class GuideOne extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.item_guide_one,container,false);
        ImageView guide_image= (ImageView) view.findViewById(R.id.guide_image);
        guide_image.setImageResource(R.mipmap.one);

        return view;

    }
}
