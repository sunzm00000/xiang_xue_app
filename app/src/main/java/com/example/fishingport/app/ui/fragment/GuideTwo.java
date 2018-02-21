package com.example.fishingport.app.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fishingport.app.R;


/**
 * Created by Administrator on 2016/4/29.
 */
public class GuideTwo extends Fragment {
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.item_guide_one,container,false);
        ImageView guide_image= (ImageView) view.findViewById(R.id.guide_image);
        TextView txt_top= (TextView) view.findViewById(R.id.txt_top);
        TextView txt_down= (TextView) view.findViewById(R.id.txt_down);
        txt_top.setText("渔友圈");
        txt_down.setText("与朋友多些沟通、与家人多些联系");
        guide_image.setImageResource(R.mipmap.two);
        return view;

    }
}
