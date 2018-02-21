package com.example.fishingport.app.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.R;
import com.example.fishingport.app.model.MailListBean;
import com.example.fishingport.app.view.GlideCircleTransform;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wushixin on 2017/4/27.
 * 选择好友
 */

public class ChooseMemberAdapter extends RecyclerView.Adapter<ChooseMemberAdapter.ViewHolder> {
    protected Context mContext;
    protected List<MailListBean> mDatas;
    protected LayoutInflater mInflater;
    private String user_ids="";
    private List<String> listId=new ArrayList<>();

    public ChooseMemberAdapter(Context mContext, List<MailListBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    public List<MailListBean> getDatas() {
        return mDatas;
    }

    public ChooseMemberAdapter setDatas(List<MailListBean> datas) {
        mDatas = datas;
        return this;
    }

    @Override
    public ChooseMemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChooseMemberAdapter.ViewHolder(mInflater.inflate(R.layout.item_choosemember, parent, false));
    }

    @Override
    public void onBindViewHolder(final ChooseMemberAdapter.ViewHolder holder, final int position) {
        final MailListBean cityBean = mDatas.get(position);
        holder.tvCity.setText(cityBean.getCity());

        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    listId.add(cityBean.getId()+"");
                } else {
                    for (int i = 0; i <listId.size() ; i++) {
                        if (listId.get(i).equals(cityBean.getId()+"")){
                            listId.remove(i);
                        }
                    }
                }
            }
        });


//        holder.content.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (position>1) {
//                    Intent i=new Intent(mContext, FriendInfoActivity.class);
//                    i.putExtra("type",1);
//                    i.putExtra("uid",cityBean.getId()+"");
//                    mContext.startActivity(i);
//                } else if (position==0){
//
//                    mContext.startActivity(new Intent(mContext, NewsFriendActivity.class));
//                }else if (position==1){
//
//                    mContext.startActivity(new Intent(mContext, GroupListActivity.class));
//                }
//            }
//        });
//
//        if (position==0||position==1){
//            holder.avatar.setImageResource(cityBean.getIcon());
//        } else {
            Glide.with(mContext).load(cityBean.getAvatar()).transform(new GlideCircleTransform(mContext)).into(holder.avatar);
  //      }

        //
        //Glide.with(mContext).load(cityBean.getAvatar()).into(holder.avatar);
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;
        ImageView avatar;
        CheckBox mCheckBox;
        View content;

        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            avatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.mCheckBox);
            content = itemView.findViewById(R.id.content);
        }
    }

    public String setUser_ids(){
        user_ids="";
        for (int i = 0; i <listId.size() ; i++) {
            user_ids+=listId.get(i)+",";
        }
        return user_ids.substring(0,user_ids.length()-1);
    }
}

