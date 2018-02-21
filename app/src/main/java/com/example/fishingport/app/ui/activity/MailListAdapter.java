package com.example.fishingport.app.ui.activity;

import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fishingport.app.Dao.PersonalDaoManager;
import com.example.fishingport.app.R;
import com.example.fishingport.app.bean.Personal;
import com.example.fishingport.app.bean.PersonalDao;
import com.example.fishingport.app.model.MailListBean;
import com.example.fishingport.app.view.GlideCircleTransform;
import com.example.fishingport.app.view.dragindicator.DragIndicatorView;

import java.util.List;

/**
 * Created by wushixin on 2017/4/18.
 *
 */
public class MailListAdapter extends RecyclerView.Adapter<MailListAdapter.ViewHolder> {
    protected Context mContext;
    protected List<MailListBean> mDatas;
    protected LayoutInflater mInflater;
    private  String num="0";
    public MailListAdapter(Context mContext, List<MailListBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }
    public List<MailListBean> getDatas() {
        return mDatas;
    }
    public MailListAdapter setDatas(List<MailListBean> datas,String num) {
        mDatas = datas;
        this.num=num;
        return this;
    }
    @Override
    public MailListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_maillist, parent, false));
    }
    @Override
    public void onBindViewHolder(final MailListAdapter.ViewHolder holder, final int position) {
        final MailListBean cityBean = mDatas.get(position);


        holder.tvCity.setText(cityBean.getCity());
        holder.content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 1) {
                    holder.dian.setVisibility(View.GONE);
                    Intent i = new Intent(mContext, FriendInfoActivity.class);
                    i.putExtra("type", 1);
                    i.putExtra("uid", cityBean.getId() + "");
                    i.putExtra("huanxin_name", cityBean.getHuanxin_name());
                    i.putExtra("avatar", cityBean.getAvatar());//头像
                    i.putExtra("nickname", cityBean.getCity());//昵称
                    mContext.startActivity(i);
                } else if (position == 0) {
                    //新渔友
                    if (num.equals("0")){
                        holder.dian.setVisibility(View.GONE);
                    }else {
                        holder.dian.setVisibility(View.VISIBLE);
                        holder.dian.setText(num);
                    }
                    mContext.startActivity(new Intent(mContext, NewsFriendActivity.class));
                } else if (position == 1) {
                    holder.dian.setVisibility(View.GONE);
                    mContext.startActivity(new Intent(mContext, GroupListActivity.class));
                }
            }
        });
        if (position == 0 || position == 1) {
            holder.avatar.setImageResource(cityBean.getIcon());
        } else {
            Glide.with(mContext).load(cityBean.getAvatar()).transform(new
                    GlideCircleTransform(mContext)).into(holder.avatar);
        }
        if (position>1) {
            PersonalDao personalDao = PersonalDaoManager.getInstance().getNewSession().getPersonalDao();
            String uid = cityBean.getId() + "";
            List<Personal> list = personalDao.queryBuilder().where(PersonalDao.Properties.Uid.eq(uid)).list();
            Log.i("list有没有", list.size() + "");
            if (list.size() == 0) {
                Log.i("走了这个4", "asdfsad" + "/" + cityBean.getAvatar() + "/" + cityBean.getHuanxin_name() +
                        "/" + cityBean.getId() + "/" + cityBean.getCity());
                Personal personal = new Personal();
                personal.setHead_img(cityBean.getAvatar());
                personal.setHuanxin_id(cityBean.getHuanxin_name());
                personal.setUid(cityBean.getId() + "");
                personal.setId(null);
                personal.setNickname(cityBean.getCity());
                personalDao.insert(personal);
            }else if (list.size()==1){
                //更新数据库
                Personal personal = new Personal();
                personal.setHead_img(cityBean.getAvatar());
                personal.setHuanxin_id(cityBean.getHuanxin_name());
                personal.setUid(cityBean.getId() + "");
                personal.setId(null);
                personal.setNickname(cityBean.getCity());
                personalDao.save(personal);//更新数据
            }
        }

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
        View content;
        DragIndicatorView dian;
        public ViewHolder(View itemView) {
            super(itemView);
            tvCity = (TextView) itemView.findViewById(R.id.tvCity);
            avatar = (ImageView) itemView.findViewById(R.id.ivAvatar);
            content = itemView.findViewById(R.id.content);
            dian= (DragIndicatorView) itemView.findViewById(R.id.txt_indicatorview);
        }
    }
}

